package com.example.ecommerce.mapper.message_chat;

import com.example.ecommerce.entities.Message;
import com.example.ecommerce.entities.MessageChat;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.models.message_chat.MessageChatDto;
import com.example.ecommerce.models.message_chat.MessageDto;
import com.example.ecommerce.repository.MessageRepository;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageChatMapper {
    private final DateTimeMapperUtils dateUtils;
    private final MessageRepository messageRepository;

    public MessageChatDto entityToShopMessageChatDto(MessageChat chat){
        Users users = chat.getUsersOfMessageStory();
        return MessageChatDto.builder()
                .chatId(chat.getId())
                .storyAvatar(users.getImageUrl())
                .storyName(users.getUsername())
                .shopCode(chat.getShopOfMessageStory().getShopCode())
                .userCode(users.getUserCode())
                .sendLastAt((chat.getSendLastAt()!=null) ?
                                dateUtils.localDateTimeToHouseString(chat.getSendLastAt()) : null)
                .lastMessage(chat.getLatestMessage())
                .unreadMessageCount(chat.getShopUnReadCount())
                .active(chat.getActive())
                .build();
    }

    public MessageChatDto entityToCustomerMessageChatDto(MessageChat chat){
        Shop shop = chat.getShopOfMessageStory();
        return MessageChatDto.builder()
                .chatId(chat.getId())
                .storyAvatar(shop.getShopLogo())
                .storyName(shop.getShopName())
                .shopCode(shop.getShopCode())
                .userCode(chat.getUsersOfMessageStory().getUserCode())
                .sendLastAt((chat.getSendLastAt()!=null) ?
                        dateUtils.localDateTimeToHouseString(chat.getSendLastAt()) : null)
                .lastMessage(chat.getLatestMessage())
                .unreadMessageCount(chat.getCustomerUnreadCount())
                .active(chat.getActive())
                .build();
    }

    public MessageDto entityToMessageDto(Message message){
        String replyMessage = null;
        if(message.getReplyTo()!=null && message.getReplyTo()!=0){
            Message reply = messageRepository.findMessageById(message.getReplyTo());
            replyMessage = reply.getContent();
        }
        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .imageUrl(message.getImageUrl())
                .isShopSender(message.getIsShopSender())
                .replyTo(message.getReplyTo()!=null ? message.getReplyTo() : null)
                .replyMessage(replyMessage)
                .createdAt(dateUtils.localDateTimeToHouseString(message.getCreatedAt()))
                .isRead(message.getIsRead())
                .build();
    }
}
