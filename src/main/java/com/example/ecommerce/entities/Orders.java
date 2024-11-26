package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_order_code", columnList = "order_code"),
        @Index(name = "idx_orders_user_id", columnList = "user_id")
})
public class Orders extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Double totalAmount;

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
    private Boolean isPayment;

    @OneToMany(mappedBy = "ordersOfOrdersShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersShop> listOrderShopOfOrders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersOfOrder;

    public enum Status{
        DANG_XU_LY,
        HOAN_THANH,
        DA_HUY
    }
}
