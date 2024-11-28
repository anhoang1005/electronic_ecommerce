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
    private Voucher.Status status;
    private String startSt;
    private String startEd;
    private String expirySt;
    private String expiryEd;
    private String sort;
    private int pageNumber;
    private int pageSize;
}
