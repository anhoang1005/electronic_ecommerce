package com.example.ecommerce.service.implement;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
@Slf4j
public class IGeminiService implements GeminiService {

    @Value("${gemini.apikey}")
    private String apiKey;

    @Value("${gemini.api}")
    private String url;

    private final RestTemplate restTemplate;
    public IGeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseBody chatWithGeminiText(String message){
        try{
            String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Gọi API POST với body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody chatWithGeminiImage(String message, MultipartFile file){
        try{
            byte[] fileBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            String jsonString = "{ \"contents\": [{ \"parts\": [ {\"text\": \"" + message + "\"}, " +
                    "{\"inline_data\": {\"mime_type\": \"" + file.getContentType() + "\", \"data\": \"" + base64Image + "\"}}]}]}";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }
}
