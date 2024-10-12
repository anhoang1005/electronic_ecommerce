package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "voucher_products")
public class Voucher_Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vp_id")
    private Long id;

    @Column(nullable = false)
    private Long product_id;

    @ManyToOne
    @JoinColumn(name = "voucher_id",referencedColumnName = "voucher_id")
    private Voucher voucher;
}
