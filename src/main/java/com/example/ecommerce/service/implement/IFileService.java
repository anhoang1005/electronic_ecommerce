package com.example.ecommerce.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.entities.*;

import com.example.ecommerce.exceptions.external.FileFailedUploadException;
import com.example.ecommerce.mapper.post.PostMapper;
import com.example.ecommerce.models.post.MediaUrl;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.mapper.post.PostMapper;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.*;

import com.example.ecommerce.repository.PostRepository;
import com.example.ecommerce.service.FileService;

import com.example.ecommerce.utils.GlobalUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@Slf4j
public class IFileService implements FileService {
    private final Cloudinary cloudinary;
    private final ShopRepository shopRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final PostMapper postMapper;
    private final PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    //upload file len cloud
    @Override
    public ResponseBody uploadToCloudinary(MultipartFile file)  {
        try{
            //Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            var data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            //System.out.println(data.get("url"));
            return new ResponseBody(data.get("url"), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (IOException io){
            log.info(io.getMessage());
            throw new FileFailedUploadException("Failed upload file");
        }
    }


    @Async
    @Override
    public void uploadListFilePostToCloudinary(Post post, List<MultipartFile> listFile) {
        List<String> listMedia = new ArrayList<>();
        for(MultipartFile file : listFile){
            try{
                var data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
                listMedia.add(data.get("url").toString());
            } catch (Exception e){
                log.info(e.getMessage());
                throw new FileFailedUploadException("ERROR");
            }
        }
        post.setMediaUrl(postMapper.convertToJson(listMedia));
        postRepository.save(post);
    }

    @Override
    @Async
    public CompletableFuture<List<String>> uploadAndSaveImagesProductAsync(List<MultipartFile> files) {

        CompletableFuture<List<File>> savedFilesFuture = saveFilesToTempAsync(files);

        return savedFilesFuture.thenCompose(savedFiles -> {
            return uploadFilesToCloudinaryAsync(savedFiles);
        });
    }
    @Async
    public CompletableFuture<List<File>> saveFilesToTempAsync(List<MultipartFile> files){
        List<File> savedFiles = new ArrayList<>();
        for(MultipartFile file : files){
            try {
                File tempFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
                file.transferTo(tempFile);
                savedFiles.add(tempFile);
                logger.info("Saved file to temporary directory: {}", tempFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Failed to save file: {}. Error: {}", file.getOriginalFilename(), e);
                savedFiles.add(null);
            }
        }
        return CompletableFuture.completedFuture(savedFiles);
    }
    @Async
    public CompletableFuture<List<String>> uploadFilesToCloudinaryAsync(List<File> files) {
        List<String> imageUrls = new ArrayList<>();
        for (File file : files) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                imageUrls.add(imageUrl);
                logger.info("Uploaded file to Cloudinary: {}", imageUrl);
            } catch (IOException e) {
                logger.error("Failed to upload file: {}. Error: {}", file.getName(), e);
                imageUrls.add(null);
            }
        }
        return CompletableFuture.completedFuture(imageUrls);
    }
    @Async
    @Override
    public CompletableFuture<Void> processImagesAndSaveAsync(Product product, CompletableFuture<List<String>> imagesUrlFuture) {
        return imagesUrlFuture.thenAcceptAsync(imageUrls -> {
            if (imageUrls != null && !imageUrls.isEmpty()) {
                List<Long> imageIds = new ArrayList<>();
                for (int i = 0; i < imageUrls.size(); i++) {
                    ProductImage productImage = new ProductImage();
                    productImage.setImageUrl(imageUrls.get(i));
                    productImage.setIsMainImage(i == 0);
                    productImage.setActive(true);
                    productImage.setProductOfProductImage(product);
                    ProductImage savedProductImg = productImageRepository.save(productImage);

                    imageIds.add(savedProductImg.getId());
                }
                product.setImageIds(imageIds.toString());
                productRepository.save(product);
            } else {
                ProductImage defaultImage = new ProductImage();
                defaultImage.setImageUrl(GlobalUtils.DEFAULT_URL);
                defaultImage.setIsMainImage(true);
                defaultImage.setActive(true);
                defaultImage.setProductOfProductImage(product);
                productImageRepository.save(defaultImage);
            }
        })
//                .thenRunAsync(() -> {
//            try {
//                productProducer.sendProductToQueue(ProductMapper.mapperProductToDocument(product));
//                log.info("Product successfully saved to Elasticsearch.");
//            } catch (Exception e) {
//                log.error("Error saving product to Elasticsearch: " + e.getMessage());
//            }
//        })
                ;
    }

    @Override
    @Async
    public CompletableFuture<String> uploadImageVariantAsync(MultipartFile file) {
        String imageUrl ;
        try{
            Map uploadImg = cloudinary.uploader().upload(file,ObjectUtils.emptyMap());
            imageUrl = uploadImg.get("url").toString();
            logger.info("Uploaded file to Cloudinary: {}", imageUrl);
        }catch (IOException e){
            imageUrl = null;
            logger.error("Failed to upload file {} , error {}",file.getName(),e.getMessage());
        }
        return CompletableFuture.completedFuture(imageUrl);
    }

    @Override
    @Async
    public CompletableFuture<Void> processImageAndSaveAsync(Variant variant, CompletableFuture<String> imageUrlFuture) {
        return imageUrlFuture.thenAcceptAsync(
                imageUrl -> {
                    ProductImage productImage = new ProductImage();
                    productImage.setVariant(variant);
                    productImage.setIsMainImage(true);
                    productImage.setActive(true);
                    productImage.setImageUrl(imageUrl);
                    productImageRepository.save(productImage);
                }
        );
    }


    @Async
    @Override
    public void uploadShopImageToCloudinary(Shop shop, MultipartFile fontCard, MultipartFile backCard){
        try{
            var frontData = this.cloudinary.uploader().upload(fontCard.getBytes(), Map.of());
            var backData = this.cloudinary.uploader().upload(backCard.getBytes(), Map.of());;
            shop.setFrontIdentityCard(frontData.get("url").toString());
            shop.setBackIdentityCard(backData.get("url").toString());
            shop = shopRepository.save(shop);
        } catch (Exception io){
            log.error("Upload error" + io.getMessage());
            throw new FileFailedUploadException(io.getMessage());
        }
    }
}
