package com.example.ecommerce.controller.message_chat;

import com.example.ecommerce.payload.socket_payload.MessageData;
import com.example.ecommerce.payload.socket_payload.NotificationData;
import com.example.ecommerce.payload.socket_payload.SocketBody;
import com.example.ecommerce.service.MessageChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class PrivateChatController {
    //private final SimpMessagingTemplate messagingTemplate;
    private final MessageChatService messageChatService;
    private final ObjectMapper objectMapper;

    @MessageMapping("/chat.sendShop/{shopCode}")
    @SendTo("/topic/shop/{shopCode}")
    public SocketBody sendMessageToShop(@Payload SocketBody body,
                                           @DestinationVariable String shopCode) {
        if(body.getType().toString().equals("SENT")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            messageChatService.userSendMessage(message, false);
        } else if(body.getType().toString().equals("SEEN")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            messageChatService.userSeenMessage(message, true);
        }
        return body;
    }

    @MessageMapping("/chat.sendCustomer/{customerCode}")
    @SendTo("/topic/customer/{customerCode}")
    public SocketBody sendMessageToCustomer(@Payload SocketBody body,
                                            @DestinationVariable String customerCode) {
        if(body.getType().toString().equals("SENT")) {
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            messageChatService.userSendMessage(message, true);
        } else if(body.getType().toString().equals("SEEN")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            messageChatService.userSeenMessage(message, false);
        }
        return body;
    }

}
