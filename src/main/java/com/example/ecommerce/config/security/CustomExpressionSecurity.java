package com.example.ecommerce.config.security;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.implement.IProductService;
import com.example.ecommerce.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("customExpressionSecurity")
@RequiredArgsConstructor
@SuppressWarnings("unused")
@Slf4j
public class CustomExpressionSecurity {
    private final JwtTokenUtils jwtTokenUtils;
    private final ProductRepository productRepository;
    public boolean isShopAllowed(Long productId){
        String token = jwtTokenUtils.getToken();
        Long userId = jwtTokenUtils.extractUserId(token);
        Product product = productRepository.findProductById(productId);
        log.info(userId.toString());
        log.info(product.getShopOfProduct().getUsersInShop().getId().toString());
        return userId.equals(product.getShopOfProduct().getUsersInShop().getId());
    }
}
