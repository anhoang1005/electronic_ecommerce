package com.example.ecommerce.controller.product.guest;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest")
public class ProductController {
    private final ProductService productService;
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
