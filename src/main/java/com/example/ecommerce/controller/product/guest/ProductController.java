package com.example.ecommerce.controller.product.guest;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.elastic.ProductDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest")
public class ProductController {
    private final ProductService productService;
    private final ProductDocumentService productDocumentService;
    @GetMapping("/product/{id}")
    public ResponseBody getProduct(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @GetMapping("/product/get-list-product")
    public ResponseEntity<?> getAll(@RequestParam("page_number") int pageNumber,
                                    @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(productService.getProductHome(pageNumber, pageSize));
    @GetMapping("/product/{code}")
    public ResponseBody getProduct(@PathVariable String code){
        return productService.findProductByCode(code);
    }

    @GetMapping("/product/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(productService.getProductDetail(id));
    }
}
