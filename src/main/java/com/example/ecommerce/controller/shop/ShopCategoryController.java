package com.example.ecommerce.controller.shop;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.IShopCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/shop-category")
public class ShopCategoryController {
    private final IShopCategoryService iShopCategoryService;
    @PostMapping
    public ResponseBody create(@RequestBody CategoryDto dto,
                               HttpServletRequest httpServletRequest){
        return iShopCategoryService.createCategory(dto,Long.valueOf(httpServletRequest.getHeader("shop-id")));
    }
    @DeleteMapping("/{id}")
    public ResponseBody delete(@PathVariable Long id,
                               HttpServletRequest httpServletRequest){
        return iShopCategoryService.unActiveCategory(id,Long.valueOf(httpServletRequest.getHeader("shop-id")));
    }
}
