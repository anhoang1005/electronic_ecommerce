package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;

public interface ProductCategoryService {
    ResponseBody addProductToCategory(Long categoryId, Long productId);
    ResponseBody removeProductFromCategory(Long categoryId, Long productId);
}
