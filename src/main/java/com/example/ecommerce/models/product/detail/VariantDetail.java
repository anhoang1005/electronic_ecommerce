package com.example.ecommerce.models.product.detail;

import com.example.ecommerce.entities.ProductVariant;
import com.example.ecommerce.mapper.product.ProductMapper;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDetail {
    private Long variantId;
    private List<VariantAtt> attribute;
    private ProductVariant.Status status;
    private Integer soldCount;
    private Integer stockCount;
    private String description;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VariantAtt{
        private String name;
        private String value;
    }
}
