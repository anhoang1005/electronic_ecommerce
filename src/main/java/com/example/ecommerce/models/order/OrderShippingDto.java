package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrderShipping;
import com.example.ecommerce.entities.OrdersShop;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderShippingDto {
    private Long id;
    private String clientAddress;
    private String clientTelephone;
    private String clientName;
    private Long shippingId; // Ma van chuyen (Lay tu GIAOHANGNHANH)
    private OrderShipping.Method method;
    private OrderShipping.Status status;
    private Long trackingCode; //Mã theo dõi vận chuyển từ bên giao hàng
    private String carrier; //Đơn vị vận chuyển (VNPost, Goship, etc.)
    private LocalDateTime shippingDate; //Ngày vận chuyển
    private LocalDateTime deliveryDate;
    private BigDecimal shippingFee;
}
