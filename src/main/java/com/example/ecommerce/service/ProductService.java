package com.example.ecommerce.service;

import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ProductUpdateDTO;
import com.example.ecommerce.models.product.ShopProductFilter;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    //Shop
    ResponseBody createProduct(ProductDTO dto, List<MultipartFile> multipartFiles);
    ResponseBody shopGetDashboard(ShopProductFilter filter);
    ResponseBody updateProduct(Long id, ProductUpdateDTO dto);
    ResponseBody shopDeleteProduct(Long id);
    ResponseBody shopGetProductDetail(Long id);


    ResponseBody findProductById(Long id);
    ResponseBody findProductHiddenById(Long id);
    ResponseBody checkProduct(Long id);


    ResponseBody getProductHome(int pageNumber, int pageSize);
    ResponseBody getProductDetail(Long productId);
}
