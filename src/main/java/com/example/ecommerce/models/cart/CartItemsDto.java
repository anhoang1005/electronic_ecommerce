package com.example.ecommerce.models.cart;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemsDto implements Serializable {
	private Long productId;
	private Long variantId;
	private String title;
	private String productImage;
	private Object variant;
	private BigDecimal sellPrice;
	private Integer quantity;
	private BigDecimal totalAmount;
}
