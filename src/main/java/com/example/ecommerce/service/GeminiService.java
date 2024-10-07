package com.example.ecommerce.service;


import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {

    ResponseBody chatWithGeminiText(String message);

    ResponseBody chatWithGeminiImage(String message, MultipartFile file);
}
