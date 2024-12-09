package com.example.ecommerce.models.product.detail;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDetail {
    private Long variantId;
    private String variantName;
    private Map<String, String> attribute;
    private Integer soldCount;
    private Integer stockCount;
    private String description;
}
