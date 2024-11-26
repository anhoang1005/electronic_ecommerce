package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shop_voucher")
public class ShopVoucher extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_voucher_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String voucherCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Voucher.DiscountType discountType;

    @Column(nullable = false)
    private String title;
    private String description;
    private Integer voucherCount;
    private Integer remainingCount;
    private Double discountValue;
    private Double maxDiscountValue;
    private Double minTotalOrder;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Voucher.Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shopOfShopVoucher;

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

}
