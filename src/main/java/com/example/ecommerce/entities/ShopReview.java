package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shop_review", indexes = {
        @Index(name = "idx_shop_review_shop_id", columnList = "shop_id"),
        @Index(name = "idx_shop_review_user_id", columnList = "user_id"),
})
public class ShopReview extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Boolean isReply;

    @Column(nullable = true, length = 300, columnDefinition = "TEXT")
    private String reply;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shopOfShopReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersOfShopReview;
}
