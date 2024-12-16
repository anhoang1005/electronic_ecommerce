package com.example.ecommerce.models.category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBreadcrumb {
    private Long id;
    private String category;
}
