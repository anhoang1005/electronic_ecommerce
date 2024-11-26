package com.example.ecommerce.models.voucher;

import com.example.ecommerce.entities.Voucher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherFilter {
    private String voucherCode;
    private Voucher.Type type;
    private Voucher.DiscountType discountType;
    private String createdAt;
    private String startAt;
    private String expiryAt;
    private String sort;
    private int pageNumber;
    private int pageSize;
}
