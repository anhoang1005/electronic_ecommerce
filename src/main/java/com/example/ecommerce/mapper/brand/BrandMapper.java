package com.example.ecommerce.mapper.brand;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.models.brand.BrandResponse;
import com.example.ecommerce.payload.object.BaseResponse;

public class BrandMapper {
    public static BrandResponse mapperToBrandResponse(Brand brand) {
        return BrandResponse
                .builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
    }
}
