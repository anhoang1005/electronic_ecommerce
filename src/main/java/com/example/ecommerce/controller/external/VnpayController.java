package com.example.ecommerce.controller.external;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class VnpayController {

    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/api/guest/vnpay/create")
    public ResponseEntity<ResponseBody> uploadOneFileApi(
            @RequestParam("total") long total,
            @RequestParam("orderType")String orderType) throws UnsupportedEncodingException {
        return new ResponseEntity<>(vnPayService.createPayment(total, orderType), HttpStatus.OK);
    }
}
