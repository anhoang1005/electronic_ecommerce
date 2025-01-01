package com.example.ecommerce.service;


import com.example.ecommerce.entities.Prompt;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {

    ResponseBody chatWithGeminiText(String message);

    ResponseBody chatWithGeminiImage(String message, MultipartFile file);

    ResponseBody chatWithGeminiTextWithPrompt(String prompt, String message);

    ResponseBody chatWithGeminiTextWithPromptText(String promptText, String message);

    ResponseBody getListPrompt();

    Prompt getByPromptName(String promptName);

    Prompt createPrompt(String promptName, String promptText);

    Prompt updatePrompt(String promptName, String newPrompt, String promptText);

    ResponseBody deletePrompt(String promptName);

    ResponseBody reviewProduct(Long productId);

    ResponseBody aiGetReviewProduct(Long productId);
}
