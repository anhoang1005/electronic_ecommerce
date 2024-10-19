package com.example.ecommerce.service;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;

public interface CategoryService {
    ResponseBody createCategory(CategoryDto dto);
}
