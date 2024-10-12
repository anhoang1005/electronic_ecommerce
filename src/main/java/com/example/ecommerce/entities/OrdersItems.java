package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders_items")
public class OrdersItems extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String productTitle;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_shop_id")
    private OrdersShop ordersShopOfOrdersItems;
}
