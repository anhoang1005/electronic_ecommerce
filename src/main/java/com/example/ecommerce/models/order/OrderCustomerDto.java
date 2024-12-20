package com.example.ecommerce.models.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCustomerDto {
    private List<OrderShopDto> orderShopDtoList;
}
