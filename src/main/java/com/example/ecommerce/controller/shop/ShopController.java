package com.example.ecommerce.controller.shop;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.service.ShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/api/customer/shop/subscribe")
    public ResponseEntity<?> customerSubscribeShopApi(
            @RequestParam("subscribe_body") String jsonData,
            @RequestParam("front_card")MultipartFile frontCard,
            @RequestParam("back_card")MultipartFile backCard){
        SubscribeShopRequest req = null;
        try{
            req = objectMapper.readValue(jsonData, SubscribeShopRequest.class);
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
        return ResponseEntity.ok(shopService.customerSubscribeShop(req, frontCard, backCard));
    }

    @GetMapping("/api/customer/shop/get-detail")
    public ResponseEntity<?> shopOwnerGetTheirShopDetailApi(){
        return ResponseEntity.ok(shopService.customerGetTheirShopDetail());
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

    @DeleteMapping("/api/customer/shop/restore")
    public ResponseEntity<?> customerRemoveShopApi(){
        return ResponseEntity.ok(shopService.customerRestoreTheirShop());
    }

    @DeleteMapping("/api/admin/shop/lock")
    public ResponseEntity<?> customerSubscribeShopApi(
            @RequestParam("shop_code") String shopCode,
            @RequestParam("enable") Boolean enable){
        return ResponseEntity.ok(shopService.adminChangeActiveShop(shopCode, enable));
    }

    @PostMapping("/api/customer/shop/follow-shop")
    public ResponseEntity<?> customerFollowShopApi(
            @RequestParam("shop_code") String shopCode,
            @RequestParam("is_follow") boolean isFollow){
        return ResponseEntity.ok(shopService.customerFollowAndUnfollowShop(shopCode, isFollow));
    }

}
