package com.example.ecommerce.controller.category;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/owner/category")
    public ResponseBody createCategoryByShop(@RequestBody CategoryDto dto){
        return categoryService.createCategory(dto);
    }
    @PostMapping("/admin/category")
    public ResponseBody createCategoryByAdmin(@RequestBody CategoryDto dto){
        return categoryService.createCategory(dto);
    }
    @GetMapping("/owner/category")
    public ResponseBody getCategories(@RequestParam(value = "categoryParentId", required = false) Long categoryParentId,
                                      @RequestParam(value = "categoryLevel", required = false) Integer categoryLevel){
        return categoryService.getListCategory(categoryParentId,categoryLevel);
    }

    @GetMapping("/guest/category/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }
}
