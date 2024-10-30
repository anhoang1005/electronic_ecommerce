package com.example.ecommerce.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonConvert {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // json -> list<String> loai bo phan tu dau cuoi
    public final List<String> extractCategories(String json) throws JsonProcessingException{
        List<String> categoryList = objectMapper.readValue(json, new TypeReference<List<String>>() {});
        if(categoryList.size() > 2){
            return categoryList.subList(1, categoryList.size() - 1);
        }
        return List.of();
    }

    public List<String> convertValueJsonToList(String valueJson) throws JsonProcessingException {
        return objectMapper.readValue(valueJson, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }
    public <T>String convertListObjectToJson(List<T> objects) throws JsonProcessingException{
        return objectMapper.writeValueAsString(objects);
    }
}
