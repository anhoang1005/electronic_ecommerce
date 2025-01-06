package com.example.ecommerce.controller.product.guest;

import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ShopProductFilter;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/guest/product/all-brand")
    public ResponseEntity<?> getAllBrand(){
        return ResponseEntity.ok(productService.guestGetAllBrand());
    }

    @GetMapping("/api/guest/product/category-parent")
    public ResponseEntity<?> getAllCategoryParent(
            @RequestParam(name = "category_id", required = false) Long categoryId){
        return ResponseEntity.ok(productService.guestGetAllCategoryOfLevel(categoryId));
    }

    @GetMapping("/api/guest/product/get-list-product")
    public ResponseEntity<?> getAll(@RequestParam("page_number") int pageNumber,
                                    @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(productService.getProductHome(pageNumber, pageSize));
    }

    @GetMapping("/api/guest/product/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    @PostMapping("/api/guest/product/get-shop-product")
    public ResponseEntity<?> getShopProduct(@RequestBody ShopProductFilter filter){
        return ResponseEntity.ok(productService.guestGetListProduct(filter));
    }

    @GetMapping("/api/guest/product/search")
    public ResponseEntity<?> searchProduct(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(productService.guestSearchProduct(keyword));
    }

    @PostMapping("/api/admin/product/accepted-dashboard")
    public ResponseEntity<?> adminDashboardProduct(@RequestBody ShopProductFilter filter){
        return ResponseEntity.ok(productService.adminGetListProductUnAccepted(filter));
    }

    @PutMapping("/api/admin/product/accepted")
    public ResponseEntity<?> checkProduct(@RequestParam("product_id") Long id,
                                          @RequestParam("is_accepted") boolean isAccepted){
        return ResponseEntity.ok(productService.checkProduct(id, isAccepted));
    }

    @PostMapping("/api/guest/product/fate-create")
    public ResponseEntity<?> fateProduct(@RequestBody ProductDTO dto){
        return ResponseEntity.ok(productService.createFateProduct(dto));
    }
}
