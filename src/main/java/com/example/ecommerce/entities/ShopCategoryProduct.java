package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shop_category_product")
public class ShopCategoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopCategory_id")
    private ShopCategory shopCategory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

//    @ManyToOne
//    @JoinColumn(name = "shop_id")
//    private Shop shop;

    @Column(name = "is_active")
    private boolean isActive;

    @PrePersist
    public void onCreate(){
        this.isActive = true;
    }
}
