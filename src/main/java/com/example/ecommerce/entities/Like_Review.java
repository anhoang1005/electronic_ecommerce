package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "like_reviews")
public class Like_Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lReview_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "review_id",referencedColumnName = "review_id")
    private Review review;
}
