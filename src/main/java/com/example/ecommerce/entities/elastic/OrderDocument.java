package com.example.ecommerce.entities.elastic;

import com.example.ecommerce.entities.Orders;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDocument implements Serializable {
    private String orderCode;
    private Orders.Status status;
    private BigDecimal totalAmount;
    private BigDecimal ecommerceServiceFee;
    private LocalDateTime createdAt;
}
