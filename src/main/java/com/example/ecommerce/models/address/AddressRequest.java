package com.example.ecommerce.models.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequest {
    private Long addressId;
    private Integer addressType;
    private String fullName;
    private String phoneNumber;
    private String wardId;
    private String districtId;
    private String provinceId;
    private String houseName;
    private String wardName;
    private String districtName;
    private String provinceName;
    private String note;
    private Boolean isDefault;
}
