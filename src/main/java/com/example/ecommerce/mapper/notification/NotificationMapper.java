package com.example.ecommerce.mapper.notification;

import com.example.ecommerce.entities.Notification;
import com.example.ecommerce.entities.NotificationUsers;
import com.example.ecommerce.models.notification.NotificationDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationMapper {
    private final DateTimeMapperUtils dateUtils;

    public NotificationDto entityToNotifyDto(NotificationUsers nu){
        Notification notify = nu.getNotifyOfNotifyUser();
        return NotificationDto.builder()
                .notifyId(notify.getId())
                .senderName(notify.getSenderName())
                .createdAt(dateUtils.localDateTimeToString(notify.getCreatedAt()))
                .image(notify.getImage()!=null ? notify.getImage() : null)
                .title(notify.getTitle())
                .detail(notify.getDetail())
                .type(notify.getType().toString())
                .isRead(nu.isRead())
                .build();
    }

    public NotificationDto entityToNotifyDto1(Notification notify){
        return NotificationDto.builder()
                .notifyId(notify.getId())
                .senderName(notify.getSenderName())
                .createdAt(dateUtils.localDateTimeToString(notify.getCreatedAt()))
                .image(notify.getImage()!=null ? notify.getImage() : null)
                .title(notify.getTitle())
                .detail(notify.getDetail())
                .type(notify.getType().toString())
                .isRead(true)
                .build();
    }
}
