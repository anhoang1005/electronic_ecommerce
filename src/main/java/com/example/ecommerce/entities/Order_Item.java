package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "oder_items")
public class Order_Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oItem_id")
    private Long id;

    @Column(nullable = false)
    private String product_code;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "oDetail_id", referencedColumnName = "oDetail_id")
    private Order_Detail orderDetail;
}
