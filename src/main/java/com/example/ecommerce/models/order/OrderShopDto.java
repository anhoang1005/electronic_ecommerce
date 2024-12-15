package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderShopDto {
    private Long id;
    private String shopName;
    private String shopLogo;
    private String shopCode;
    private String orderShopCode;
    private OrdersShop.Status orderStatus;
    private BigDecimal totalProduct;
    private BigDecimal shopShippingFee;
    private BigDecimal shopDiscount;
    private BigDecimal serviceFee;
    private BigDecimal shopTotalAmount;
    private BigDecimal ecommerceTotalAmount;
    private String deliveryMethod;
    private String paymentMethod;
    private Boolean isPayment;
    private String clientInfo;
}
