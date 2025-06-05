package com.example.ecommerce.controller.faker;

import com.example.ecommerce.service.faker.*;
import com.example.ecommerce.service.imports.ImportProduct;
import com.example.ecommerce.service.imports.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fake")
public class FakerController {
    private final FakeService fakeService;
    private final ImportService importService;
    private final FakePlusService fakePlusService;
    private final ImportProduct importProduct;
    private final FakeProductService fakeProductService;
    private final FakeVoucherService fakeVoucherService;
    private final FakerOrderService fakerOrderService;

    @PostMapping("/brands")
    public String fakerBrand(){
        fakeService.generateAndInsertFakeBrand();
        return "done";
    }

    @PostMapping("/products")
    public String fakeProduct(@RequestParam("file") MultipartFile file){
//        fakeService.createRootAccount();
//        fakeService.generateAndInsertFakeUserData();
//        importService.importCategoriesFromExcel(file);
//        fakeService.generateAndInsertFakeShopDate();
//        fakeService.generateAndInsertFakeShopCategoryData();
//        fakeService.generateAndInsertFakeAttributeData();
//        fakeService.generateAndInsertFakeBrand();
//        fakeService.generateAndInsertFakeProductData();
        fakeService.sendProductToElasticsearch();
        return "done";
    }
    @PostMapping("/orders")
    public String fakeOder(){
       fakeService.generateAndInsertOrders();
        fakeService.sendOrderShopToElasticSearch();
        fakeService.sendOrderItemShopToElasticsearch();
        return "done";
    }

    @GetMapping("/plus")
    public ResponseEntity<String> test(@RequestParam("file") MultipartFile file){
        fakePlusService.createRootAccount();
        fakePlusService.createUserAccount(150);
        fakePlusService.generateAndInsertFakeShopDate();
        importService.importCategoriesFromExcel(file);
        fakePlusService.generateAndInsertFakeShopCategoryData();
        fakePlusService.generateAndInsertFakeAttributeData();

        fakeProductService.generateAndInsertFakeBrand();
        fakeProductService.generateAndInsertFakeProductData();

        fakeVoucherService.generateAndInsertFakeVoucherData();
        fakeVoucherService.generateAndInsertFakeShopVoucherData();

//        fakerOrderService.fakeOrder();

        fakePlusService.fakeShopReview();
        fakePlusService.fakeProductReview();
        return ResponseEntity.ok("done");
    }

    @GetMapping("/import")
    public String fakeProductt(@RequestParam("file") MultipartFile file){
        importProduct.readProductsFromExcel(file);
        return "done";
    }
}
