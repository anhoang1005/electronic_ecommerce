package com.example.ecommerce.payload.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class VariantResponse {
    private Long variantId;
    private BigDecimal price;
    private Integer quantity;
    List<VariantAttributeValueResponse> attributeValues = new ArrayList<>();

}
