package com.example.ecommerce.entities;

import com.example.ecommerce.utils.RandomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_shop")
public class OrdersShop extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String orderShopCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status orderStatus;

    @Column(nullable = false)
    private BigDecimal totalProduct;

    @Column(nullable = false)
    private BigDecimal shopShippingFee;

    @Column(nullable = false)
    private BigDecimal shopDiscount;

    @Column(nullable = false)
    private BigDecimal serviceFee;

    @Column(nullable = false)
    private BigDecimal shopTotalAmount;

    @Column(nullable = false)
    private BigDecimal ecommerceTotalAmount;

    @Column(nullable = false)
    private String deliveryMethod;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private Boolean isPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shopOfOrdersShop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_orders_id")
    private Orders ordersOfOrdersShop;

    @OneToOne(mappedBy = "ordersShopOfShipping", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderShipping orderShippingOfShopOrder;

    @OneToMany(mappedBy = "ordersShopOfOrdersItems", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersItems> listOrdersItemsOfOrdersShop = new ArrayList<>();

    @OneToMany(mappedBy = "ordersShopPOfOrderHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersHistory> listOrderHistoryOfOrderShop = new ArrayList<>();

    public enum Status{
        CHO_XAC_NHAN,
        DANG_LAY_HANG,
        DANG_GIAO_HANG,
        GH_THANH_CONG,
        DA_HUY,
        TRA_HANG
    }

    @PostPersist
    public void update(){
        this.orderShopCode = RandomUtils.generateOrderCode("SOR", this.id);
    }
}
