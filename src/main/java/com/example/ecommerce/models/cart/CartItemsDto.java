package com.example.ecommerce.models.cart;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemsDto implements Serializable {
	private Long productId;
	private String title;
	private String thumbnail;
	private Double sellPrice;
	private Integer quantity;
}
