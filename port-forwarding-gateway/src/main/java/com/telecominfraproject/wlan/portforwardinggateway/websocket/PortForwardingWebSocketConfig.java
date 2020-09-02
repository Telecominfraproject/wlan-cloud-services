package com.telecominfraproject.wlan.portforwardinggateway.websocket;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class PortForwardingWebSocketConfig implements WebSocketConfigurer {
    private static final long DEFAULT_IDLE_TIMEOUT = TimeUnit.SECONDS.toMicros(90);

    @Value("${tip.wlan.websocketSessionTokenEncryptionKey:superToKeN123456}")
    private String tokenEncryptionKey;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(portForwardingHandler(), "/").setAllowedOrigins("http://origin");
    }

    @Bean
    public WebSocketHandler portForwardingHandler() {
        return new PortForwarderWebSocketHandler(tokenEncryptionKey);
    }

    @Bean
    @Profile("!integration_test")
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        // also see
        // https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Web_Server/2.1/html/HTTP_Connectors_Load_Balancing_Guide/Implementing_WebSocket_on_Tomcat.html
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout(DEFAULT_IDLE_TIMEOUT);
        return container;
    }
}
