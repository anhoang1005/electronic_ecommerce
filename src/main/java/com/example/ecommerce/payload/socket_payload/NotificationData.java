package com.example.ecommerce.payload.socket_payload;

import com.example.ecommerce.entities.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationData {
    private Long notifyId;
    private String senderCode;
    private String senderName;
    private String image;
    private String title;
    private String detail;
}
