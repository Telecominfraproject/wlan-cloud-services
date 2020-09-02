package com.telecominfraproject.wlan.portforwardinggateway.websocket;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author dtop
 *
 */
public class ForwarderSession {
    
    private int listenOnLocalPort;
    private String inventoryId;
    private String securityToken;
    private int connectToPortOnEquipment;
    private Socket localSocket;
    private ServerSocket serverSocket;
    private Thread socketStreamReaderThread;
    
    public String getSessionId(){
        return Integer.toString(listenOnLocalPort) + "-" + securityToken + "-" + inventoryId + "-"
                + Integer.toString(connectToPortOnEquipment);
    }
    
    public int getListenOnLocalPort() {
        return listenOnLocalPort;
    }
    
    public void setListenOnLocalPort(int listenOnLocalPort) {
        this.listenOnLocalPort = listenOnLocalPort;
    }
    
    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String qrCode) {
        this.inventoryId = qrCode;
    }

    public int getConnectToPortOnEquipment() {
        return connectToPortOnEquipment;
    }
    
    public void setConnectToPortOnEquipment(int connectToPortOnEquipment) {
        this.connectToPortOnEquipment = connectToPortOnEquipment;
    }
    
    public Socket getLocalSocket() {
        return localSocket;
    }
    
    public void setLocalSocket(Socket localSocket) {
        this.localSocket = localSocket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public Thread getSocketStreamReaderThread() {
        return socketStreamReaderThread;
    }

    public void setSocketStreamReaderThread(Thread socketStreamReaderThread) {
        this.socketStreamReaderThread = socketStreamReaderThread;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ForwarderSession [listenOnLocalPort=");
        builder.append(listenOnLocalPort);
        builder.append(", inventoryId=");
        builder.append(inventoryId);
        builder.append(", securityToken=");
        builder.append(securityToken);
        builder.append(", connectToPortOnEquipment=");
        builder.append(connectToPortOnEquipment);
        builder.append(", localSocket=");
        builder.append(localSocket);
        builder.append(", serverSocket=");
        builder.append(serverSocket);
        builder.append("]");
        return builder.toString();
    }
    
    
}
