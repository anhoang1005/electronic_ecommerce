package com.example.ecommerce.controller.category;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/owner/categories")
    public ResponseBody createCategoryByShop(@RequestBody CategoryDto dto){
        return categoryService.createCategory(dto);
    }
    @PostMapping("/admin/categories")
    public ResponseBody createCategoryByAdmin(@RequestBody CategoryDto dto){
        return categoryService.createCategory(dto);
    }
}
