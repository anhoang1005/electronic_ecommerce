package com.example.ecommerce.mapper.address;

import com.example.ecommerce.entities.Address;
import com.example.ecommerce.models.address.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDto entityToAddressDto(Address address){
        return AddressDto.builder()
                .addressId(address.getId())
                .addressType(address.getAddressType().getValueString())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .address(address.getHouseName()
                                + ", " + address.getWardName()
                                + ", " + address.getDistrictName()
                                + ", " + address.getProvinceName())
                .wardId(address.getWardId())
                .districtId(address.getDistrictId())
                .provinceId(address.getProvinceId())
                .isDefault(address.getIsDefault())
                .note(address.getNote())
                .build();
    }
}
