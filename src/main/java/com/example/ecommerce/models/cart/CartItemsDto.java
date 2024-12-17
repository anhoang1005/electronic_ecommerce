package com.example.ecommerce.models.cart;

import com.example.ecommerce.models.product.detail.VariantDetail;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
	private List<VariantDetail.VariantAtt> variant;
	private BigDecimal sellPrice;
	private Integer quantity;
	private BigDecimal totalAmount;
}
