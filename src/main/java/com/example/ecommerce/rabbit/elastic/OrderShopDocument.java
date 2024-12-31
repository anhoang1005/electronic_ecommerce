package com.example.ecommerce.rabbit.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopDocument {
    private Long id;
    private String orderShopCode;
    private String orderCode;
    private String status;
    private BigDecimal shopShippingPrice;
    private BigDecimal shopDiscount;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private String deliveryMethod;
    private String paymentMethod;
    private  boolean isPayment;
    private Long shopId;
    private Date updatedAt;
    private Long userId;
    private List<Long> itemIds;
}
