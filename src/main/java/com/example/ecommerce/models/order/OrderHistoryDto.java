package com.example.ecommerce.models.order;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistoryDto {
    private Long id;
    private String event;
    private String description;
    private String note;
}
