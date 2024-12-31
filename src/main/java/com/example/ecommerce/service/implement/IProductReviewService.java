package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductReview;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.product_review.ProductReviewMapper;
import com.example.ecommerce.models.product_review.*;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductReviewRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.ProductReviewService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IProductReviewService implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final AuthenticationUtils authUtils;
    private final ProductReviewMapper productReviewMapper;
    private final FileService fileService;


    @Override
    public ResponseBody guestGetListReviewOfProduct(ProductReviewFilter filter) {
        try{
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("id")));
            Page<ProductReview> page = productReviewRepository.guestGetListReviewOfProduct(
                    filter.getProductId(), filter.getRating(), pageable);
            List<ProductReviewDto> listPReview = page.stream()
                    .map(productReviewMapper::entityToPRDto)
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listPReview)
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .pageNumber(filter.getPageNumber())
                    .pageSize(filter.getPageSize())
                    .build();
            List<Object[]> list = productReviewRepository.countReviewsByRating(filter.getProductId());
            long oneStarCount = 0, twoStarCount = 0, threeStarCount = 0, fourStarCount = 0, fiveStarCount = 0;
            for (Object[] obj : list) {
                Integer star = (Integer) obj[0];
                Long count = (Long) obj[1];

                if (star != null) {
                    switch (star) {
                        case 1 -> oneStarCount = count;
                        case 2 -> twoStarCount = count;
                        case 3 -> threeStarCount = count;
                        case 4 -> fourStarCount = count;
                        case 5 -> fiveStarCount = count;
                    }
                }
            }
            ProductReviewRating reviewRating = ProductReviewRating.builder()
                    .rating(productReviewRepository.findAverageRatingByProductId(filter.getProductId())) // Rating trung bình
                    .review_count(productReviewRepository.countByProductOfProductReview_Id(filter.getProductId())) // Tổng số review
                    .one_star_count(oneStarCount)
                    .two_star_count(twoStarCount)
                    .three_star_count(threeStarCount)
                    .four_star_count(fourStarCount)
                    .five_star_count(fiveStarCount)
                    .build();

            ProductReviewResponse response = ProductReviewResponse.builder()
                    .my_review(null)
                    .review_page(pageData)
                    .review_rating(reviewRating)
                    .build();
            return new ResponseBody(response, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            log.error("Get list review of product error! Error {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerGetListReviewOfProduct(ProductReviewFilter filter) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Optional<ProductReview> review = productReviewRepository
                    .findByUsersOfProductReview_IdAndProductOfProductReview_Id(userId, filter.getProductId());
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("id")));
            Page<ProductReview> page = productReviewRepository.guestGetListReviewOfProduct(
                    filter.getProductId(), filter.getRating(), pageable);
            List<ProductReviewDto> listPReview = page.stream()
                    .map(productReviewMapper::entityToPRDto)
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listPReview)
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .pageNumber(filter.getPageNumber())
                    .pageSize(filter.getPageSize())
                    .build();
            List<Object[]> list = productReviewRepository.countReviewsByRating(filter.getProductId());
            long oneStarCount = 0, twoStarCount = 0, threeStarCount = 0, fourStarCount = 0, fiveStarCount = 0;
            for (Object[] obj : list) {
                Integer star = (Integer) obj[0];
                Long count = (Long) obj[1];

                if (star != null) {
                    switch (star) {
                        case 1 -> oneStarCount = count;
                        case 2 -> twoStarCount = count;
                        case 3 -> threeStarCount = count;
                        case 4 -> fourStarCount = count;
                        case 5 -> fiveStarCount = count;
                    }
                }
            }
            ProductReviewRating reviewRating = ProductReviewRating.builder()
                    .rating(productReviewRepository.findAverageRatingByProductId(filter.getProductId())) // Rating trung bình
                    .review_count(productReviewRepository.countByProductOfProductReview_Id(filter.getProductId())) // Tổng số review
                    .one_star_count(oneStarCount)
                    .two_star_count(twoStarCount)
                    .three_star_count(threeStarCount)
                    .four_star_count(fourStarCount)
                    .five_star_count(fiveStarCount)
                    .build();
            ProductReviewResponse response = ProductReviewResponse.builder()
                    .my_review(review.map(productReviewMapper::entityToPRDto).orElse(null))
                    .review_page(pageData)
                    .review_rating(reviewRating)
                    .build();
            return new ResponseBody(response, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get list review of product error! Error {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerCreateProductReview(ProductReviewRequest req, MultipartFile image) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            if(productReviewRepository.existsByUsersOfProductReview_UserCodeAndProductOfProductReview_Id(
                    userCode, req.getProductId())){
                return new ResponseBody("Review existed", ResponseBody.Status.SUCCESS, ResponseBody.Code.FORBIDDEN);
            }
            Users users = usersRepository.findUsersByUserCode(userCode);
            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            ProductReview review = new ProductReview();
            review.setProductOfProductReview(product);
            review.setUsersOfProductReview(users);
            review.setRating(req.getRating());
            review.setTag(req.getTag());
            review.setTextComment(req.getTextReview());
            if(image!=null){
                ResponseBody responseBody = fileService.uploadToCloudinary(image);
                review.setImageComment(responseBody.getData().toString());
            } else{
                review.setImageComment(null);
            }
            review = productReviewRepository.save(review);
//            productRepository.findAndLockUpdateReview(
//                    req.getProductId(), 0, review.getRating(), "INSERT");
            return new ResponseBody(productReviewMapper.entityToPRDto(review), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerUpdateProductReview(ProductReviewRequest req, MultipartFile image) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            ProductReview review = productReviewRepository
                    .findByUsersOfProductReview_IdAndProductOfProductReview_Id(userId, req.getProductId())
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            Integer oldStar = review.getRating();
            review.setRating(req.getRating());
            review.setTag(req.getTag());
            review.setTextComment(req.getTextReview());
            if(image!=null){
                ResponseBody responseBody = fileService.uploadToCloudinary(image);
                review.setImageComment(responseBody.getData().toString());
            }
            review = productReviewRepository.save(review);
//            if(!review.getRating().equals(oldStar)){
//                productRepository.findAndLockUpdateReview(
//                        req.getProductId(), oldStar, review.getRating(), "UPDATE");
//            }
            return new ResponseBody(productReviewMapper.entityToPRDto(review), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Update review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerDeleteProductReview(Long productId) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            ProductReview review = productReviewRepository
                    .findByUsersOfProductReview_IdAndProductOfProductReview_Id(userId, productId)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            productReviewRepository.delete(review);
//            productRepository.findAndLockUpdateReview(
//                    productId, review.getRating(), 0, "DELETE");
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Delete review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody shopOwnerReplyProductReview(Long reviewId, String shopReply) {
        try{
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            ProductReview review = productReviewRepository
                    .getReviewOfShop(reviewId, shopCode)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            review.setIsShopReply(true);
            review.setShopReply(shopReply);
            review = productReviewRepository.save(review);
            return new ResponseBody(productReviewMapper.entityToPRDto(review), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            e.printStackTrace();
            log.error("shop reply review error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerLikeProductReview(Long reviewId, boolean like) {
        return null;
    }
}
