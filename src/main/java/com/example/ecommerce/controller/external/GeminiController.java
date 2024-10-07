package com.example.ecommerce.controller.external;

import com.example.ecommerce.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/api/guest/gemini/chat-text")
    public ResponseEntity<?> chatWithGeminiAPI(
            @RequestParam("message") String message
    ){
        return new ResponseEntity<>(geminiService.chatWithGeminiText(message), HttpStatus.OK);
    }

    @GetMapping("/api/guest/gemini/chat-image")
    public ResponseEntity<?> chatWithGeminiImageAPI(
            @RequestParam("message") String message,
            @RequestParam("file")MultipartFile file
    ){
        return new ResponseEntity<>(geminiService.chatWithGeminiImage(message, file), HttpStatus.OK);
    }
}
