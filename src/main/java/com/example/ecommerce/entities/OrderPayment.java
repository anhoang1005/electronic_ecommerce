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
public class OrderPayment extends BaseEntity{

    private Long id;
    private Method method;
    private Double amount;
    private Status status;
    private String transactionCode;
    private String transactionNote;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;

    private Long orderId;

    public enum Method{
        THANH_TOAN_KHI_GIAO_HANG,
        VNPAY,
        MOMO,
        PAYPAL
    }

    public enum Status{
        PENDING,
        SUCCESS,
        FAILED,
        REFUNDED
    }
}
