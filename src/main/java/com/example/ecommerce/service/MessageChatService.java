package com.example.ecommerce.service;

import com.example.ecommerce.models.message_chat.MessageChatFilter;
import com.example.ecommerce.models.message_chat.SocketMessage;
import com.example.ecommerce.payload.ResponseBody;

public interface MessageChatService {
    ResponseBody shopOwnerGetAllMessageChat(MessageChatFilter filter);
    ResponseBody customerGetAllMessageChat(MessageChatFilter filter);
    ResponseBody usersCreateShopMessageChat(String shopCode, String userCode, Boolean isShop);

    ResponseBody userGetMessageOfChat(Long chatId, boolean isShop, int pageNumber, int pageSize);
    void userSendMessage(SocketMessage req, Boolean isShopSender);
    void userSeenMessage(SocketMessage req, Boolean isShop);
    ResponseBody userRemoveMessage(Long messageId);
}
