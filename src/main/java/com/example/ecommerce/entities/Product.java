package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private Double base_price;

    @Column(nullable = false)
    private Double sell_price;

    @Column(nullable = false)
    private String product_code;

    private Long quantity;

    private String note;

    private Double rating;

    private Long sold_quantity;

    private Long count_review;

    private boolean is_available;

    private boolean publish_at;

    @PrePersist
    public void onCreate(){
        this.quantity = Long.valueOf(0);
        this.rating = Double.valueOf(0);
        this.sold_quantity = Long.valueOf(0);
        this.count_review = Long.valueOf(0);
        this.is_available = false;
    }
}
