package com.example.ecommerce.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WebSocketEventListener {

    private final Set<String> onlineCustomer = ConcurrentHashMap.newKeySet();

    private final Set<String> onlineShop = ConcurrentHashMap.newKeySet();
    private final SimpMessageSendingOperations messagingTemplate;
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        String header = event.getMessage().getHeaders().toString();
        Pattern patternShop = Pattern.compile("shopCode=(\\w+)");
        Pattern patternUser = Pattern.compile("userCode=(\\w+)");
        Matcher matcherShop = patternShop.matcher(header);
        Matcher matcherUser = patternUser.matcher(header);
        String userCode = null;
        String shopCode = null;
        if (matcherShop.find()) {
            shopCode = matcherShop.group(1);
            onlineShop.add(shopCode);
            log.info("Shop connected: " + shopCode);
        }
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineCustomer.add(userCode);
            log.info("User connected: " + userCode);
        }
        messagingTemplate.convertAndSend("/topic/customer/status", onlineCustomer);
        messagingTemplate.convertAndSend("/topic/shop/status", onlineShop);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        //StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String header = event.getMessage().getHeaders().toString();
        Pattern patternShop = Pattern.compile("shopCode=(\\w+)");
        Pattern patternUser = Pattern.compile("userCode=(\\w+)");
        Matcher matcherShop = patternShop.matcher(header);
        Matcher matcherUser = patternUser.matcher(header);
        String userCode = null;
        String shopCode = null;
        if (matcherShop.find()) {
            shopCode = matcherShop.group(1);
            onlineShop.remove(shopCode);
            messagingTemplate.convertAndSend("/topic/shop/status", onlineShop);
            log.info("Shop disconnected: " + shopCode);
        }
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineCustomer.remove(userCode);
            messagingTemplate.convertAndSend("/topic/customer/status", onlineCustomer);
            log.info("User disconnected: " + userCode);
        }
    }
}
