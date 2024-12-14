package com.example.ecommerce.payload.product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VariantResponse {
    private String type;
    private Long variantId;
    private Map<String, BigDecimal> value = new HashMap<>();
}
