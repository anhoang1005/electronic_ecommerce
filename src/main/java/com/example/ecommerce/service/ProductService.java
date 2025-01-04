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
    ResponseBody shopUpdateProduct(Long id, ProductUpdateDTO dto);
    ResponseBody shopDeleteProduct(Long id);

    //Admin
    ResponseBody adminGetListProductUnAccepted(ShopProductFilter filter);
    ResponseBody checkProduct(Long id, boolean isAccepted);

    //Guest
    ResponseBody guestGetListProduct(ShopProductFilter filter);
    ResponseBody guestSearchProduct(String keyword);
    ResponseBody guestGetAllBrand();
    ResponseBody guestGetAllCategoryOfLevel(Long categoryId);
    ResponseBody getProductHome(int pageNumber, int pageSize);
    ResponseBody getProductDetail(Long productId);

}
