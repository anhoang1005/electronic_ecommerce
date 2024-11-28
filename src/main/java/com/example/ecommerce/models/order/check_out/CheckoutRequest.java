package com.example.ecommerce.models.order.check_out;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {
    private List<CheckoutItem> items;
    private String discountVoucher;
    private String shippingVoucher;
    private Long addressId;
}
