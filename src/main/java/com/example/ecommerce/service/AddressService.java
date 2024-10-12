package com.example.ecommerce.service;

import com.example.ecommerce.entities.Address;
import com.example.ecommerce.models.address.AddressRequest;
import com.example.ecommerce.payload.ResponseBody;

public interface AddressService {
    ResponseBody customerGetAllAddress();

    ResponseBody customerCreateAddress(AddressRequest req);

    ResponseBody customerUpdateAddress(AddressRequest req);

    ResponseBody customerDeleteAddress(Long addressId);
}
