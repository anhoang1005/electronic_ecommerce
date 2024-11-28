package com.example.ecommerce.entities;

import com.example.ecommerce.payload.socket_payload.SocketBody;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String senderCode;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String image;

    @Column(nullable = false)
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocketBody.Type type;

    @OneToMany(mappedBy = "notifyOfNotifyUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationUsers> notifyUserOfNotification;

}
