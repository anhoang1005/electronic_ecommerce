package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseBody uploadToCloudinary(MultipartFile file);
}
