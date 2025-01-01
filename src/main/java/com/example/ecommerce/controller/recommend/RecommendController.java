package com.example.ecommerce.controller.recommend;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class RecommendController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;

    @GetMapping("/api/guest/recommend/content-based")
    public ResponseEntity<?> contentBase(@RequestParam("product_id") Long productId,
                                         @RequestParam("count") int count) {
        String url = "http://localhost:8080/recommend-content-based?product_id=" +
                productId + "&count=" + count;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                List<Long> resultList = objectMapper.readValue(response.getBody(), new TypeReference<List<Long>>() {});
                List<ProductCard> cardList = new ArrayList<>();
                for(Long id : resultList){
                    Product product = productRepository.findById(id).orElseThrow(()-> new RequestNotFoundException("ERROR"));
                    ProductCard card = productMapper.entityToProductCard(product);
                    cardList.add(card);
                }
                ResponseBody responseBody = new ResponseBody(cardList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                return ResponseEntity.ok(responseBody);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing response: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch recommendations");
    }

    @GetMapping("/api/guest/recommend/user-based")
    public ResponseEntity<?> userBased(@RequestParam("user_id") Long userId,
                                         @RequestParam("count") int count) {
        String url = "http://localhost:8080/recommend-user-based?user_id=" +
                userId + "&count=" + count;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                List<Long> resultList = objectMapper.readValue(response.getBody(), new TypeReference<List<Long>>() {});
                List<ProductCard> cardList = new ArrayList<>();
                for(Long id : resultList){
                    Product product = productRepository.findById(id).orElseThrow(()-> new RequestNotFoundException("ERROR"));
                    ProductCard card = productMapper.entityToProductCard(product);
                    cardList.add(card);
                }
                ResponseBody responseBody = new ResponseBody(cardList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                return ResponseEntity.ok(responseBody);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing response: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch recommendations");
    }

    @GetMapping("/api/guest/recommend/item-based")
    public ResponseEntity<?> itembased(@RequestParam("product_id") Long userId,
                                       @RequestParam("count") int count) {
        String url = "http://localhost:8080/recommend-item-based?product_id=" +
                userId + "&count=" + count;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                List<Long> resultList = objectMapper.readValue(response.getBody(), new TypeReference<List<Long>>() {});
                List<ProductCard> cardList = new ArrayList<>();
                for(Long id : resultList){
                    Product product = productRepository.findById(id).orElseThrow(()-> new RequestNotFoundException("ERROR"));
                    ProductCard card = productMapper.entityToProductCard(product);
                    cardList.add(card);
                }
                ResponseBody responseBody = new ResponseBody(cardList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                return ResponseEntity.ok(responseBody);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing response: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch recommendations");
    }
}
