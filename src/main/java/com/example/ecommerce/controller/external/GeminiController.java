package com.example.ecommerce.controller.external;

import com.example.ecommerce.models.external.gemini.GeminiDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.GeminiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class GeminiController {
    private final GeminiService geminiService;

    @GetMapping("/api/guest/gemini/chat-text")
    public ResponseEntity<?> chatWithGeminiAPI(
            @RequestParam("message") String message){
        return new ResponseEntity<>(geminiService.chatWithGeminiText(message), HttpStatus.OK);
    }

    @PostMapping("/api/guest/gemini/chat-image")
    public ResponseEntity<?> chatWithGeminiImageAPI(
            @RequestParam(name = "message", required = false) String message,
            @RequestParam(name = "file", required = false)MultipartFile file){
        return new ResponseEntity<>(geminiService.chatWithGeminiImage(message, file), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/prompt-text")
    public ResponseEntity<?> chatWithGeminiPromptAPI(
            @RequestBody GeminiDto dto){
        return new ResponseEntity<>(geminiService.chatWithGeminiTextWithPrompt(dto), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/get-prompt")
    public ResponseEntity<?> createPromptAPI(){
        return new ResponseEntity<>(geminiService.getListPrompt(), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/get-prompt-name")
    public ResponseEntity<?> getPromptAPI(@RequestParam("prompt_name") String promptName){
        ResponseBody responseBody = new ResponseBody(geminiService.getByPromptName(promptName), ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/api/guest/gemini/create-prompt")
    public ResponseEntity<?> createPromptAPI(
            @RequestParam("prompt_name") String promptName,
            @RequestParam("prompt_text") String promptText){
        return new ResponseEntity<>(geminiService.createPrompt(promptName, promptText), HttpStatus.OK);
    }

    @PutMapping("/api/guest/gemini/update-prompt")
    public ResponseEntity<?> updatePromptAPI(
            @RequestParam("prompt_name") String promptName,
            @RequestParam(name = "new_prompt_name", required = false) String newPrompt,
            @RequestParam(name = "prompt_text", required = false) String promptText){
        return new ResponseEntity<>(geminiService.updatePrompt(promptName, newPrompt, promptText), HttpStatus.OK);
    }

    @DeleteMapping("/api/guest/gemini/delete-prompt")
    public ResponseEntity<?> deletePromptAPI(
            @RequestParam("prompt_name") String promptName){
        return new ResponseEntity<>(geminiService.deletePrompt(promptName), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/product-analysis")
    public ResponseEntity<?> reviewProductAPI(@RequestParam("product_id") Long productId){
        return new ResponseEntity<>(geminiService.reviewProduct(productId), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/product-review")
    public ResponseEntity<?> aiReviewProductAPI(@RequestParam("product_id") Long productId){
        return new ResponseEntity<>(geminiService.aiGetReviewProduct(productId), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/product-compare")
    public ResponseEntity<?> aiCompareProductAPI(@RequestParam("product_id") Long productId1,
                                                 @RequestParam("product_other") Long productId2){
        return new ResponseEntity<>(geminiService.aiCompareProduct(productId1, productId2), HttpStatus.OK);
    }
}
