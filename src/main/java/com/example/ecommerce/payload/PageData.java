package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageData {
    private Object data;
    private Long totalAmount;
    private Integer totalPage;
    private Integer pageNumber;
    private Integer pageSize;
}
