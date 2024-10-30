package com.example.ecommerce.controller.shop;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.IShopCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/shop-category")
public class ShopCategoryController {
    private final IShopCategoryService iShopCategoryService;
    @PostMapping
    public ResponseBody create(@RequestBody CategoryDto dto){
        return iShopCategoryService.createCategory(dto);
    }
}
