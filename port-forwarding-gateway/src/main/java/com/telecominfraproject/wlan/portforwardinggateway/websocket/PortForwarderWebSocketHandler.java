package com.telecominfraproject.wlan.portforwardinggateway.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.telecominfraproject.wlan.core.model.utils.TokenEncoder;
import com.telecominfraproject.wlan.core.model.utils.TokenUtils;
import com.telecominfraproject.wlan.server.exceptions.GenericErrorException;

public class PortForwarderWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PortForwarderWebSocketHandler.class);

    private static final String CONNECT_TO_CE_PORT_COMMAND = "connect_to_CE_port:";
    private static final String CONNECTED_TO_CE_PORT_MSG = "connected_to_CE_port:";
    private static final String DISCONNECT_FROM_CE_PORT_COMMAND = "disconnect_from_CE_port:";
    private static final String DISCONNECTED_FROM_CE_PORT_MSG = "disconnected_from_CE_port:";
    private static final String DROPPED_CONNECTION_FROM_CE_PORT_MSG = "dropped_from_CE_port:";

    private final Map<String, ForwarderSession> sessionIdToForwarderSessionMap = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();
    
    private final String tokenEncryptionKey;
    
    public PortForwarderWebSocketHandler(String tokenEncryptionKey) {
        this.tokenEncryptionKey = tokenEncryptionKey;
    }

    /**
     * To test this method:
     *  curl --digest --user user:password --request POST --insecure --header "Content-Type: application/json; charset=utf-8" --data '' https://localhost:7072/api/portFwd/createSession/inventoryId/dev-ap-0001/port/22/
     *  
     * @param inventoryId
     * @param connectToPortOnEquipment
     * @return session id of a newly created forwarder session
     */
    public String startForwarderSession(final String inventoryId, int connectToPortOnEquipment){
        //inventoryId is used to tie ForwarderSession to WebSocketSession
        
        try {
            //we support only one session per port per CE
            //if another session already exists - do not create a new one
            for(ForwarderSession fs: sessionIdToForwarderSessionMap.values()){
                if(fs.getInventoryId().equals(inventoryId) && fs.getConnectToPortOnEquipment() == connectToPortOnEquipment){
                    String errMsg = "Only one session per port per CE is supported. Another session already exists "+fs.getSessionId()+". Remove that session if you want to start a new one.";
                    LOG.error("[{}] {}", inventoryId, errMsg);
                    throw new IllegalStateException(errMsg);
                }
            }
            
            final ServerSocket serverSocket = new ServerSocket(0);
            final int listenOnLocalPort = serverSocket.getLocalPort();
            final ForwarderSession forwarderSession = new ForwarderSession();
            forwarderSession.setListenOnLocalPort(listenOnLocalPort);
            forwarderSession.setInventoryId(inventoryId);
            forwarderSession.setConnectToPortOnEquipment(connectToPortOnEquipment);
            forwarderSession.setServerSocket(serverSocket);
            
            String token = Long.toString(System.currentTimeMillis()) + Long.toString(Math.round(1000000 * Math.random()));
            String encryptedToken = TokenUtils.encrypt(token, tokenEncryptionKey, TokenEncoder.Base62TokenEncoder);
            // we're using @ symbol to append host name to the end of the local session Id,
            // so here we're making sure that '@' does not appear in the local session Id
            // and so that we can parse it out later
            encryptedToken.replace("@", "A");
            forwarderSession.setSecurityToken(encryptedToken);
            
            final WebSocketSession webSocketSession = webSocketSessionMap.get(forwarderSession.getInventoryId());
            if(webSocketSession==null){
                String errMsg = "Failed to start debug session, no websocket session for device";
                LOG.error("[{}] {}", inventoryId, errMsg);
                throw new IllegalStateException(errMsg);
            }
            

            LOG.info("[{}] starting portForwarder listening on {}", inventoryId, listenOnLocalPort);
            Thread acceptorThread = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    LOG.info("[{}] started portForwarder server on {}", inventoryId, listenOnLocalPort);
                    try {
                        final Socket localSocket = serverSocket.accept();
                        LOG.info("[{}] portForwarder accepted connection on port {} from {}:{}", 
                                inventoryId, localSocket.getLocalPort(), localSocket.getInetAddress(), localSocket.getPort());
                        
                        //do not accept any more connections
                        serverSocket.close();
                        
                        forwarderSession.setLocalSocket(localSocket);
                        
                        //set up reader for localSocket input stream 
                        Thread socketInputStreamReaderThread = new Thread(new Runnable() {
                            
                            @Override
                            public void run() {
                                LOG.info("[{}] portForwarder forwarding connection on port {} from {}:{}", 
                                        inventoryId, localSocket.getLocalPort(), localSocket.getInetAddress(), localSocket.getPort());

                                WebSocketSession webSocketSession = webSocketSessionMap.get(forwarderSession.getInventoryId());
                                int port = forwarderSession.getListenOnLocalPort();
                                
                                if(webSocketSession == null){
                                    //CE is not connected, cannot establish forwarder session
                                    LOG.error("[{}] CE is not connected, cannot establish forwarder session {}", inventoryId, forwarderSession.getSessionId());
                                    try {
                                        localSocket.close();
                                        forwarderSession.getServerSocket().close();
                                    } catch (IOException e) {
                                        //do nothing here
                                    }
                                    return;
                                }
                                
                                try {
                                    InputStream is = localSocket.getInputStream();
                                    byte[] buf = new byte[1024];
                                    int len;
                                    LOG.debug("[{}] Started polling inputstream on local socket {}", inventoryId, localSocket.getPort());
                                    boolean tokenFound = false;
                                    
                                    while((len = is.read(buf)) >= 0){
                                        if(len > 0){
                                            byte[] bufCopy = new byte[len];
                                            System.arraycopy(buf, 0, bufCopy, 0, len);
                                            ByteBuffer byteBuffer = ByteBuffer.allocate(len + 4); //4 bytes for integer port header
                                            byteBuffer.putInt(forwarderSession.getConnectToPortOnEquipment());
                                            byteBuffer.put(bufCopy);

                                            
                                            LOG.debug("[{}] Session {} read from local socket {} : {} bytes", inventoryId, forwarderSession.getSessionId(), port, len);
                                            if(LOG.isTraceEnabled()){
                                                LOG.trace("[{}] msg='{}'", inventoryId, new String(bufCopy, 0, bufCopy.length<256?bufCopy.length:256));
                                            }
                                            
                                            if(!tokenFound){
                                                String connectionBanner = new String(bufCopy, 0, bufCopy.length<256?bufCopy.length:256);
                                                int tokenStart = connectionBanner.indexOf(' ') + 1;
                                                int tokenEnd = connectionBanner.indexOf("\r\n", tokenStart);
                                                if(tokenStart > 0 && tokenEnd > 0){
                                                    String token = connectionBanner.substring(tokenStart, tokenEnd);
                                                    if(token.equals(forwarderSession.getSecurityToken())){
                                                        tokenFound = true;
                                                    }
                                                }
                                                
                                                if(!tokenFound){
                                                    //either token mismatch or token was not provided - close forwarder session and sockets
                                                    LOG.warn("[{}] Token mismatch or token was not provided. Closing session {}. "
                                                            + "\nNext time use command:\nexport SSH_BANNER_COMMENTS=\"secret_token_123\" ; "
                                                            + "openssh-portable/ssh -p 56425 user@debugGatewayHost",
                                                            inventoryId , 
                                                            forwarderSession.getSessionId());
                                                    break;
                                                }
                                            }
                                            
                                            byteBuffer.flip();
                                            BinaryMessage msgFwd = new BinaryMessage(byteBuffer);
                                            webSocketSession.sendMessage(msgFwd);
                                            if(LOG.isTraceEnabled()){
                                                LOG.trace("[{}] Session {} forwarded to websocket {} : {} bytes", inventoryId, forwarderSession.getSessionId(), port, len);
                                            }
                                        } else {
                                            //sleep a bit if no data is available
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                //do nothing
                                                Thread.currentThread().interrupt();
                                            }
                                        }
                                    }
                                    LOG.debug("[{}] Stopped polling inputstream on local socket {}", inventoryId, localSocket.getPort());
                                    
                                } catch (IOException e) {
                                    LOG.error("[{}] Session {} got exception {} for the socket on port {}", inventoryId, forwarderSession.getSessionId(), e, port);
                                } finally {
                                    
                                    //notify the other end that forwarding session is terminated 
                                    WebSocketMessage<String> retMsg = new TextMessage(DISCONNECT_FROM_CE_PORT_COMMAND + port);
                                    try {
                                        if(webSocketSession.isOpen()){
                                            webSocketSession.sendMessage(retMsg);
                                        }
                                    } catch (IOException e) {
                                        LOG.error("[{}] Session {} got exception {} for the socket on port {}", inventoryId, forwarderSession.getSessionId(), e, port);
                                    }
                                    
                                    //clean up local resources and sessions
                                    try {
                                        localSocket.close();
                                        forwarderSession.getServerSocket().close();
                                    } catch (IOException e) {
                                        //do nothing here
                                    }

                                    sessionIdToForwarderSessionMap.remove(forwarderSession.getSessionId());
                                    
                                    LOG.info("[{}] Session {} dropped connection on port {}", inventoryId, forwarderSession.getSessionId(), port);
                                }                                
                            }
                        });

                        //send initial control 
                        //message over websocket session (found by inventoryId) to connect to target port on the client side of the port forwarder
                        TextMessage message = new TextMessage(CONNECT_TO_CE_PORT_COMMAND + forwarderSession.getConnectToPortOnEquipment());
                        webSocketSession.sendMessage(message);
                        
                        LOG.debug("[{}] Session {} sent command {}", inventoryId, forwarderSession.getSessionId(), message.getPayload());

                        //Create reader thread and store it in the forwarderSession. 
                        //This thread will be started when CE replies with CONNECTED_TO_CE_PORT_COMMAND
                        socketInputStreamReaderThread.setName("Socket-Reader-"+ forwarderSession.getSessionId());
                        socketInputStreamReaderThread.setDaemon(true);
                        forwarderSession.setSocketStreamReaderThread(socketInputStreamReaderThread);

                    } catch (Exception e) {
                        LOG.error("[{}] error accepting conection on port {} - closing forwarding session {}", inventoryId, listenOnLocalPort, forwarderSession.getSessionId());
                        try {
                            serverSocket.close();
                        } catch (IOException e1) {
                            // do nothing here
                        }
                        sessionIdToForwarderSessionMap.remove(forwarderSession.getSessionId());
                    }
                }
            });

            sessionIdToForwarderSessionMap.put(forwarderSession.getSessionId(), forwarderSession);

            acceptorThread.setName("Acceptor-"+ forwarderSession.getSessionId());
            acceptorThread.setDaemon(true);
            acceptorThread.start();

            return forwarderSession.getSessionId();

        } catch (IOException e1) {
            throw new GenericErrorException("Cannot set up local listening socket", e1);
        }
        
    }
    
    public void stopForwarderSession(String sessionId){
        LOG.debug("Received stop forwarding Session request for sessionId {}", sessionId);
        ForwarderSession forwarderSession = sessionIdToForwarderSessionMap.get(sessionId);
        if(forwarderSession==null){
            LOG.info("Could not find session {}", sessionId);
            return;
        }
        
        try {
            LOG.debug("Found forwarderSession {} for sessionId {}", forwarderSession, sessionId);
            //find websocket session by inventoryId and send control messages to disconnect from target port on the client side of the port forwarder
            WebSocketSession webSocketSession = webSocketSessionMap.get(forwarderSession.getInventoryId());
            TextMessage message = new TextMessage(DISCONNECT_FROM_CE_PORT_COMMAND + forwarderSession.getConnectToPortOnEquipment());
            if(webSocketSession.isOpen()){
                webSocketSession.sendMessage(message);
                LOG.debug("[{}] Session {} sent command {}", forwarderSession.getInventoryId(), forwarderSession.getSessionId(), message.getPayload());
            }

            if(forwarderSession.getServerSocket() != null){
                forwarderSession.getServerSocket().close();
                LOG.debug("Closed forwarderSession server socket for sessionId {}", sessionId);
            }
            
            if(forwarderSession.getLocalSocket() != null){
                forwarderSession.getLocalSocket().close();
                LOG.debug("Closed forwarderSession local socket for sessionId {}", sessionId);
            }
            //stream reader will stop in a separate thread by themselves
            
            sessionIdToForwarderSessionMap.remove(forwarderSession.getSessionId());

        } catch (Exception e) {
            // do nothing here
            LOG.error("Encountered exception when closing connection for forwarder session {}", forwarderSession, e);
            sessionIdToForwarderSessionMap.remove(forwarderSession.getSessionId());
        }

        LOG.info("[{}] Stopped forwarder session {}", forwarderSession.getInventoryId(), sessionId);
    }
    
    public ForwarderSession getForwardingSession(String sessionId) {
        return sessionIdToForwarderSessionMap.get(sessionId);
    }
    
    public List<String> getForwardingSessions(){
        return new ArrayList<>(sessionIdToForwarderSessionMap.keySet());
    }
    
    public boolean isWebSocketSessionExist(String inventoryId){
        WebSocketSession wsSession = webSocketSessionMap.get(inventoryId);
        return wsSession!=null && wsSession.isOpen() && wsSession.getPrincipal()!=null;
    }
    
    /**
     * Text messages are used for control, and binary messages for data
     *  (non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleTextMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.TextMessage)
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String webSocketSessionKey = getWebSocketSessionKey(session);
        String payload = message.getPayload();
        
        LOG.debug("[{}] Incoming control message {}", webSocketSessionKey, message.getPayload());
        int port = -1;
        if(payload.indexOf(':')>0){
            port = Integer.parseInt(payload.substring(payload.indexOf(':')+1));
        }
        LOG.debug("handleTextMessage: Port {} is used on Equipment {}", port, webSocketSessionKey);
        //find forwarderSession by inventoryId and CEPort
        ForwarderSession forwarderSession = null;
        for(ForwarderSession fs: sessionIdToForwarderSessionMap.values()){
            if(fs.getInventoryId().equals(webSocketSessionKey) && fs.getConnectToPortOnEquipment()==port){
                forwarderSession = fs;
                break;
            }
        }

        LOG.debug("[{}] Session {} got message {} with forwarderSession {}", webSocketSessionKey, session, payload, forwarderSession);

        if(payload.startsWith(CONNECTED_TO_CE_PORT_MSG)){
            //start reader thread to forward packets from local socket to websocket
            if (forwarderSession != null) {
                forwarderSession.getSocketStreamReaderThread().start();
            }
        } else if(payload.startsWith(DISCONNECTED_FROM_CE_PORT_MSG) || payload.startsWith(DROPPED_CONNECTION_FROM_CE_PORT_MSG)){
            
            //disconnect all local sockets, if any
            if(forwarderSession!=null){
                if(forwarderSession.getServerSocket()!=null && !forwarderSession.getServerSocket().isClosed()){
                    forwarderSession.getServerSocket().close();
                }
                
                if(forwarderSession.getLocalSocket()!=null && !forwarderSession.getLocalSocket().isClosed()){
                    forwarderSession.getLocalSocket().close();
                }
                
                sessionIdToForwarderSessionMap.remove(forwarderSession.getSessionId());

                LOG.debug("[{}] Cleaned up forwarder session {}", forwarderSession.getInventoryId(), forwarderSession.getSessionId());
            }
            
        } else if("hello".equals(payload)){
            LOG.debug("[{}] Port forwarder client connected to {}", webSocketSessionKey, webSocketSessionKey);
        } else {
            LOG.error("[{}] Unknown control message : {} ", webSocketSessionKey, payload);    
        }
        
    }

    /**
     * Binary messages are used for data
     *  (non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleBinaryMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.BinaryMessage)
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        
        //TODO: may need to have message ack for each binary packet, and do not send the next packet until previous one has been acknowledged
        //DT: this has not been an issue so far
        
        String webSocketSessionKey = getWebSocketSessionKey(session);
        
        ByteBuffer payload = message.getPayload();
        int msgPayloadLength = message.getPayloadLength();
        
        int port = payload.getInt();
        LOG.debug("handleBinaryMessage: Port {} is used on Equipment {}", port, webSocketSessionKey);

        //find forwarderSession by inventoryId and CEPort
        ForwarderSession forwarderSession = null;
        for(ForwarderSession fs: sessionIdToForwarderSessionMap.values()){
            if(fs.getInventoryId().equals(webSocketSessionKey) && fs.getConnectToPortOnEquipment()==port){
                forwarderSession = fs;
                break;
            }
        }

        byte[] fwdPayload = new byte[msgPayloadLength  - 4]; 
        payload.get(fwdPayload);

        LOG.debug("[{}] Incoming binary message for port {} size {} ", webSocketSessionKey, port, msgPayloadLength );
        if(LOG.isTraceEnabled()){
            LOG.trace("[{}] msg='{}'", webSocketSessionKey, new String(fwdPayload, 0, fwdPayload.length<256?fwdPayload.length:256));
        }

        if(forwarderSession != null && forwarderSession.getLocalSocket()!=null && !forwarderSession.getLocalSocket().isClosed() && forwarderSession.getLocalSocket().isConnected()){
            try{
                forwarderSession.getLocalSocket().getOutputStream().write(fwdPayload);
                forwarderSession.getLocalSocket().getOutputStream().flush();
                if(LOG.isDebugEnabled()){
                    LOG.debug("[{}] Session {} wrote to local socket : {} bytes ", forwarderSession.getInventoryId(), forwarderSession.getSessionId(), fwdPayload.length );
                }
            } catch (IOException e) {
                WebSocketMessage<String> retMsg = new TextMessage(DISCONNECT_FROM_CE_PORT_COMMAND + port);
                try {
                    if(session.isOpen()){
                        session.sendMessage(retMsg);
                    }
                } catch (IOException e1) {
                    LOG.error("[{}] Session {} got exception {} for the socket on port {}", forwarderSession.getInventoryId(), session, e1, port);
                }
                LOG.debug("[{}] Session {} dropped connection", forwarderSession.getInventoryId(), forwarderSession.getSessionId());
            }
        } else  {
            LOG.debug("[{}] Session {} received message that cannot be delivered because local socket is inoperable {}", webSocketSessionKey, session, message);
            if (forwarderSession == null) {
                LOG.debug("forwarderSession not found fpr webSocketSessionKey {}", webSocketSessionKey);
            } else if (forwarderSession.getLocalSocket() == null) {
                LOG.debug("forwarderSession local socket is null for webSocketSessionKey {}", webSocketSessionKey);
            } else {
                LOG.debug("forwarderSession local socket for webSocketSessionKey {} is closed = {} and connected = {} ",
                        webSocketSessionKey, forwarderSession.getLocalSocket().isClosed(), forwarderSession.getLocalSocket().isConnected());
            }

        }
        
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LOG.error("[{}] Not supported pong message {}", getWebSocketSessionKey(session), message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.error("[{}] Transport Error for Session {} : {}", getWebSocketSessionKey(session), session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String webSocketSessionKey = getWebSocketSessionKey(session);

        webSocketSessionMap.put(webSocketSessionKey, session);
        LOG.info("[{}] New portForwarder websocket connection {} : {}", webSocketSessionKey, webSocketSessionKey, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String webSocketSessionKey = getWebSocketSessionKey(session);
        LOG.info("[{}] Closed portForwarder websocket connection {} : {}", webSocketSessionKey, session, closeStatus);

        webSocketSessionMap.remove(webSocketSessionKey);

        LOG.debug(" Removed key {} from webSocketSessionMap", webSocketSessionKey);
        //close and remove all forwarder sessions for that CE

        Iterator<ForwarderSession> iter = sessionIdToForwarderSessionMap.values().iterator();
        while(iter.hasNext()){
            ForwarderSession fs = iter.next();
            if (fs.getInventoryId().equals(webSocketSessionKey)) {
                LOG.debug("Closing webSocketSession for forwarderSession: {} ", fs);
                if(fs.getLocalSocket()!=null && !fs.getLocalSocket().isClosed()){
                    fs.getLocalSocket().close();
                    LOG.debug("Closing local Socket for fs {}", fs);
                }
                if(fs.getServerSocket()!=null && !fs.getServerSocket().isClosed()){
                    fs.getServerSocket().close();
                    LOG.debug("Closing Server Socket for fs {}", fs);
                }
            }
            iter.remove();
        }

    }

    private String getWebSocketSessionKey(WebSocketSession session) {
        return session.getPrincipal().getName();
    }


}

