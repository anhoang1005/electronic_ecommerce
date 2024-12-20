package com.example.ecommerce.models.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Long variantId;
    private String productTitle;
    private String variantName;
    private String productImage;
    private Long quantity;
    private BigDecimal price;
    private String description;
}
