package com.example.ecommerce.service;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Variant;

import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    ResponseBody uploadToCloudinary(MultipartFile file);
    void uploadListFilePostToCloudinary(Post post, List<MultipartFile> listFile);
    CompletableFuture<List<String>> uploadAndSaveImagesProductAsync(List<MultipartFile> file);
    CompletableFuture<Void> processImagesAndSaveAsync(Product product, CompletableFuture<List<String>> imagesUrlFuture);
    CompletableFuture<String> uploadImageVariantAsync(MultipartFile file);
    CompletableFuture<Void> processImageAndSaveAsync(Variant variant, CompletableFuture<String> imageUrlFuture);

    void uploadShopImageToCloudinary(Shop shop, MultipartFile fontCard, MultipartFile backCard);
}
