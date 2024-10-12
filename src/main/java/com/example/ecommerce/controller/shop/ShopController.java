package com.example.ecommerce.controller.shop;

import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/api/customer/shop/subscribe")
    public ResponseEntity<?> customerSubscribeShopApi(
            @RequestBody SubscribeShopRequest req,
            @RequestParam("front_card")MultipartFile frontCard,
            @RequestParam("back_card")MultipartFile backCard){
        return ResponseEntity.ok(shopService.customerSubscribeShop(req, frontCard, backCard));
    }

    @GetMapping("/api/owner/shop/get-detail")
    public ResponseEntity<?> shopOwnerGetTheirShopDetailApi(){
        return ResponseEntity.ok(shopService.shopOwnerGetTheirShopDetail());
    }

    @PutMapping("/api/owner/shop/update")
    public ResponseEntity<?> shopOwnerUpdateShopApi(
            @RequestBody SubscribeShopRequest req){
        return ResponseEntity.ok(shopService.shopOwnerUpdateTheirShopInfo(req));
    }

    @DeleteMapping("/api/owner/shop/remove")
    public ResponseEntity<?> shopOwnerRemoveShopApi(){
        return ResponseEntity.ok(shopService.shopOwnerRemoveTheirShop());
    }

    @DeleteMapping("/api/admin/shop/change-active")
    public ResponseEntity<?> customerSubscribeShopApi(
            @RequestParam("shop_code") String shopCode,
            @RequestParam("active") Boolean active){
        return ResponseEntity.ok(shopService.adminChangeActiveShop(shopCode, active));
    }

}
