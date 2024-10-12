package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders_shop")
public class OrdersShop extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderShopCode;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private String totalAmount;

    @Column(nullable = false)
    private String deliveryMethod;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String isPayment;

    @Column(nullable = false)
    private String clientAddress;

    @Column(nullable = false)
    private String clientTelephone;

    @Column(nullable = false)
    private String clientName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shopOfOrdersShop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_orders_id")
    private Orders ordersOfOrdersShop;

    @OneToMany(mappedBy = "ordersShopOfOrdersItems", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersItems> listOrdersItemsOfOrdersShop;

    @OneToMany(mappedBy = "ordersShopPOfOrderHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersHistory> listOrderHistoryOfOrderShop;

}
