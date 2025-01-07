package com.example.ecommerce.service;


import com.example.ecommerce.entities.Prompt;
import com.example.ecommerce.models.external.gemini.GeminiDto;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {

    ResponseBody chatWithGeminiText(String message);

    ResponseBody chatWithGeminiImage(String message, MultipartFile file);

    ResponseBody chatWithGeminiTextWithPrompt(GeminiDto dto);

    ResponseBody chatWithGeminiTextWithPromptText(String promptText, String message);

    ResponseBody getListPrompt();

    Prompt getByPromptName(String promptName);

    Prompt createPrompt(String promptName, String promptText);

    Prompt updatePrompt(String promptName, String newPrompt, String promptText);

    ResponseBody deletePrompt(String promptName);

    ResponseBody reviewProduct(Long productId);

    ResponseBody aiGetReviewProduct(Long productId);

    ResponseBody aiCompareProduct(Long productId1, Long productId2);

    ResponseBody aiCheckPostCreate(PostRequest req);
}
