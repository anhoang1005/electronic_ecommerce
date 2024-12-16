package com.example.ecommerce.payload.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class VariantAttributeValueResponse {
    private Long attributeId;
    private String attributeName;
    private String attributeValue;
}
