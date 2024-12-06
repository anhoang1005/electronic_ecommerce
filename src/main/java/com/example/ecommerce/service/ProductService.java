package com.example.ecommerce.service;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ProductUpdateDTO;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ResponseBody createProduct(ProductDTO dto, List<MultipartFile> multipartFiles);
    ResponseBody findProductByCode(String code);
    ResponseBody findProductHiddenById(String code);
    ResponseBody updateProduct(String code, ProductUpdateDTO dto);
    ResponseBody acceptProduct(String code);
    ResponseBody deleteProduct(String code);
    ResponseBody getEditProduct(String code);
}
