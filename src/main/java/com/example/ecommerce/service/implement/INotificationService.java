package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Notification;
import com.example.ecommerce.entities.NotificationUsers;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.notification.NotificationMapper;
import com.example.ecommerce.models.notification.NotificationDto;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.socket_payload.NotificationData;
import com.example.ecommerce.payload.socket_payload.SocketBody;
import com.example.ecommerce.repository.NotificationRepository;
import com.example.ecommerce.repository.NotificationUsersRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.NotificationService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class INotificationService implements NotificationService {
    private final AuthenticationUtils authUtils;
    private final NotificationRepository notifyRepository;
    private final UsersRepository usersRepository;
    private final NotificationUsersRepository notifyUserRepository;
    private final ShopRepository shopRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final DateTimeMapperUtils dateUtils;
    private final NotificationMapper notifyMapper;

    @Async
    @Override
    @Transactional
    public void sendNotifyToOneUser(String userCode, SocketBody.Type type, NotificationData data) {
        try{
            String senderUserCode = authUtils.getUserFromAuthentication().getUserCode();
            System.out.println(userCode + " " + senderUserCode);
            Users users = usersRepository.findUsersByUserCode(userCode);
            Notification notify = new Notification();
            notify.setType(type);
            notify.setImage(null);
            notify.setSenderCode(senderUserCode);
            if(type==SocketBody.Type.SHOP_NOTIFY){
                Shop shop = shopRepository.findShopByShopCode(authUtils.getUserFromAuthentication().getShopCode());
                notify.setSenderName("shop: " + shop.getShopName());
            } else{
                notify.setSenderName(type.toString());
            }
            notify.setTitle(data.getTitle());
            notify.setDetail(data.getDetail());
            notify = notifyRepository.save(notify);

            NotificationUsers notifyUser = new NotificationUsers();
            notifyUser.setNotifyOfNotifyUser(notify);
            notifyUser.setUsersOfNotifyUsers(users);
            notifyUser.setRead(false);
            notifyUserRepository.save(notifyUser);
            SocketBody socketBody = buildSocketBody(notify, type);
            messagingTemplate.convertAndSend("topic/customer/" + userCode, socketBody);
        } catch (Exception e){
            log.error("Send notify error");
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Async
    @Override
    @Transactional
    public void sendNotifyToAllUser(SocketBody.Type type, NotificationData data) {
        try{
            String senderUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Notification notify = new Notification();
            notify.setSenderCode(senderUserCode);
            notify.setSenderName("SYSTEM_NOTIFY");
            notify.setImage(null);
            notify.setType(SocketBody.Type.SYSTEM_NOTIFY);
            notify.setTitle(data.getTitle());
            notify.setDetail(data.getDetail());
            notify.setNotifyUserOfNotification(null);
            notify = notifyRepository.save(notify);
            NotificationUsers notifyUser = new NotificationUsers();
            notifyUser.setUsersOfNotifyUsers(null);
            notifyUser.setNotifyOfNotifyUser(notify);
            notifyUserRepository.save(notifyUser);
            SocketBody socketBody = buildSocketBody(notify, SocketBody.Type.SYSTEM_NOTIFY);
            messagingTemplate.convertAndSend("topic/system", socketBody);
        } catch (Exception e){
            log.error("Send notify error! {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Async
    @Override
    @Transactional
    public void shopSendNotifyToAllUserFollow(NotificationData data) {
        try{
            Shop shop = shopRepository.findShopByShopCode(authUtils.getUserFromAuthentication().getShopCode());
            List<Users> listUsers = shop.getListUsersFollowShop();
            Notification notify = new Notification();
            notify.setSenderCode(authUtils.getUserFromAuthentication().getShopCode());
            notify.setSenderName("shop: " + shop.getShopName());
            notify.setImage(null);
            notify.setType(SocketBody.Type.SYSTEM_NOTIFY);
            notify.setTitle(data.getTitle());
            notify.setDetail(data.getDetail());
            notify = notifyRepository.save(notify);
            SocketBody socketBody = buildSocketBody(notify, SocketBody.Type.SHOP_NOTIFY);
            List<NotificationUsers> listNotifyUsers = new ArrayList<>();
            for(Users users : listUsers){
                NotificationUsers notifyUser = new NotificationUsers();
                notifyUser.setNotifyOfNotifyUser(notify);
                notifyUser.setUsersOfNotifyUsers(users);
                notifyUser.setRead(false);
                messagingTemplate.convertAndSend("/topic/customer/" + users.getUserCode(), socketBody);
                listNotifyUsers.add(notifyUser);
            }
            notify.setNotifyUserOfNotification(listNotifyUsers);
            notifyRepository.save(notify);
        } catch (Exception e){
            log.error("Shop send notify error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerGetTheirNotification(int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by(Sort.Order.desc("id")));
            Page<NotificationUsers> page = notifyUserRepository.customerGetTheirNotification(userId, pageable);
            List<NotificationDto> listNotifyDto = page.stream()
                    .map(notifyMapper::entityToNotifyDto)
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listNotifyDto)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            Page<NotificationUsers> latestNotify = notifyUserRepository.customerGetTheirNotification(userId,
                    PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id"))));
            if(latestNotify.hasContent()){
                usersRepository.findAndUpdateReadNotify(
                        authUtils.getUserFromAuthentication().getUserCode(),
                        latestNotify.getContent().get(0).getNotifyOfNotifyUser().getId());
            }
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get list notify error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerGetNotificationDetail(Long notifyId) {
        try {
            Notification notification = notifyRepository.findById(notifyId)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            return new ResponseBody(notifyMapper.entityToNotifyDto1(notification),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get notification error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    private SocketBody buildSocketBody(Notification notify, SocketBody.Type type) {
        return SocketBody.builder()
                .type(type)
                .senderCode(notify.getSenderCode())
                .createdAt(dateUtils.localDateTimeToString(notify.getCreatedAt()))
                .body(NotificationData.builder()
                        .notifyId(notify.getId())
                        .senderName(notify.getSenderName())
                        .image(notify.getImage())
                        .title(notify.getTitle())
                        .detail(notify.getDetail())
                        .build())
                .build();
    }


}
