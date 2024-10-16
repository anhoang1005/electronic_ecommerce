package com.example.ecommerce.service;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseBody uploadToCloudinary(MultipartFile file);

    void uploadShopImageToCloudinary(Shop shop, MultipartFile fontCard, MultipartFile backCard);
}
