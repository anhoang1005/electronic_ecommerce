package com.example.ecommerce.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageData {
    private Object data;
    private long totalAmount;
    private int totalPage;
}
