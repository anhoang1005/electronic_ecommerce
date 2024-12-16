package com.example.ecommerce.models.order.check_out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutItem {
    private Long variantId;
    private Integer quantity;
}
