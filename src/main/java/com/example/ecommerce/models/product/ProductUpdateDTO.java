package com.example.ecommerce.models.product;

import com.example.ecommerce.models.variant.VariantDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductUpdateDTO {
    private String title;
    private String description;
    private boolean isHidden;
    private String productState;
    private Long categoryId;
    private Long brandId;
    private List<VariantDTO> variantDTOS = new ArrayList<>();
    private List<Long> shopCategoryIds = new ArrayList<>();
}
