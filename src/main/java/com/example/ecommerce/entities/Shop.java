package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shops")
@Data
public class Shop extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id;

    @Column(nullable = false)
    private String shop_name;

    private String description;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Long count_follows;

    @Column(nullable = false)
    private Long order_waiting;

    @Column(nullable = false)
    private Long order_cancel;

    private String shop_logo;

    @Column(nullable = false,unique = true,updatable = false)
    private String shop_code;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String province_id;

    @ManyToMany
    @JoinTable(name = "shops_users",joinColumns = @JoinColumn(name = "shop_id",referencedColumnName = "shop_id")
    ,inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id"))
    private List<UsersEntity> users = new ArrayList<>();
    @PrePersist
    public void onCreate(){
        this.rating = Double.valueOf(0);
        this.count_follows = Long.valueOf(0);
        this.order_cancel = Long.valueOf(0);
        this.order_waiting = Long.valueOf(0);
    }
}
