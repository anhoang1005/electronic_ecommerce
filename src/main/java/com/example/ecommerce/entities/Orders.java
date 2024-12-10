package com.example.ecommerce.entities;

import com.example.ecommerce.utils.RandomUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(nullable = true, unique = true)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private BigDecimal totalFee;

    @Column(nullable = false)
    private BigDecimal totalShipping;

    @Column(nullable = false)
    private BigDecimal totalDiscount;

    @Column(nullable = false)
    private BigDecimal totalAmount;

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

    @OneToMany(mappedBy = "orderOfOrderPayment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderPayment> listOrderPayment = new ArrayList<>();

    @OneToMany(mappedBy = "ordersOfOrdersShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersShop> listOrderShopOfOrders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersOfOrder;

    public enum Status{
        CHO_THANH_TOAN,
        DANG_XU_LY,
        HOAN_THANH,
        DA_HUY
    }

    @PostPersist
    public void onCreate(){
        this.orderCode = RandomUtils.generateOrderCode("EOR", this.id);
    }
}
