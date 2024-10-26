package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Message;
import com.example.ecommerce.entities.MessageChat;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.message_chat.MessageChatMapper;
import com.example.ecommerce.models.message_chat.*;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.MessageChatRepository;
import com.example.ecommerce.repository.MessageRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.MessageChatService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IMessageChatService implements MessageChatService {
    private final AuthenticationUtils authUtils;
    private final ShopRepository shopRepository;
    private final UsersRepository usersRepository;
    private final MessageChatRepository messageChatRepository;
    private final MessageChatMapper messageChatMapper;
    private final MessageRepository messageRepository;

    @Override
    public ResponseBody shopOwnerGetAllMessageChat(MessageChatFilter filter) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            Shop shop = users.getShopOfUsers();
            if (shop == null) {
                throw new RequestNotFoundException("ERROR");
            }
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("modifiedAt")));
            Page<MessageChat> page = messageChatRepository.getListMessageChatOfShop(
                    shop.getShopCode(), filter.getUserCode(), pageable);
            List<MessageChatDto> listChat = page.stream()
                    .map(messageChatMapper::entityToShopMessageChatDto)
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageSize())
                    .data(listChat)
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get list chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerGetAllMessageChat(MessageChatFilter filter) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            if (users == null) {
                throw new RequestNotFoundException("ERROR");
            }
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("modifiedAt")));
            Page<MessageChat> page = messageChatRepository.getListMessageChatOfShop(
                    filter.getShopCode(), users.getUserCode(), pageable);
            List<MessageChatDto> listChat = page.stream()
                    .map(messageChatMapper::entityToCustomerMessageChatDto)
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageNumber())
                    .data(listChat)
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get list chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody usersCreateShopMessageChat(String shopCode, String userCode, Boolean isShop) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            MessageChat chat = new MessageChat();
            Users users;
            Shop shop;
            if (isShop) {
                users = usersRepository.findUsersByUserCode(userCode);
                shop = usersRepository.findUsersEntitiesByEmail(email).getShopOfUsers();
                if (messageChatRepository.getMessageChatByShopCodeAndUserCode(userCode, shop.getShopCode()).isPresent()) {
                    return new ResponseBody("CHAT_EXISTED", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                }
            } else {
                users = usersRepository.findUsersEntitiesByEmail(email);
                shop = shopRepository.findShopByShopCode(shopCode);
                if (messageChatRepository.getMessageChatByShopCodeAndUserCode(users.getUserCode(), shop.getShopCode()).isPresent()) {
                    return new ResponseBody("CHAT_EXISTED", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                }
            }
            chat.setUsersOfMessageStory(users);
            chat.setShopOfMessageStory(shop);
            chat = messageChatRepository.save(chat);
            if (isShop) {
                return new ResponseBody(messageChatMapper.entityToShopMessageChatDto(chat), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                return new ResponseBody(messageChatMapper.entityToCustomerMessageChatDto(chat), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.error("Create chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody userGetMessageOfChat(Long chatId, boolean isShop, int pageNumber, int pageSize) {
        try {
            MessageChat chat = messageChatRepository.findById(chatId)
                    .orElseThrow(() -> new RequestNotFoundException("CHAT_NOT_FOUND"));
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            Page<Message> page = messageRepository.getListMessageByChatId(chatId, pageable);
            List<MessageDto> listMessage = page.stream()
                    .map(messageChatMapper::entityToMessageDto)
                    .collect(Collectors.toList());
            Collections.reverse(listMessage);
            MessageStoryDto story = new MessageStoryDto();
            Shop shop = chat.getShopOfMessageStory();
            Users users = chat.getUsersOfMessageStory();
            story.setChatId(chat.getId());
            story.setUserCode(users.getUserCode());
            story.setShopCode(shop.getShopCode());
            story.setActive(chat.getActive());
            story.setListMessage(listMessage);
            if(isShop){
                story.setStoryName(users.getUsername());
                story.setStoryAvatar(users.getImageUrl());
                story.setUnreadMessageCount(chat.getShopUnReadCount());
            } else{
                story.setStoryName(shop.getShopName());
                story.setStoryAvatar(shop.getShopLogo());
                story.setUnreadMessageCount(chat.getCustomerUnreadCount());
            }
            return new ResponseBody(story, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get message of chat error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public void userSendMessage(SocketMessage req, Boolean isShopSender) {
        try {
            MessageChat messageChat = messageChatRepository.findById(req.getChatId())
                    .orElseThrow(() -> new RequestNotFoundException("CHAT_NOT_FOUND"));
            Message message = new Message();
            message.setMessageStoryOfMessage(messageChat);
            message.setContent(req.getContent());
            message.setImageUrl(req.getImageUrl());
            message.setIsRead(false);
            message.setReplyTo(0L);
            message.setIsShopSender(isShopSender);
            message = messageRepository.save(message);
            messageChat.setSendLastAt(message.getCreatedAt());
            if(isShopSender){
                if(message.getImageUrl()!=null){
                    messageChat.setLatestMessage("[Hình ảnh]");
                } else{
                    messageChat.setLatestMessage(message.getContent());
                }
                messageChat.setShopUnReadCount(0);
                messageChat.setCustomerUnreadCount(messageChat.getCustomerUnreadCount() + 1);
            } else{
                if(message.getImageUrl()!=null){
                    messageChat.setLatestMessage("[Hình ảnh]");
                } else{
                    messageChat.setLatestMessage(message.getContent());
                }
                messageChat.setCustomerUnreadCount(0);
                messageChat.setShopUnReadCount(messageChat.getShopUnReadCount() + 1);
            }
            messageChatRepository.save(messageChat);
        } catch (Exception e) {
            log.error("Send message error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to send message");
        }
    }

    @Override
    public void userSeenMessage(SocketMessage req, Boolean isShop) {
        try{
            MessageChat messageChat = messageChatRepository.findById(req.getChatId())
                    .orElseThrow(() -> new RequestNotFoundException("CHAT_NOT_FOUND"));
            if(isShop){
                messageChat.setShopUnReadCount(0);
            } else{
                messageChat.setCustomerUnreadCount(0);
            }
            messageChatRepository.save(messageChat);
        } catch (Exception e){
            log.error("Send message error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to send message");
        }
    }


    @Override
    @Transactional
    public ResponseBody userRemoveMessage(Long messageId) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RequestNotFoundException("MESSAGE_NOT_FOUND"));
            if(message.getCreatedBy().equals(email)){
                throw new UnauthorizedException("UNAUTHORIZED");
            }
            message.setActive(false);
            message = messageRepository.save(message);
            return new ResponseBody(messageChatMapper.entityToMessageDto(message),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Remove message of chat error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
