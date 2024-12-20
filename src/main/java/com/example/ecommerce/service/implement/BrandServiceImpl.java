package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.models.brand.BrandResponse;
import com.example.ecommerce.payload.object.ResponsePage;
import com.example.ecommerce.repository.BrandRepository;
import com.example.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public ResponsePage<Brand, BrandResponse> getPageBrands(Pageable pageable) {
        return new ResponsePage<>(brandRepository.findAll(pageable), BrandResponse.class);
    }
}
