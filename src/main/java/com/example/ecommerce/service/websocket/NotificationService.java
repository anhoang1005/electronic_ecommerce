package com.example.ecommerce.service.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final static Logger logger = LoggerFactory.getLogger(NotificationService.class);
    public NotificationService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNoticeProductByAdmin(String shopCode, String message) {
        try {
            messagingTemplate.convertAndSend("/topic/admin-notice-product" + shopCode, message);
            logger.info("Message sent successfully: " + message);
        } catch (Exception e) {
            logger.error("Failed to send message: " + message, e);
        }

    }

//    public void sendNotificationToAdmins(String message) {
//        messagingTemplate.convertAndSend("/topic/admin-notice-product", message);
//    }
}
