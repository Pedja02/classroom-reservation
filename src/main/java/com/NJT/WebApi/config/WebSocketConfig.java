/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.config;

import com.NJT.WebApi.model.user.User;
import jakarta.persistence.Entity;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 *
 * @author Pera
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private Map<String, SessionInfo> activeSessions = new HashMap<>();

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("http://localhost:4200")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory((delegate) -> new WebSocketHandlerDecorator(delegate) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String query = session.getUri().getQuery();
                
                String sessionId = session.getId();
                String username = session.getPrincipal() != null ? session.getPrincipal().getName() : "";
                String role = "";
                if(query != null){
                    if (query.contains("ADMIN")) {
                    role = "ADMIN";
                } else if (query.contains("USER")) {
                    role = "USER";
                }
                }
                
                SessionInfo sessionInfo = new SessionInfo(sessionId, username, role);
                activeSessions.put(sessionId, sessionInfo);

                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                activeSessions.remove(session.getId());
                super.afterConnectionClosed(session, closeStatus);
            }
        });
    }

    public Map<String, SessionInfo> getActiveSessions() {
        return activeSessions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class SessionInfo {

        private String sessionId;
        private String username;
        private String role;
    }
    
}
