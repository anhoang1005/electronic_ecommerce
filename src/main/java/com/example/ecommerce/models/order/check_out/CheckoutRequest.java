package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.entities.OrderPayment;
import com.example.ecommerce.entities.OrderShipping;
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
    private OrderPayment.Method paymentMethod;
    private OrderShipping.Method shippingMethod;
    private List<ShopDiscount> shopDiscounts;

    @Getter
    @Setter
    @Builder
    public static class ShopDiscount{
        private String shopCode;
        private String voucherCode;
    }
}
