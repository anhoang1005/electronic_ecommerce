package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.ShopReview;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.shop_review.ShopReviewMapper;
import com.example.ecommerce.models.shop_review.ShopReviewDto;
import com.example.ecommerce.models.shop_review.ShopReviewFilter;
import com.example.ecommerce.models.shop_review.ShopReviewRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.ShopReviewRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.ShopReviewService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IShopReviewService implements ShopReviewService {
    private final ShopReviewRepository shopReviewRepository;
    private final AuthenticationUtils authUtils;
    private final ShopReviewMapper shopReviewMapper;
    private final ShopRepository shopRepository;
    private final UsersRepository usersRepository;


    @Override
    public ResponseBody customerGetPageReviewOfShop(ShopReviewFilter filter) {
        try{
            Sort sortValue;
            if(filter.getSort() == 0){
                sortValue = Sort.by(Sort.Order.asc("createdAt"));
            } else{
                sortValue = Sort.by(Sort.Order.desc("createdAt"));
            }
            if(filter.getRating() == 0 || filter.getRating() == null){ filter.setRating(null);}
            Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, 15, sortValue);
            Page<ShopReview> listPage = shopReviewRepository
                    .customerGetListShopReviewOfShop(filter.getShopCode(), filter.getRating(), pageable);
            List<ShopReviewDto> listDtos = listPage.stream()
                    .map(shopReviewMapper::entityToShopReviewDto)
                    .collect(Collectors.toList());
            return new ResponseBody(listDtos, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Fet review error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerGetTheirReviewOfShop(String shopCode) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            Optional<ShopReview> srOptional = shopReviewRepository.findShopReviewByUserEmailAndShopCode(email, shopCode);
            ShopReviewDto reviewDto = srOptional.map(shopReviewMapper::entityToShopReviewDto)
                    .orElse(null);
            if(reviewDto!=null){
                return new ResponseBody(reviewDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                return new ResponseBody(null, ResponseBody.Status.SUCCESS, "REVIEW_NOT_EXISTED", ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.error("Customer get their review error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerCreateReview(ShopReviewRequest req) {
        try{
            String email = authUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            Shop shop = shopRepository.findShopByShopCode(req.getShopCode());
            if (shop == null || users == null) {
                throw new RequestNotFoundException("ERROR");
            }
            ShopReview review = new ShopReview();
            review.setShopOfShopReview(shop);
            review.setUsersOfShopReview(users);
            review.setRating(req.getRating());
            review.setComment(req.getComment());
            review.setActive(true);
            review.setIsReply(false);
            review = shopReviewRepository.save(review);
            shop.setReviewCount(shop.getReviewCount() + 1);
            shop.setTotalReview(shop.getTotalReview() + req.getRating());
            shop.setRating((double)shop.getTotalReview() / shop.getReviewCount());
            shopRepository.save(shop);
            return new ResponseBody(shopReviewMapper.entityToShopReviewDto(review),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create shop review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerUpdateReview(ShopReviewRequest req) {
        try{
            String email = authUtils.getEmailFromAuthentication();
            Optional<ShopReview> srOptional = shopReviewRepository.findShopReviewByUserEmailAndShopCode(email, req.getShopCode());
            if(srOptional.isPresent()){
                ShopReview review = srOptional.get();
                Shop shop = review.getShopOfShopReview();
                Integer oldRating = review.getRating();
                review.setRating(req.getRating());
                review.setComment(req.getComment());
                shop.setTotalReview(shop.getTotalReview() - oldRating + req.getRating());
                if (shop.getReviewCount() > 0) {
                    shop.setRating((double)shop.getTotalReview() / shop.getReviewCount());
                } else {
                    shop.setRating(0.0);
                }
                review = shopReviewRepository.save(review);
                shopRepository.save(shop);
                return new ResponseBody(shopReviewMapper.entityToShopReviewDto(review),
                        ResponseBody.Status.SUCCESS,
                        ResponseBody.Code.SUCCESS);
            } else{
                throw new RequestNotFoundException("ERROR");
            }
        } catch (Exception e){
            log.error("Update shop review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }


    @Override
    @Transactional
    public ResponseBody customerDeleteReview(String shopCode) {
        try {
            String email = authUtils.getEmailFromAuthentication();
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            Optional<ShopReview> srOptional = shopReviewRepository.findShopReviewByUserEmailAndShopCode(email, shopCode);
            if(srOptional.isPresent()){
                ShopReview review = srOptional.get();
                shop.setReviewCount(shop.getReviewCount() - 1);
                shop.setTotalReview(shop.getTotalReview() - review.getRating());
                if (shop.getReviewCount() > 0) {
                    shop.setRating((double)shop.getTotalReview() / shop.getReviewCount());
                } else {
                    shop.setRating(0.0);
                }
                shopReviewRepository.delete(review);
                shopRepository.save(shop);
                return new ResponseBody("",
                        ResponseBody.Status.SUCCESS,
                        ResponseBody.Code.SUCCESS);
            } else{
                throw new RequestNotFoundException("ERROR");
            }
        } catch (Exception e){
            log.error("Delete shop review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody adminRemoveReview(Long reviewId) {
        try{
            ShopReview review = shopReviewRepository.findShopReviewById(reviewId);
            if (review == null) {
                throw new RequestNotFoundException("ERROR");
            }
            Shop shop = review.getShopOfShopReview();
            shop.setReviewCount(shop.getReviewCount() - 1);
            shop.setTotalReview(shop.getTotalReview() - review.getRating());
            if (shop.getReviewCount() > 0) {
                shop.setRating((double)shop.getTotalReview() / shop.getReviewCount());
            } else {
                shop.setRating(0.0);
            }
            shopReviewRepository.delete(review);
            shopRepository.save(shop);
            return new ResponseBody("",
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Admin delete shop review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody ownerShopReplyShopReview(String userCode, String reply) {
        try{
            String email = authUtils.getEmailFromAuthentication();
            Optional<ShopReview> srOptional = shopReviewRepository.ownerFindReviewByUsersCodeAndOwnerShopEmail(
                    email, userCode);
            if(srOptional.isPresent()){
                ShopReview review = srOptional.get();
                review.setIsReply(true);
                review.setReply(reply);
                shopReviewRepository.save(review);
                return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                throw new RequestNotFoundException("ERROR");
            }
        } catch (Exception e){
            log.error("Reply shop review failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
