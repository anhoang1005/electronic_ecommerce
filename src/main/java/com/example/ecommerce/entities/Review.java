package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean is_reply;

    private Long id_user_reply;

    private boolean is_hidden;

    private Long count_like;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private UsersEntity user;

    @PrePersist
    public void onCreate(){
        this.count_like = Long.valueOf(0);
        this.is_hidden = false;
    }
}
