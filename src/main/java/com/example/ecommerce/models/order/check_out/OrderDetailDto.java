package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.entities.Orders;
import com.example.ecommerce.models.order.OrderPaymentDto;
import com.example.ecommerce.models.order.OrderShopDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
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
    private String createdAt;
    private List<OrderPaymentDto> listOrderPayment;
    private List<OrderShopDto> listOrderShop;
}
