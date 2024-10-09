package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "product_review")
public class ProductReview extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String textComment;

    @Column(columnDefinition = "TEXT")
    private String imageComment;

    @Column(nullable = true)
    private String tag;

    @Column(nullable = false)
    private String isShopReply;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String shopReply;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private String active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product productOfProductReview;

    @ManyToMany(mappedBy = "listProductReviewUserLike", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Users> listUsersLikeReview;

}
