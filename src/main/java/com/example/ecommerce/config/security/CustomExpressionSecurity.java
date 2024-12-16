package com.example.ecommerce.config.security;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ShopRepository;
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
    private final ShopRepository shopRepository;
    public boolean isShopAllowed(String productCode){
        String token = jwtTokenUtils.getToken();
        Long userId = jwtTokenUtils.extractUserId(token);
        log.info(productCode);
        Product product = productRepository.findProductByProductCode(productCode);
        return userId.equals(product.getShop().getUsersInShop().getId());
    }
}
