package com.example.ecommerce.models.voucher;

import com.example.ecommerce.entities.Voucher;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopVoucherDto {
    private Long id;
    private String shopCode;
    private String voucherCode;
    private Voucher.DiscountType discountType;
    private String title;
    private String description;
    private Integer voucherCount;
    private Integer remainingCount;
    private Double discountValue;
    private Double maxDiscountValue;
    private Double minTotalOrder;
    private String startedAt;
    private String expiredAt;
    private Voucher.Status status;
}
