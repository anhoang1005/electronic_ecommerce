package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.models.address.AddressDto;
import com.example.ecommerce.models.voucher.VoucherDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDto {
    private String fullName;
    private AddressDto addressDto;

    private VoucherDto discountVoucher;
    private VoucherDto shippingVoucher;

    private String leadDate;
    private Long leadDay;
    private List<ShopCheckoutDto> shopBill;

    private Double voucherDiscount;
    private Double shippingDiscount;

    private Integer itemsCount;
    private Double productTotal;
    private Double totalPrice;
}
