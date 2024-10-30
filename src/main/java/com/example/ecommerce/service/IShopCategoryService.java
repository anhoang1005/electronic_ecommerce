package com.example.ecommerce.service;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;

public interface IShopCategoryService {
    ResponseBody createCategory(CategoryDto dto, Long shopId);
    ResponseBody unActiveCategory(Long id, Long shopId);
    ResponseBody addProduct(Long id, Long shopId, Long productId);
    ResponseBody removeProduct(Long id, Long shopId, Long productId);
}
