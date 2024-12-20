package com.example.ecommerce.controller.product.shop;

import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ProductUpdateDTO;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.implement.IProductService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Slf4j
public class ProductShopController {
    private final IProductService iProductService;
    private final ObjectMapper objectMapper;
    private final AuthenticationUtils authenticationUtils;
    @PostMapping("/product")
    @PreAuthorize("hasRole('CHUCUAHANG')")
    public ResponseEntity<?> createProduct(@RequestParam("dto") String jsonDto,
                                           @RequestParam("files") List<MultipartFile> files,

    @RequestParam(required = false) boolean isDev) {

        try {
            ProductDTO productDTO = objectMapper.readValue(jsonDto, ProductDTO.class);
            return ResponseEntity.ok(iProductService.createProduct(productDTO, files,isDev));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/product/{code}")
    @PreAuthorize("@customExpressionSecurity.isShopAllowed(#code) or hasRole('QUANLI')")
    public ResponseEntity<?> findProduct(@PathVariable String code){
        return ResponseEntity.ok(iProductService.findProductHiddenById(code));
    }

    @DeleteMapping("/product/{code}")
    @PreAuthorize("@customExpressionSecurity.isShopAllowed(#code)")
    public ResponseEntity<?> deleteProduct(@PathVariable String code){
        return ResponseEntity.ok(iProductService.deleteProduct(code));
    }

    @PutMapping("/product/{code}")
    @PreAuthorize("@customExpressionSecurity.isShopAllowed(#code)")
    public ResponseEntity<?> updateProduct(@PathVariable String code,
                                           @RequestParam("dto") String jsonDto,
                                           @RequestParam("files") List<MultipartFile> files){
        try {
            ProductUpdateDTO productDTO = objectMapper.readValue(jsonDto, ProductUpdateDTO.class);
            return ResponseEntity.ok(iProductService.updateProduct(code,productDTO, files));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
}

