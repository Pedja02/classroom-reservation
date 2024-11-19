/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.config.WebSocketConfig;
import com.NJT.WebApi.config.WebSocketConfig.SessionInfo;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    WebSocketConfig config;

    public void sendNotificationToAdmins() {
        for (Map.Entry<String, SessionInfo> en : config.getActiveSessions().entrySet()) {
            if (en.getValue().getRole().equals("ADMIN")) {
                messagingTemplate.convertAndSendToUser(en.getValue().getUsername(),
                        "/topic/notifications", "Novo obavestenje!");
            }
        }
    }
}
