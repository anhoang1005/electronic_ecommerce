package com.example.ecommerce.models.voucher;

import com.example.ecommerce.entities.Voucher;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVoucherRequest {
    private Long id;
    private String shopCode;
    private String voucherCode;
    private Voucher.Type type;
    private Voucher.DiscountType discountType;
    private String title;
    private String description;
    private Integer voucherCount;
    private Integer remainingCount;
    private Double discountValue;
    private Double maxDiscountValue;
    private Double minTotalOrder;
    private Double shippingDiscount;
    private Double maxShippingDiscount;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private Voucher.Status status;
}
