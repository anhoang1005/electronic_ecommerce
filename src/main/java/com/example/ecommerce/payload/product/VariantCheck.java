package com.example.ecommerce.payload.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantCheck {
    private Boolean result;
    private Check checkVariant;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Check {
        private Long variantId;
        private String variant;
    }
}
