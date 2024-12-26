package com.example.ecommerce.controller.product.product_review;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.product_review.ProductReviewFilter;
import com.example.ecommerce.models.product_review.ProductReviewRequest;
import com.example.ecommerce.service.ProductReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ProductReviewController {
    private final ProductReviewService previewService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/guest/product-review")
    public ResponseEntity<?> customerGetReviewOfProduct(@RequestBody ProductReviewFilter filter){
        return ResponseEntity.ok(previewService.guestGetListReviewOfProduct(filter));
    }

    @GetMapping("/api/customer/product-review/get")
    public ResponseEntity<?> customerGetReviewOfProduct(@RequestParam("productId") Long productId){
        return ResponseEntity.ok(previewService.customerGetTheirReview(productId));
    }

    @PostMapping("/api/customer/product-review")
    public ResponseEntity<?> customerCreateReviewOfProduct(
            @RequestParam String json,
            @RequestParam(name = "image", required = false) MultipartFile image){
        try{
            ProductReviewRequest req = objectMapper.readValue(json, ProductReviewRequest.class);
            return ResponseEntity.ok(previewService.customerCreateProductReview(req, image));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/customer/product-review")
    public ResponseEntity<?> customerUpdateReviewOfProduct(
            @RequestParam ProductReviewRequest req,
            @RequestParam(name = "image", required = false) MultipartFile image){
        return ResponseEntity.ok(previewService.customerUpdateProductReview(req, image));
    }

    @DeleteMapping("/api/customer/product-review/{reviewId}")
    public ResponseEntity<?> customerDeleteReviewOfProduct(@PathVariable("reviewId") Long reviewId){
        return ResponseEntity.ok(previewService.customerDeleteProductReview(reviewId));
    }

    @PostMapping("/api/owner/product-review/{reviewId}")
    public ResponseEntity<?> ownerReplyReviewOfProduct(@PathVariable("reviewId") Long reviewId,
                                                       @RequestParam("reply") String reply){
        return ResponseEntity.ok(previewService.shopOwnerReplyProductReview(reviewId, reply));
    }
}
