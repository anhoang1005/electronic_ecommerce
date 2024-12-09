package com.example.ecommerce.controller.check_out;

import com.example.ecommerce.models.order.check_out.CheckoutRequest;
import com.example.ecommerce.service.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/api/customer/checkout")
    public ResponseEntity<?> customerCheckout(@RequestBody CheckoutRequest req){
        return ResponseEntity.ok(checkoutService.customerGetCheckOut(req));
    }
}
