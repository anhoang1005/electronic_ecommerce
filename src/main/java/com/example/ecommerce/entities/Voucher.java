package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long id;

    @Column(nullable = false)
    private String voucher_name;

    @Column(nullable = false,unique = true)
    private String voucher_code;

    @Column(nullable = false)
    private Double min_total_order;

    @Column(nullable = false)
    private Long max_uses;

    @Column(nullable = false)
    private Long user_max_use;

    private String description;

    @Column(nullable = false)
    private String voucher_type_value;

    @Column(nullable = false)
    private String voucher_value;

    @Column(nullable = false)
    private LocalDateTime expired_at;

    private boolean is_publish;
    private boolean is_available;

    @PrePersist
    public  void onCreate(){
        this.is_available = false;
        this.is_publish = false;
    }
}
