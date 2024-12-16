package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.repository.BrandRepository;
import com.example.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
