package elk_service.elk.product.controller;

import elk_service.elk.product.entites.ProductDocument;
import elk_service.elk.product.service.ProductDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest")
public class ProductController {
    private final ProductDocumentService productDocumentService;
    @GetMapping("/filter")
    public ResponseEntity<?> filterProducts(
            @RequestParam(required = false) String updatedAtStr,
            @RequestParam(required = false) Long shopCategoryId,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Long sold,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ){
        return ResponseEntity.ok(productDocumentService.filterProduct(shopCategoryId,updatedAtStr,rating,sold,minPrice,maxPrice,pageable));
    }
    @GetMapping("/filter-sort")
    public ResponseEntity<Page<ProductDocument>> filterAndSortProducts(
            @RequestParam String shopCode,
            @RequestParam Long shopCategoryId,
            @RequestParam String sortField,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ){
        return ResponseEntity.ok(productDocumentService.filterAndSortProduct(shopCode,shopCategoryId,sortField,pageable,minPrice,maxPrice));
    }



    @PostMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword,
                                            Pageable pageable){
        return ResponseEntity.ok(productDocumentService.searchProductDocument(keyword,pageable));
    }
}