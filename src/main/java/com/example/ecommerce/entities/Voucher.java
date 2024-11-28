package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vourches")
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String voucherCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private String title;
    private String description;

    @Column(nullable = false)
    private Integer voucherCount;
    private Integer remainingCount;

    private Double discountValue;
    private Double maxDiscountValue;
    private Double minTotalOrder;

    private Double shippingDiscount;
    private Double maxShippingDiscount;

    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum DiscountType{
        AMOUNT(1, "Giá trị"),
        PERCENT(2, "Phần trăm");
        private final int valueInt;
        private final String valueString;
        DiscountType(int valueInt, String valueString){
            this.valueInt = valueInt;
            this.valueString = valueString;
        }
        public String getValueString() { return this.valueString; }
        public int getValueInt() { return  this.valueInt; }
    }

    public enum Status{
        ENABLE,
        WAIT,
        EXPIRED,
        DISABLE,
        DELETED
    }

    public enum Type{
        DISCOUNT,
        SHIPPING
    }

    @PrePersist
    void onCreate(){
        this.remainingCount = 0;
    }
}
