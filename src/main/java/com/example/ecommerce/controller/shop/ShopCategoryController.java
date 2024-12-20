package com.example.ecommerce.controller.shop;

import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.IShopCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/shop-category")
public class ShopCategoryController {
    private final IShopCategoryService iShopCategoryService;
    @PostMapping
    @PreAuthorize("hasRole('CHUCUAHANG')")
    public ResponseBody create(@RequestBody CategoryDto dto,
                               HttpServletRequest httpServletRequest){
        return iShopCategoryService.createCategory(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseBody delete(@PathVariable Long id){
        return iShopCategoryService.unActiveCategory(id);
    }

    @PostMapping("/{id}/add-product/{idProduct}")
    @PreAuthorize("@customExpressionSecurity.isShopCategoryAllowed(#id)")
    public ResponseBody addProduct(@PathVariable("id")Long id,
                                   @PathVariable("idProduct") Long idProduct){
        return iShopCategoryService.addProduct(id,idProduct);
    }

    @PostMapping("/{id}/remove-product/{idProduct}")
    @PreAuthorize("@customExpressionSecurity.isShopCategoryAllowed(#id)")
    public ResponseBody removeProduct(@PathVariable("id")Long id,
                                   @PathVariable("idProduct") Long idProduct
                                  ){
        return iShopCategoryService.removeProduct(id,idProduct);
    }
    @GetMapping("/by-shop/{shopId}")
    public ResponseBody getByShopId(@PathVariable("shopId") Long shopId,
                                    @RequestParam(value = "categoryParentId", required = false) Long categoryParentId,
                                    @RequestParam(value = "categoryLevel", required = false) Integer categoryLevel
   ){
        return iShopCategoryService.getListCategories(shopId,categoryParentId,categoryLevel);
    }
}
