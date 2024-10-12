package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private  Double total_amount;

    @Column(nullable = false)
    private String order_code;

    @Column(nullable = false)
    private String order_status;

    private String note;
}
