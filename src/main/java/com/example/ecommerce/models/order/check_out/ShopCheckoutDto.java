package com.example.ecommerce.models.order.check_out;

import com.example.ecommerce.models.voucher.ShopVoucherDto;
import lombok.*;

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
    private List<ShopItem> listItem;
    private ShopVoucherDto voucher;
    private Double discount;

    @Getter
    @Setter
    public static class ShopItem{
        private Long id;
        private Long variantId;
        private String title;
        private String variant;
        private Long sellPrice;
        private Integer quantity;
        private Double total;
    }
}
