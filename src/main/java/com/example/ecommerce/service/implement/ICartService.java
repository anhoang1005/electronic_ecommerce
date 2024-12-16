package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.Variant;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.cart.CartItemsDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.VariantRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ICartService implements CartService {
    private final String CART_KEY_PREFIX = "cart:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, Long, Integer> hashOperations;
    private final AuthenticationUtils authUtils;
    private final VariantRepository variantRepository;

    public ICartService(RedisTemplate<String, Object> redisTemplate,
                        AuthenticationUtils authUtils,
                        VariantRepository variantRepository) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.authUtils = authUtils;
        this.variantRepository = variantRepository;
    }

    @Override
    public ResponseBody customerGetCartByUser() {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            Map<Long, Integer> cartItems = hashOperations.entries(cartKey);
            List<CartItemsDto> cartItemsDto = new ArrayList<>();
            cartItems.forEach((variantId, quantity) -> {
                Variant variant = variantRepository.findById(variantId)
                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
                Product product = variant.getProduct();
                CartItemsDto dto = CartItemsDto.builder()
                        .productId(product.getId())
                        .variantId(variantId)
                        .productImage(product.getImageList())
                        .title(product.getTitle())
                        .variant(null)
                        .sellPrice(variant.getPrice())
                        .quantity(quantity)
                        .totalAmount(variant.getPrice().multiply(BigDecimal.valueOf(quantity)))
                        .build();
                cartItemsDto.add(dto);
            });
            return new ResponseBody(cartItemsDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("get item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerInsertCartItems(Long productId, Integer quantity) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            if (hashOperations.hasKey(cartKey, productId)) {
                Integer value = hashOperations.get(cartKey, productId);
                if(value==null) value = 0;
                hashOperations.put(cartKey, productId, value + quantity);
            } else {
                hashOperations.put(cartKey, productId, quantity);
            }
            redisTemplate.expire(cartKey, Duration.ofDays(3));
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            e.printStackTrace();
            log.error("save item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerUpdateCartItemsQuantity(Long productId, Integer quantity) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            if (hashOperations.hasKey(cartKey, productId)) {
                hashOperations.put(cartKey, productId, quantity);
                redisTemplate.expire(cartKey, Duration.ofDays(3));
            } else {
                throw new RequestNotFoundException("ERROR");
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("update item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerDeleteCartItems(List<Long> productId) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            for(Long id : productId){
                hashOperations.delete(cartKey, id);
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("remove item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerClearCart() {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            redisTemplate.delete(cartKey);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("delete cart failed! " + e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public Integer getCartCount() {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            Map<Long, Integer> cartItems = hashOperations.entries(cartKey);
            if(!cartItems.isEmpty()){
                return cartItems.size();
            } else {
                return 0;
            }
        } catch (Exception e){
            log.error("get item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
