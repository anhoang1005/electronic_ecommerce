package com.example.ecommerce.models.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto {
    private Long id;
    private String brand;
    private Long productCount;
}
