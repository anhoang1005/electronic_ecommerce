package com.example.ecommerce.rabbit.messageTemplate;

import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.rabbit.state.ProductElkState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTemplate {
    private ProductDocument productDocument;
    private Long productId;
    private ProductElkState productState;
}
