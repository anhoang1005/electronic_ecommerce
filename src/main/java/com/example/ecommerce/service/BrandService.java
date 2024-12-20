package com.example.ecommerce.service;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.models.brand.BrandResponse;
import com.example.ecommerce.payload.object.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BrandService {
    ResponsePage<Brand, BrandResponse> getPageBrands(Pageable pageable);
}
