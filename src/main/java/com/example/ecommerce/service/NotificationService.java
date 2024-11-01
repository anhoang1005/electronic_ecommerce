package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.socket_payload.NotificationData;
import com.example.ecommerce.payload.socket_payload.SocketBody;

public interface NotificationService {
    ResponseBody sendNotifyToOneUser(String userCode, SocketBody.Type type, NotificationData data);
    ResponseBody sendNotifyToAllUser(SocketBody.Type type, NotificationData data);
    ResponseBody shopSendNotifyToAllUserFollow(NotificationData data);

    ResponseBody customerGetTheirNotification(int pageNumber, int pageSize);
    ResponseBody customerGetNotificationDetail(Long notifyId);
}
