package com.example.ecommerce.controller.address;

import com.example.ecommerce.models.address.AddressRequest;
import com.example.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/api/customer/address/get-all")
    public ResponseEntity<?> customerGetAddressApi(){
        return ResponseEntity.ok(addressService.customerGetAllAddress());
    }

    @PostMapping("/api/customer/address/create")
    public ResponseEntity<?> customerCreateAddressApi(
            @RequestBody AddressRequest req){
        return ResponseEntity.ok(addressService.customerCreateAddress(req));
    }

    @PutMapping("/api/customer/address/update")
    public ResponseEntity<?> customerUpdateAddressApi(
            @RequestBody AddressRequest req){
        return ResponseEntity.ok(addressService.customerUpdateAddress(req));
    }

    @DeleteMapping("/api/customer/address/delete")
    public ResponseEntity<?> customerDeleteAddressApi(
            @RequestParam("address_id") Long addressId){
        return ResponseEntity.ok(addressService.customerDeleteAddress(addressId));
    }
}
