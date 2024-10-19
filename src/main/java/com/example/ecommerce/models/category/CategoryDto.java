package com.example.ecommerce.models.category;

import lombok.Data;

@Data
public class CategoryDto {
    private String categoryName;
    private String description;
    private Long parentId;
    private Long shopId;
}
