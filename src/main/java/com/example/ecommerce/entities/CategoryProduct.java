package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "category_product")
public class CategoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "is_active")
    private boolean isActive;

    @PrePersist
    public void onCreate(){
        this.isActive = true;
    }
}
