package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrdersShop;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderShopDetail {
    private Long id;
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
    private String createdAt;
    private OrderShippingDto shippingDto;
    private List<OrderItemDto> itemDtoList;
    private List<OrderHistoryDto> historyDtoList;
}
