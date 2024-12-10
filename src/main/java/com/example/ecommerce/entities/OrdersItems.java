package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_items")
public class OrdersItems extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long variantId;

    @Column(nullable = false)
    private String productTitle;

    @Column(nullable = false)
    private String variantName;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_shop_id")
    private OrdersShop ordersShopOfOrdersItems;


}
