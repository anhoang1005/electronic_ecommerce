package com.example.ecommerce.mapper.category;

import com.example.ecommerce.entities.Category;
import com.example.ecommerce.payload.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse entityToCategoryResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());
        return categoryResponse;
    }
}
