package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "message_chat")
public class MessageChat extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 200)
    private String storyName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users usersOfMessageStory;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shopOfMessageStory;

    @OneToMany(mappedBy = "messageStoryOfMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> listMessageOfMessageStory;

}
