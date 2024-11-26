package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderShipping {
    private Long shippingId; // Ma van chuyen (Lay tu GIAOHANGNHANH)
    private Long orderId;
    private Method method;
    private Long trackingCode; //Mã theo dõi vận chuyển từ bên giao hàng
    private String carrier; //Đơn vị vận chuyển (VNPost, Goship, etc.)
    private LocalDateTime shippingDate; //Ngày vận chuyển
    private LocalDateTime deliveryDate;
    private Double shippingFee;

    public enum Method{
        PENDING,
        SHIPPED,
        DELiVERY,
        RETURNED
    }
}
