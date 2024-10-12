package com.example.ecommerce.controller.faker;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.faker.FakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fake")
public class FakerController {
    private final FakeService fakeService;
    @PostMapping("/users")
    public String fakerUser(){
        fakeService.generateAndInsertFakeUserData();

        return "done";
    }
}
