package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.models.address.AddressDto;
import com.example.ecommerce.models.voucher.VoucherDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutDto {
    private String fullName;
    private AddressDto addressDto;
    private Integer itemsCount;
    private BigDecimal productTotal;
    private BigDecimal totalFee;
    private BigDecimal shopDiscount;
    private BigDecimal ecommerceDiscount;
    private BigDecimal ecommerceShipping;
    private BigDecimal totalAmount;
    private VoucherDto discountVoucher;
    private VoucherDto shippingVoucher;
    private List<ShopCheckoutDto> shopBill;
}
