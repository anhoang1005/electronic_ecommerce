package com.example.ecommerce.service;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;

public interface CategoryService {
    ResponseBody createCategory(CategoryDto dto);
    ResponseBody findCategoryById(Long id);
    ResponseBody updateCategory(Long id, CategoryDto dto);
    ResponseBody unActiveCategory(Long id);
    ResponseBody removeProductFromCategory(Long id,Long productId);
    ResponseBody addProductToCategory(Long id, Long productId);
    ResponseBody getListCategory(Long parentId, Integer level);
}
