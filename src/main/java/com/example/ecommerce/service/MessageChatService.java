package com.example.ecommerce.service;

import com.example.ecommerce.models.message_chat.MessageChatFilter;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.socket_payload.MessageData;

public interface MessageChatService {
    ResponseBody shopOwnerGetAllMessageChat(MessageChatFilter filter);
    ResponseBody customerGetAllMessageChat(MessageChatFilter filter);
    ResponseBody usersCreateShopMessageChat(String shopCode, String userCode, Boolean isShop);

    ResponseBody userGetMessageOfChat(Long chatId, boolean isShop, int pageNumber, int pageSize);
    void userSendMessage(MessageData req, Boolean isShopSender);
    void userSeenMessage(MessageData req, Boolean isShop);
    ResponseBody userRemoveMessage(Long messageId);
}
