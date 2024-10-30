package com.example.ecommerce.service;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);
    ResponseBody createProduct(ProductDTO dto, List<MultipartFile> multipartFiles);
}
