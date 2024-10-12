package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Orders extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orderCode;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private String totalAmount;

    @Column(nullable = false)
    private String deliveryMethod;

    @Column(nullable = false)
    private String clientAddress;

    @Column(nullable = false)
    private String clientTelephone;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String isPayment;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "ordersOfOrdersShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersShop> listOrderShopOfOrders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersOfOrder;
}
