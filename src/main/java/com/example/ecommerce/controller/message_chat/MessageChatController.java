package com.example.ecommerce.controller.message_chat;

import com.example.ecommerce.models.message_chat.MessageChatFilter;
import com.example.ecommerce.service.MessageChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageChatController {
    @Autowired
    private MessageChatService messageChatService;

    @PostMapping("/api/customer/message-chat")
    public ResponseEntity<?> customerGetAllChatApi(
            @RequestBody MessageChatFilter filter){
        return ResponseEntity.ok(messageChatService.customerGetAllMessageChat(filter));
    }

    @PostMapping("/api/owner/message-chat")
    public ResponseEntity<?> shopGetAllChatApi(
            @RequestBody MessageChatFilter filter){
        return ResponseEntity.ok(messageChatService.shopOwnerGetAllMessageChat(filter));
    }

    @PostMapping("/api/owner/get-chat")
    public ResponseEntity<?> shopGetChatApi(
            @RequestParam("chat_id") Long chatId,
            @RequestParam("page_number") Integer pageNumber,
            @RequestParam("page_size") Integer pageSize){
        return ResponseEntity.ok(
                messageChatService.userGetMessageOfChat(chatId, true, pageNumber, pageSize));
    }

    @PostMapping("/api/customer/get-chat")
    public ResponseEntity<?> customerGetChatApi(
            @RequestParam("chat_id") Long chatId,
            @RequestParam("page_number") Integer pageNumber,
            @RequestParam("page_size") Integer pageSize){
        return ResponseEntity.ok(
                messageChatService.userGetMessageOfChat(chatId, false, pageNumber, pageSize));
    }

    @PostMapping("/api/owner/create-chat")
    public ResponseEntity<?> shopCreateChatApi(
            @RequestParam("user_code") String userCode,
            @RequestParam("shop_code") String shopCode){
        return ResponseEntity.ok(messageChatService.usersCreateShopMessageChat(shopCode, userCode, true));
    }

    @PostMapping("/api/customer/create-chat")
    public ResponseEntity<?> customerCreateChatApi(
            @RequestParam("user_code") String userCode,
            @RequestParam("shop_code") String shopCode){
        return ResponseEntity.ok(messageChatService.usersCreateShopMessageChat(shopCode, userCode, false));
    }
}
