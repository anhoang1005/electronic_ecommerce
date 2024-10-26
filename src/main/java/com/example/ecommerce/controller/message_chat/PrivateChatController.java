package com.example.ecommerce.controller.message_chat;

import com.example.ecommerce.models.message_chat.SocketMessage;
import com.example.ecommerce.service.MessageChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@AllArgsConstructor
public class PrivateChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageChatService messageChatService;

    @MessageMapping("/chat.sendShop/{shopCode}")
    @SendTo("/topic/shop/{shopCode}")
    public SocketMessage sendMessageToShop(@Payload SocketMessage message,
                                  @DestinationVariable String shopCode) {
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        message.setCreatedAt(formattedTime);
        if(message.getType().toString().equals("SENT")){
            messageChatService.userSendMessage(message, false);
        } else if(message.getType().toString().equals("SEEN")){
            System.out.println("OK");
            messageChatService.userSeenMessage(message, true);
        }
        System.out.println(message.getUserCode() + " sent to " + message.getShopCode());
        return message;
    }

    @MessageMapping("/chat.sendCustomer/{customerCode}")
    @SendTo("/topic/customer/{customerCode}")
    public SocketMessage sendMessageToCustomer(@Payload SocketMessage message,
                                  @DestinationVariable String customerCode) {
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        message.setCreatedAt(formattedTime);
        if(message.getType().toString().equals("SENT")) {
            messageChatService.userSendMessage(message, true);
        } else if(message.getType().toString().equals("SEEN")){
            System.out.println("OK");
            messageChatService.userSeenMessage(message, false);
        }
        System.out.println(message.getShopCode() + " sent to " + message.getUserCode());
        return message;
    }

}
