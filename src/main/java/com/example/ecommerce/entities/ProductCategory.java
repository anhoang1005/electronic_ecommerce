package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double sellPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer soldQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryOfProductCategory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productOfProductCategory;
}
