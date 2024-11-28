package com.example.ecommerce.service;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ProductUpdateDTO;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ResponseBody createProduct(ProductDTO dto, List<MultipartFile> multipartFiles);
    ResponseBody findProductById(Long id);
    ResponseBody findProductHiddenById(Long id);
    ResponseBody updateProduct(Long id, ProductUpdateDTO dto);
    ResponseBody checkProduct(Long id);
    ResponseBody deleteProduct(Long id);
}
