package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;

import java.util.List;

public interface CartService {

    ResponseBody customerGetCartByUser();

    ResponseBody customerInsertCartItems(Long productId, Integer quantity);

    ResponseBody customerUpdateCartItemsQuantity(Long itemId, Integer quantity);

    ResponseBody customerDeleteCartItems(List<Long> productId);

    ResponseBody customerClearCart();

    Integer getCartCount();
}
