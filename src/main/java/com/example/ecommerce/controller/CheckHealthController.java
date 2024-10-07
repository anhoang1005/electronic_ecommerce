package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckHealthController {

    @GetMapping("/api/guest/check-health")
    public String checkHealth(){
        return "Success";
    }
}
