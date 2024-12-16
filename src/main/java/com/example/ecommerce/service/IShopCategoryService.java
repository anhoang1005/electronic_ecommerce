package com.example.ecommerce.service;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;

public interface IShopCategoryService {
    ResponseBody createCategory(CategoryDto dto, Long shopId);
    ResponseBody unActiveCategory(Long id);
    ResponseBody addProduct(Long id, Long shopId);
    ResponseBody removeProduct(Long id, Long shopId);
    ResponseBody getListCategories(Long shopId,Long parentId, Integer level);
}
