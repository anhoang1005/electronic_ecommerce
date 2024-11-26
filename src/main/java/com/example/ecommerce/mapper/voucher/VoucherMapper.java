package com.example.ecommerce.mapper.voucher;

import com.example.ecommerce.entities.ShopVoucher;
import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.models.voucher.ShopVoucherDto;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VoucherMapper {
    private final StringFormatUtils stringUtil;

    public VoucherDto voucherToVoucherDto(Voucher voucher){
        return VoucherDto.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .type(voucher.getType())
                .discountType(voucher.getDiscountType())
                .title(voucher.getTitle())
                .description(genVoucherDescriptionVoucher(voucher))
                .voucherCount(voucher.getVoucherCount())
                .remainingCount(voucher.getRemainingCount())
                .discountValue(voucher.getDiscountValue())
                .maxDiscountValue(voucher.getMaxDiscountValue())
                .minTotalOrder(voucher.getMinTotalOrder())
                .shippingDiscount(voucher.getShippingDiscount())
                .maxShippingDiscount(voucher.getMaxShippingDiscount())
                .startedAt(voucher.getStartedAt().toString())
                .expiredAt(voucher.getExpiredAt().toString())
                .status(voucher.getStatus())
                .build();
    }

    public ShopVoucherDto shopVoucherToVoucherDto(ShopVoucher voucher){
        return ShopVoucherDto.builder()
                .id(voucher.getId())
                .shopCode(voucher.getShopOfShopVoucher().getShopCode())
                .voucherCode(voucher.getVoucherCode())
                .discountType(voucher.getDiscountType())
                .title(voucher.getTitle())
                .description(genVoucherDescriptionShopVoucher(voucher))
                .voucherCount(voucher.getVoucherCount())
                .remainingCount(voucher.getRemainingCount())
                .discountValue(voucher.getDiscountValue())
                .maxDiscountValue(voucher.getMaxDiscountValue())
                .minTotalOrder(voucher.getMinTotalOrder())
                .startedAt(voucher.getStartedAt().toString())
                .expiredAt(voucher.getExpiredAt().toString())
                .status(voucher.getStatus())
                .build();
    }

    private String genVoucherDescriptionVoucher(Voucher req){
        String result;
        if(req.getType()== Voucher.Type.DISCOUNT){
            if(req.getDiscountType()== Voucher.DiscountType.AMOUNT){
                if(req.getMinTotalOrder()!=null){
                    result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "\n" +
                            "Đơn tối thiểu " + stringUtil.formatPrice(req.getMinTotalOrder().longValue());
                } else {
                    result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue());
                }
            } else {
                if(req.getMinTotalOrder()!=null){
                    result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "%\n" +
                            "Tối đa " + stringUtil.formatPrice(req.getMaxDiscountValue().longValue()) + "\n" +
                            "Đơn tối thiểu " + stringUtil.formatPrice(req.getMinTotalOrder().longValue());
                } else {
                    result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "%\n" +
                            "Tối đa " + stringUtil.formatPrice(req.getMaxDiscountValue().longValue());
                }
            }
        } else {
            if(req.getMaxShippingDiscount()!=null){
                result = "Giảm giá " + stringUtil.formatPrice(req.getShippingDiscount().longValue()) + "\n" +
                        "Đơn tối thiểu " + stringUtil.formatPrice(req.getMaxShippingDiscount().longValue());
            } else {
                result = "Giảm giá " + stringUtil.formatPrice(req.getShippingDiscount().longValue());
            }
        }
        return result;
    }

    private String genVoucherDescriptionShopVoucher(ShopVoucher req){
        String result;
        if(req.getDiscountType()== Voucher.DiscountType.AMOUNT){
            if(req.getMinTotalOrder()!=null){
                result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "\n" +
                        "Đơn tối thiểu " + stringUtil.formatPrice(req.getMinTotalOrder().longValue());
            } else {
                result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue());
            }
        } else {
            if(req.getMinTotalOrder()!=null){
                result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "%\n" +
                        "Tối đa " + stringUtil.formatPrice(req.getMaxDiscountValue().longValue()) + "\n" +
                        "Đơn tối thiểu " + stringUtil.formatPrice(req.getMinTotalOrder().longValue());
            } else {
                result = "Giảm giá " + stringUtil.formatPrice(req.getDiscountValue().longValue()) + "%\n" +
                        "Tối đa " + stringUtil.formatPrice(req.getMaxDiscountValue().longValue());
            }
        }
        return result;
    }
}
