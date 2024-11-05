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

    @PostMapping("/api/customer/notify/send-user/{userCode}")
    public ResponseEntity<?> sendNotifyToUser(@PathVariable("userCode") String userCode,
                                              @RequestBody NotificationData data){
        return ResponseEntity.ok(notifyService.sendNotifyToOneUser(userCode,
                SocketBody.Type.SYSTEM_NOTIFY, data));
    }

    @PostMapping("/api/customer/notify/send-all")
    public ResponseEntity<?> send1NotifyToUser(@RequestBody NotificationData data){
        return ResponseEntity.ok(notifyService.sendNotifyToAllUser(SocketBody.Type.SYSTEM_NOTIFY, data));
    }

    @PostMapping("/api/customer/notify/get-all")
    public ResponseEntity<?> getNotifyToUser(@RequestParam("page_number") int pageNumber,
                                             @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(notifyService.customerGetTheirNotification(pageNumber, pageSize));
    }
}
