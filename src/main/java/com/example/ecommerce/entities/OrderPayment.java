package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_payment")
public class OrderPayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method method;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String transactionCode;

    @Column
    private String transactionNote;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orderOfOrderPayment;

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
