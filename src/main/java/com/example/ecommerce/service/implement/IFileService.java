package com.example.ecommerce.service.implement;

import com.cloudinary.Cloudinary;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.external.FileFailedUploadException;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class IFileService implements FileService {
    private final Cloudinary cloudinary;
    private final ShopRepository shopRepository;

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
