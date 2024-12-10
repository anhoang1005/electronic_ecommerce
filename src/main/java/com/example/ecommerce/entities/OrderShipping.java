package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.Na;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_shipping")
public class OrderShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_shop_id", referencedColumnName = "id")
    private OrdersShop ordersShopOfShipping;

    @Column(nullable = false)
    private String clientAddress;

    @Column(nullable = false)
    private String clientTelephone;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = true)
    private Long shippingId; // Ma van chuyen (Lay tu GIAOHANGNHANH)

    @Column(nullable = false)
    private Method method;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = true)
    private Long trackingCode; //Mã theo dõi vận chuyển từ bên giao hàng

    @Column
    private String carrier; //Đơn vị vận chuyển (VNPost, Goship, etc.)

    @Column
    private LocalDateTime shippingDate; //Ngày vận chuyển

    @Column
    private LocalDateTime deliveryDate;

    @Column(nullable = false)
    private BigDecimal shippingFee;

    public enum Method{
        GHN,
    }

    public enum Status{
        PENDING,
        SHIPPED,
        DELiVERY,
        RETURNED
    }
}
