package com.example.ecommerce.mapper.voucher;

import com.example.ecommerce.entities.ShopVoucher;
import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.models.voucher.ShopVoucherDto;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VoucherMapper {
    private final DateTimeMapperUtils timeUtils;

    public VoucherDto voucherToVoucherDto(Voucher voucher){
        return VoucherDto.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .type(voucher.getType())
                .discountType(voucher.getDiscountType())
                .title(voucher.getTitle())
                .description(voucher.getDescription())
                .voucherCount(voucher.getVoucherCount())
                .remainingCount(voucher.getRemainingCount())
                .discountValue(voucher.getDiscountValue())
                .maxDiscountValue(voucher.getMaxDiscountValue())
                .minTotalOrder(voucher.getMinTotalOrder())
                .shippingDiscount(voucher.getShippingDiscount())
                .maxShippingDiscount(voucher.getMaxShippingDiscount())
                .startedAt(timeUtils.localDateTimeToString(voucher.getStartedAt()))
                .expiredAt(timeUtils.localDateTimeToString(voucher.getExpiredAt()))
                .status(voucher.getStatus())
                .build();
    }

    public ShopVoucherDto shopVoucherToVoucherDto(ShopVoucher voucher){
        return ShopVoucherDto.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .discountType(voucher.getDiscountType())
                .title(voucher.getTitle())
                .description(voucher.getDescription())
                .voucherCount(voucher.getVoucherCount())
                .remainingCount(voucher.getRemainingCount())
                .discountValue(voucher.getDiscountValue())
                .maxDiscountValue(voucher.getMaxDiscountValue())
                .minTotalOrder(voucher.getMinTotalOrder())
                .startedAt(timeUtils.localDateTimeToString(voucher.getStartedAt()))
                .expiredAt(timeUtils.localDateTimeToString(voucher.getExpiredAt()))
                .status(voucher.getStatus())
                .build();
    }
}
