package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "message", indexes = {
        @Index(name = "idx_message_message_story_id", columnList = "message_story_id")
})
public class Message extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isShopSender;

    @Column(nullable = true)
    private Long replyTo;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true, columnDefinition = "MEDIUMTEXT")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void initializeDefaultValues() {
        this.active = true;
        this.isRead = false;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_story_id")
    private MessageChat messageStoryOfMessage;

}
