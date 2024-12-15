package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrderPayment;
import com.example.ecommerce.entities.Orders;
import com.example.ecommerce.entities.OrdersShop;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String orderCode;
    private Orders.Status status;
    private BigDecimal totalFee;
    private BigDecimal totalShipping;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
    private BigDecimal ecommerceServiceFee;
    private String deliveryMethod;
    private String clientInfo;
    private String paymentMethod;
    private Boolean isPayment;
}
