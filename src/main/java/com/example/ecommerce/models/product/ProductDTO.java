package com.example.ecommerce.models.product;

import com.example.ecommerce.models.variant.VariantDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long categoryId;
    private Long brandId;

    private String title;
    private String description;

    private String currency;
    private String productState;
    private boolean isHidden;

    private List<VariantDTO> variantDTOS = new ArrayList<>();
    private List<Long> shopCategoryIds = new ArrayList<>();
}
