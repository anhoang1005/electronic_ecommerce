package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.models.voucher.ShopVoucherDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopCheckoutDto {
    private String shopCode;
    private String shopName;
    private String shopLogo;
    private BigDecimal productTotal = BigDecimal.ZERO;
    private BigDecimal fee = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Long leadDay;
    private String leadTime;

    @JsonIgnore
    private Double totalHeight;
    @JsonIgnore
    private Double totalLength;
    @JsonIgnore
    private Double totalWidth;
    @JsonIgnore
    private Double totalWeight;
    private ShopVoucherDto voucher;

    private List<ShopItem> listItem = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    public static class ShopItem{
        private Long productId;
        private Long variantId;
        private String title;
        private String imageUrl;
        private String variant;
        private BigDecimal sellPrice;
        private Integer quantity;
        private BigDecimal itemTotal;
    }
}
