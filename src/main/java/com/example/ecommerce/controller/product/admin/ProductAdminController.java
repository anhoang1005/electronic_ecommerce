package com.example.ecommerce.controller.product.admin;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class ProductAdminController {
    private final ProductService productService;
    @PutMapping("/product/{code}/approval")
    public ResponseBody approveProduct(@PathVariable String code) {
        return productService.acceptProduct(code);
    }

    @PutMapping("/product/{code}/rejection")
    public ResponseBody rejectProduct(@PathVariable String code) {
        return productService.rejectProduct(code);
    }
}
