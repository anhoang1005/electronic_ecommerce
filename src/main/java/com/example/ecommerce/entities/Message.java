package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "shop_id",referencedColumnName = "shop_id")
    private Shop shop;


}
