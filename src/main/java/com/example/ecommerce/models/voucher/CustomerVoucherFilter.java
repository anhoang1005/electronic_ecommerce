package com.example.ecommerce.models.voucher;

import com.example.ecommerce.entities.Voucher;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerVoucherFilter {
    private String voucherCode;
    private Voucher.DiscountType discountType;
    private Voucher.Type type;
    private int pageNumber;
    private int pageSize;
}
