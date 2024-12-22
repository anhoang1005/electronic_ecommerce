package com.example.ecommerce.entities.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {
    private String id;
    private String orderCode;
    private String status;
    private BigDecimal totalFee;
    private BigDecimal totalShipping;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
    private BigDecimal ecommerceServiceFee;
    private String deliveryMethod;
    private String paymentMethod;
    private Boolean isPayment;
    private Long userId;
}
