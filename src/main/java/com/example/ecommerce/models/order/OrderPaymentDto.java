package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrderPayment;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentDto {
    private Long id;
    private OrderPayment.Method method;
    private BigDecimal amount;
    private OrderPayment.Status status;
    private String transactionCode;
    private String transactionNote;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
}
