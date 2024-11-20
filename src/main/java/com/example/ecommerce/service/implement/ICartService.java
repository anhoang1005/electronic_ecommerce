package com.example.ecommerce.service.implement;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.cart.CartItemsDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private final ProductRepository productRepository;

    public ICartService(RedisTemplate<String,
                        Object> redisTemplate,
                        AuthenticationUtils authUtils,
                        ProductRepository productRepository) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.authUtils = authUtils;
        this.productRepository = productRepository;
    }

    @Override
    public ResponseBody customerGetCartByUser() {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String cartKey = CART_KEY_PREFIX + userCode;
            Map<Long, Integer> cartItems = hashOperations.entries(cartKey);
            List<CartItemsDto> cartItemsDto = new ArrayList<>();
            cartItems.forEach((productId, quantity) -> {
                    CartItemsDto dto = CartItemsDto.builder()
                            .productId(productId)
                            .quantity(quantity)
                            .build();
                    cartItemsDto.add(dto);
//                ProductVariants variants variants = productRepository.findById(productId)
//                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
//                if(va.isActive()){
//                    cartItemsDto.add(CartItemsDto.builder()
//                            .title(variants.getTitle())
//                            .thumbnail(variants.getThumbnail())
//                            .productId(variants.getId())
//                            .quantity(quantity)
//                            .sellPrice(variants.getSellPrice())
//                            .build());
//                }
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
            return cartItems.size();
        } catch (Exception e){
            log.error("get item to cart failed! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
