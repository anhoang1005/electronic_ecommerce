package com.example.ecommerce.controller.product.shop;

import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.implement.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Slf4j
public class ProductShopController {
    private final IProductService iProductService;
    private final ObjectMapper objectMapper;

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestParam("dto") String jsonDto,
                                           @RequestParam("files") List<MultipartFile> files) {
        log.info("controller");

        try {
            ProductDTO productDTO = objectMapper.readValue(jsonDto, ProductDTO.class);
            for(int i = 0 ; i < productDTO.getVariantDTOS().size() ;i++){
                String variantImageKey = "variantDTOS[" + i + "].productImage"; // Key trong form-data
                MultipartFile variantImage = files.stream()
                        .filter(file -> variantImageKey.equals(file.getName())) // Tìm ảnh tương ứng
                        .findFirst()
                        .orElse(null);

                if (variantImage != null) {
                    productDTO.getVariantDTOS().get(i).setProductImage(variantImage);
                    log.info("Received image for variant {}: {}", i, variantImage.getOriginalFilename());
                } else {
                    log.warn("No image received for variant {}", i);
                }
            }
            return ResponseEntity.ok(iProductService.createProduct(productDTO, files));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/product/{id}")
    @PreAuthorize("@customExpressionSecurity.isShopAllowed(#id) or hasAuthority('QUANLY')")
    public ResponseEntity<?> findProduct(@PathVariable Long id){
        return ResponseEntity.ok(iProductService.findProductHiddenById(id));
    }

    @DeleteMapping("/product/{id}")
    @PreAuthorize("@customExpressionSecurity.isShopAllowed(#id)")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(iProductService.deleteProduct(id));
    }
}

