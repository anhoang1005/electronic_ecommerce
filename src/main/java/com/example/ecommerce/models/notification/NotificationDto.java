package com.example.ecommerce.models.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    private Long notifyId;
    private String senderName;
    private String createdAt;
    private String title;
    private String image;
    private String detail;
    private boolean isRead;
    private String type;
}
