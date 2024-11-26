package com.example.ecommerce.models.voucher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherFilter {
    private String voucherCode;
    private String createdAt;
    private String startAt;
    private String expiryAt;
}
