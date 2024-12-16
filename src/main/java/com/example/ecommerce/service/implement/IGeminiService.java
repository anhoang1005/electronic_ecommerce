package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Prompt;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.external.gemini.GeminiRequest;
import com.example.ecommerce.models.external.gemini.GeminiResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.PromptRepository;
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

    private final PromptRepository promptRepository;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String url;

    private final RestTemplate restTemplate;
    public IGeminiService(RestTemplate restTemplate,
                          @Value("${gemini.apikey}") String apiKey,
                          @Value("${gemini.api}") String url,
                          PromptRepository promptRepository,
                          ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.promptRepository = promptRepository;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.url = url;
    }

    @Override
    public ResponseBody chatWithGeminiText(String message){
        try{
            //String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(message, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            System.out.println(jsonString);
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
            String jsonString;
            if(file!=null){
                if(message==null) message = "Mô tả ảnh";
                byte[] fileBytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                jsonString = "{ \"contents\": [{ \"parts\": [ {\"text\": \"" + message + "\"}, " +
                        "{\"inline_data\": {\"mime_type\": \"" + file.getContentType() + "\", \"data\": \"" + base64Image + "\"}}]}]}";
            } else {
                jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            }
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

    @Override
    public ResponseBody chatWithGeminiTextWithPrompt(String prompt, String message) {
        try{
            String result;
            if(prompt!=null){
                Prompt promptText = promptRepository.findPromptByPromptName(prompt)
                        .orElseThrow(()->new RequestNotFoundException("ERROR"));
                result = promptText.getPromptText() + message;
            } else {
                result = message;
            }
            String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + result + "\"}]}]}";

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
                //Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                GeminiResponse response1 = objectMapper.readValue(response.getBody(), GeminiResponse.class);
                return new ResponseBody(
                        response1.getCandidates().get(0)
                                .getContent().getParts().get(0).getText(),
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.warn("Request gemini not found");
            e.printStackTrace();
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }


    @Override
    public Prompt getByPromptName(String promptName) {
        try{
            return promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
        }catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public Prompt createPrompt(String promptName, String promptText) {
        try{
            Prompt prompt = new Prompt();
            prompt.setPromptName(promptName);
            prompt.setPromptText(promptText);
            prompt = promptRepository.save(prompt);
            return prompt;
        }catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public Prompt updatePrompt(String promptName, String newPrompt, String promptText) {
        try{
            Prompt prompt = promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            prompt.setPromptName(newPrompt!=null ? newPrompt : prompt.getPromptName());
            prompt.setPromptText(promptText!=null ? promptText : prompt.getPromptText());
            prompt = promptRepository.save(prompt);
            return prompt;
        }catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public ResponseBody deletePrompt(String promptName) {
        try{
            Prompt prompt = promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            promptRepository.delete(prompt);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }
}
