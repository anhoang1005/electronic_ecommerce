package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String category_name;

    @Column(nullable = false)
    private String category_level;

    private Long category_parent_id;

    private boolean is_available;

    @Column(nullable = false)
    private String category_type;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private UsersEntity user;
}
