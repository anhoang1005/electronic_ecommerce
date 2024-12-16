package com.example.ecommerce.controller.brand;

import com.example.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BrandController {
    private final BrandService brandService;
    @GetMapping("/owner/brand")
    public ResponseEntity<List<?>> getBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}
