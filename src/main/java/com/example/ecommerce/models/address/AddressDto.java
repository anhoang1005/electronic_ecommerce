package com.example.ecommerce.models.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    @JsonProperty("address_id")
    private Long addressId;
    @JsonProperty("address_type")
    private String addressType;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("is_default")
    private Boolean isDefault;
    @JsonProperty("ward_id")
    private String wardId;
    @JsonProperty("district_id")
    private String districtId;
    @JsonProperty("province_id")
    private String provinceId;
    @JsonProperty("note")
    private String note;
}
