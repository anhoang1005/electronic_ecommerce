package com.example.ecommerce.models.cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
	private Integer cartCount;
	private Double totalPrice;
	private List<CartItemsDto> listItemsDto;
}
