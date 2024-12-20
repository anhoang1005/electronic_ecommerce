package com.example.ecommerce.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingDimensionsDTO {
    private Double weight;
    private Double height;
    private Double length;
    private Double width;
}
