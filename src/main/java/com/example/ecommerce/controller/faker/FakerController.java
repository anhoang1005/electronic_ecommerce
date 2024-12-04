package com.example.ecommerce.controller.faker;

import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.faker.FakeService;
import com.example.ecommerce.service.imports.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fake")
public class FakerController {
    private final FakeService fakeService;
    private final ImportService importService;
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
    public String fakeProduct(@RequestParam("file") MultipartFile file){
        fakeService.createRootAccount();
        fakeService.generateAndInsertFakeUserData();
        importService.importCategoriesFromExcel(file);
        fakeService.generateAndInsertFakeShopDate();
        fakeService.generateAndInserFakeShopCategoryData();
        fakeService.generateAndInsertFakeAttributeData();
        fakeService.generateAndInsertFakeBrand();
        fakeService.generateAndInsertFakeProductData();
        return "done";
    }
}
