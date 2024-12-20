package com.example.ecommerce.controller.brand;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.models.brand.BrandResponse;
import com.example.ecommerce.payload.object.ResponsePage;
import com.example.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResponsePage<Brand, BrandResponse>> getAllBrands(Pageable pageable) {
        return ResponseEntity.ok(brandService.getPageBrands(pageable));
    }
}
