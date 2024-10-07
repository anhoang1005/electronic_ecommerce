package com.example.ecommerce.controller.external;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GhnController {

    @Autowired
    private GHNService ghnService;

    @GetMapping("/api/guest/ghn/province")
    public ResponseEntity<ResponseBody> getAllProvinceApi() {
        return new ResponseEntity<>(ghnService.getAllProvince(), HttpStatus.OK);
    }

    @GetMapping("/api/guest/ghn/district")
    public ResponseEntity<ResponseBody> getAllDistrictApi(
            @RequestParam("province_id") String provinceId) {
        return new ResponseEntity<>(ghnService.getAllDistrict(provinceId), HttpStatus.OK);
    }

    @GetMapping("/api/guest/ghn/ward")
    public ResponseEntity<ResponseBody> getAllWardApi(
            @RequestParam("district_id") String districtId) {
        return new ResponseEntity<>(ghnService.getAllWard(districtId), HttpStatus.OK);
    }

    @GetMapping("/api/guest/ghn/service")
    public ResponseEntity<ResponseBody> getAllServiceApi(
            @RequestParam("to_district") Integer districtId) {
        return new ResponseEntity<>(ghnService.getService(districtId), HttpStatus.OK);
    }
}
