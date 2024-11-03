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
    @PostMapping("/shops")
    public String fakerShop(){
        fakeService.generateAndInsertFakeShopDate();
        return "done";
    }
    @PostMapping("/roots")
    public String createRoot(){
        fakeService.createRootAccount();
        return "done";
    }
    @PostMapping("/shop-categories")
    public String fakerShopCategory(){
        fakeService.generateAndInserFakeShopCategoryData();
        return "done";
    }

    @PostMapping("/attributes")
    public String fakerAttributes(){
        fakeService.generateAndInsertFakeAttributeData();
        return "done";
    }
    @PostMapping("/brands")
    public String fakerBrand(){
        fakeService.generateAndInsertFakeBrand();
        return "done";
    }
    @PostMapping("/products")
    public String fakeProduct(){
        fakeService.generateAndInsertFakeProductData();
        return "done";
    }
}
