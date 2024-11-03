package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "message_chat", indexes = {
        @Index(name = "idx_message_chat_user_id", columnList = "user_id"),
        @Index(name = "idx_message_chat_shop_id", columnList = "shop_id"),
})
public class MessageChat extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String latestMessage;

    @Column(nullable = true)
    private LocalDateTime sendLastAt;

    @Column(nullable = false)
    private Integer shopUnReadCount;

    @Column(nullable = false)
    private Integer customerUnreadCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users usersOfMessageStory;

    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void initializeDefaultValues() {
        this.shopUnReadCount = 0;
        this.customerUnreadCount = 0;
        this.sendLastAt = null;
        this.latestMessage = null;
        this.active = true;
    }

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shopOfMessageStory;

    @OneToMany(mappedBy = "messageStoryOfMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> listMessageOfMessageStory;

}
