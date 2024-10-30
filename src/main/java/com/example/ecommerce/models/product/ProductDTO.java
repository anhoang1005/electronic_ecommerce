package com.example.ecommerce.models.product;

import com.example.ecommerce.models.variant.VariantDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long categoryId;
    private String title;
    private String description;
    private String currency;
    private boolean isHidden;
    private List<VariantDTO> variantDTOS = new ArrayList<>();
}
