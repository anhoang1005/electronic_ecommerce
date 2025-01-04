package com.example.ecommerce.models.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String categoryName;
    private Long childrenCount;
}
