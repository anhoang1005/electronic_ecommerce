package com.example.ecommerce.rabbit.elastic;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDocument {
    private Long id;
    private Long quantity;
    private Long variantId;
    private BigDecimal price;
    private Long orderId;
    private Long userId;
    private Date orderDate;
    private Long productId;
    private String title;
    private String url;
    private String code;
    private Long shopId;
}
