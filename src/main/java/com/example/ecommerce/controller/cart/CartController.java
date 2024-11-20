package com.example.ecommerce.controller.cart;

import com.example.ecommerce.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/api/customer/cart")
    public ResponseEntity<?> customerGetCart(){
        return ResponseEntity.ok(cartService.customerGetCartByUser());
    }

    @PostMapping("/api/customer/cart")
    public ResponseEntity<?> customerInsertCartItem(@RequestParam("item_id") Long id,
                                                    @RequestParam("quantity") Integer quantity){
        return ResponseEntity.ok(cartService.customerInsertCartItems(id, quantity));
    }

    @PutMapping("/api/customer/cart")
    public ResponseEntity<?> customerUpdateCart(@RequestParam("item_id") Long id,
                                                @RequestParam("quantity") Integer quantity){
        return ResponseEntity.ok(cartService.customerUpdateCartItemsQuantity(id, quantity));
    }

    @DeleteMapping("/api/customer/cart")
    public ResponseEntity<?> customerDeleteCartItem(@RequestParam("item_list") List<Long> id){
        return ResponseEntity.ok(cartService.customerDeleteCartItems(id));
    }

    @DeleteMapping("/api/customer/cart/clear")
    public ResponseEntity<?> customerClearCart(){
        return ResponseEntity.ok(cartService.customerClearCart());
    }

    @GetMapping("/api/customer/cart/count")
    public ResponseEntity<?> customerCountCart(){
        return ResponseEntity.ok(cartService.getCartCount());
    }
}
