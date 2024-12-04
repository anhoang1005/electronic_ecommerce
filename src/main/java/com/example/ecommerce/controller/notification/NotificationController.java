package com.example.ecommerce.controller.notification;

import com.example.ecommerce.payload.socket_payload.NotificationData;
import com.example.ecommerce.payload.socket_payload.SocketBody;
import com.example.ecommerce.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notifyService;

    @PostMapping("/api/customer/notify/send-user/{user_Code}")
    public ResponseEntity<?> sendNotifyToUser(@PathVariable("user_Code") String userCode,
                                              @RequestBody NotificationData data){
        notifyService.sendNotifyToOneUser(userCode, SocketBody.Type.SYSTEM_NOTIFY, data);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/api/customer/notify/send-all")
    public ResponseEntity<?> send1NotifyToAllUser(@RequestBody NotificationData data){
        notifyService.sendNotifyToAllUser(SocketBody.Type.SYSTEM_NOTIFY, data);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/api/owner/notify/send-follow")
    public ResponseEntity<?> sendNotifyToFollowUser(@RequestBody NotificationData data){
        notifyService.shopSendNotifyToAllUserFollow(data);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/api/customer/notify/get-all")
    public ResponseEntity<?> getNotifyToUser(@RequestParam("page_number") int pageNumber,
                                             @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(notifyService.customerGetTheirNotification(pageNumber, pageSize));
    }

    @GetMapping("/api/customer/notify/get-detail")
    public ResponseEntity<?> getNotifyDetail(@RequestParam("notify_id") Long notifyId){
        return ResponseEntity.ok(notifyService.customerGetNotificationDetail(notifyId));
    }
}
