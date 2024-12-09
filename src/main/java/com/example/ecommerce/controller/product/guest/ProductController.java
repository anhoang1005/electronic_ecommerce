package com.example.ecommerce.controller.product.guest;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/api/guest/product/{id}")
    public ResponseBody getProduct(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @GetMapping("/api/guest/product/get-home")
    public ResponseEntity<?> getAll(@RequestParam("page_number") int pageNumber,
                                    @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(productService.getProductHome(pageNumber, pageSize));
    }

    @GetMapping("/api/guest/product/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(productService.getProductDetail(id));
    }
}
