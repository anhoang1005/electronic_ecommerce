package com.example.ecommerce.controller.external;

import com.example.ecommerce.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

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
            @RequestParam(name = "prompt_name", required = false) String promptName,
            @RequestParam("message") String message){
        return new ResponseEntity<>(geminiService.chatWithGeminiTextWithPrompt(promptName, message), HttpStatus.OK);
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

}
