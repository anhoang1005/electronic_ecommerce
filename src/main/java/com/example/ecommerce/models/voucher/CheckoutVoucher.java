package com.example.ecommerce.models.voucher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutVoucher {
    private String shopCode;
    private String voucherCode;
    private Double totalAmount;
    private int pageSize;
    private int pageNumber;
}
