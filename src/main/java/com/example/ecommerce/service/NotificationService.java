package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.socket_payload.NotificationData;

public interface NotificationService {
    ResponseBody sendNotifyToOneUser(String userCode, String role, NotificationData data);
    ResponseBody sendNotifyToManyUser();
    ResponseBody sendNotifyToAllUser();

    ResponseBody sendSystemNotifyToAllUser(NotificationData data);
    ResponseBody sendSystemNotifyToAllShopOwner(NotificationData data);
    ResponseBody sendSystemNotifyToAllAdmin(NotificationData data);

    ResponseBody shopSendNotifyToOneCustomer();
    ResponseBody shopSendNotifyToAllCustomer();

}
