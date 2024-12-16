package com.example.ecommerce.models.product.detail;

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
//    private List<ProductMapper.VariantAtt> variantName;
    private Map<String, String> attribute;
    private Integer soldCount;
    private Integer stockCount;
    private String description;
}
