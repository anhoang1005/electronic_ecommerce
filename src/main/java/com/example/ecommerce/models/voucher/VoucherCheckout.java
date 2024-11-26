package com.example.ecommerce.models.voucher;

import com.example.ecommerce.entities.Voucher;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherCheckout {
    private ShopVoucherDto shopVoucherDto;
    private VoucherDto voucherDto;
    private double discount;
    private String message;
}
