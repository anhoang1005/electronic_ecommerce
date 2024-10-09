package com.example.ecommerce.entities;

import java.time.LocalDateTime;

public class Promotion {
    private Long id;
    private String promotionName;
    private Double discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
}
