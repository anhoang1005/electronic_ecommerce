package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shop_category_product")
public class ShopCategoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_category_id")
    @JsonBackReference("shop-category-shop-category-product")
    private ShopCategory shopCategory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-shop-category-product")
    private Product product;

    @Column(name = "is_active")
    private boolean isActive;

    @PrePersist
    public void onCreate(){
        this.isActive = true;
    }
}
