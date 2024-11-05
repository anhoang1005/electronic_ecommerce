package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification_users", indexes = {
        @Index(name = "idx_notification_user_user_id", columnList = "user_id")
})
public class NotificationUsers extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_id", nullable = false)
    private Notification notifyOfNotifyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private Users usersOfNotifyUsers;

    @Column(nullable = false)
    private boolean isRead;
}
