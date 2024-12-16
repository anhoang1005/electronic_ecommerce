package com.example.ecommerce.controller.external;

import com.example.ecommerce.models.external.ghn.FeeRequest;
import com.example.ecommerce.models.external.ghn.TimeRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
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
    public ResponseEntity<?> getAllServiceApi(
            @RequestParam("to_district") Integer districtId) {
        return new ResponseEntity<>(ghnService.getService(districtId), HttpStatus.OK);
    }

    @PostMapping("/api/guest/ghn/fee")
    public ResponseEntity<?> getFeeApi(
            @RequestBody FeeRequest req) {
        return new ResponseEntity<>(ghnService.getFee(req), HttpStatus.OK);
    }

    @PostMapping("/api/guest/ghn/time")
    public ResponseEntity<?> getTimeApi(
            @RequestBody TimeRequest req) {
        return new ResponseEntity<>(ghnService.getTime((req)), HttpStatus.OK);
    }
}
