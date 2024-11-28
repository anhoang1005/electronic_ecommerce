package com.example.ecommerce.models.product;

import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String title;
    private String description;
    private boolean isHidden;
    private String productState;
}
