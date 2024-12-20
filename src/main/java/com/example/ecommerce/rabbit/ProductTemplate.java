package com.example.ecommerce.rabbit;


import com.example.ecommerce.rabbit.elastic.ProductDocument;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTemplate {
    private ProductDocument productDocument;
    private Long productId;
    private ProductElkState productState;

    public enum ProductElkState {
        CREATED,
        UPDATE,
        DELETE
    }
}