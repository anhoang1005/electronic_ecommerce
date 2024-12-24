package com.example.ecommerce.service.implement;

import com.example.ecommerce.config.Constant;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.shop.ShopMapper;
import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.models.users.UserCard;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.ShopService;
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

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class IShopService implements ShopService {
    private final AuthenticationUtils authUtils;
    private final ShopRepository shopRepository;
    private final UsersRepository usersRepository;
    private final FileService fileService;
    private final ShopMapper shopMapper;
    private final AccountService accountService;

    @Override
    public ResponseBody customerGetTheirShopDetail() {
        try{
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            return new ResponseBody(shopMapper.entityToShopDetailDto(shop), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get shop detail failed! Error {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
    @Override
    public ResponseBody getShopDetailByCode(String shopCode) {
        Shop shop = shopRepository.findShopByShopCode(shopCode);
        if (shop == null) {
            throw new RequestNotFoundException("Shop not found with code: " + shopCode);
        }
        return new ResponseBody(shopMapper.entityToShopDetailDto(shop), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    @Override
    @Transactional
    public ResponseBody customerSubscribeShop(SubscribeShopRequest req, MultipartFile frontCard, MultipartFile backCard) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users user = usersRepository.findUsersByUserCode(userCode);
            if(user.getShopOfUsers()!=null){
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "SHOP_EXISTED", ResponseBody.Code.SUCCESS);
            }
            Shop shop = mapRequestToShop(req, new Shop());
            shop.setUsersInShop(user);
            if(frontCard==null || backCard == null){
                shop.setFrontIdentityCard(null);
                shop.setBackIdentityCard(null);
            } else{
                uploadShopImages(shop, frontCard, backCard);
            }
            shop = shopRepository.save(shop);
            accountService.changeRoleUsers(user.getUserCode(), "CHUCUAHANG", true);
            log.info("Subscribe shop {} success!", shop.getShopCode());
            return new ResponseBody(shopMapper.entityToShopDetailDto(shop), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Subscribe shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody shopOwnerUpdateTheirShopInfo(SubscribeShopRequest req) {
        try {
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            updateShopInfo(req, shop);
            shopRepository.save(shop);
            log.info("Update shop {} success!", shop.getShopCode());
            return new ResponseBody(shopMapper.entityToShopDetailDto(shop), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Update shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }


    @Override
    public ResponseBody shopOwnerRemoveTheirShop() {
        try {
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode);
            Shop shop = users.getShopOfUsers();
            shop.setActive(false);
            shopRepository.save(shop);
            accountService.changeRoleUsers(users.getUserCode(), "CHUCUAHANG", false);
            log.info("{} removed shop {} success!", userCode, shop.getShopCode());
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Remove shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerRestoreTheirShop() {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode);
            Shop shop = users.getShopOfUsers();
            shop.setActive(true);
            shopRepository.save(shop);
            accountService.changeRoleUsers(users.getUserCode(), "CHUCUAHANG", true);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Restore shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody adminChangeActiveShop(String shopCode, Boolean enable) {
        try {
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            shop.setEnable(enable);
            shopRepository.save(shop);
            accountService.changeRoleUsers(shop.getUsersInShop().getUserCode(), "CHUCUAHANG", false);
            log.info("Admin lock shop {} success!", shop.getShopCode());
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Admin lock shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerFollowAndUnfollowShop(String shopCode, boolean isFollow) {
        try {
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode);
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            if(users == null || shop == null){
                throw new RequestNotFoundException("ERROR");
            }
            if (isFollow) {
                if (!users.getListShopUserFollow().contains(shop)) {
                    users.getListShopUserFollow().add(shop);
                    shop.setFollowCount(shop.getFollowCount() + 1);
                }
            } else {
                if (users.getListShopUserFollow().contains(shop)) {
                    users.getListShopUserFollow().remove(shop);
                    shop.setFollowCount(shop.getFollowCount() - 1);
                }
            }
            shopRepository.save(shop);
            usersRepository.save(users);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Users follow shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody checkUsersFollowShop(String shopCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("user not found!"));
            Boolean checkFollow = shopRepository.checkUserFollow(users, shopCode);
            return new ResponseBody(checkFollow, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("check users follow shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody getListUsersFollowShop(String shopCode, int pageNumber, int pageSize) {
        try{
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id")));
            Page<Users> page = shopRepository.getUserFollowShop(shopCode, pageable);
            PageData pageData = PageData.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .data(page.stream().map(users -> {
                        return UserCard.builder()
                                .userCode(users.getUserCode())
                                .userName(users.getUsername())
                                .userAvatar(users.getImageUrl())
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("get list users follow shop failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    private void updateShopInfo(SubscribeShopRequest req, Shop shop) {
        shop.setBusinessType(req.getBusinessType());
        shop.setShopName(req.getShopName());
        shop.setShopLogo(Constant.SHOP_IMAGE);
        shop.setShopHotLine(req.getShopHotLine());
        shop.setFullName(req.getFullName());
        shop.setTaxCode(req.getTaxCode());
        shop.setAddress(req.getAddress());
        shop.setShopEmail(req.getShopEmail());
        shop.setDescription(req.getDescription());
        shop.setIdentityCode(req.getIdentityCode());
        shop.setWardId(req.getWardId());
        shop.setDistrictId(req.getDistrictId());
        shop.setProvinceId(req.getProvinceId());
    }

    private Shop mapRequestToShop(SubscribeShopRequest req, Shop shop) {
        updateShopInfo(req, shop);
        return shop;
    }

    private void uploadShopImages(Shop shop, MultipartFile frontCard, MultipartFile backCard) {
        try {
            CompletableFuture<String> frontImageFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return fileService.uploadToCloudinary(frontCard).getData().toString();
                } catch (Exception e) {
                    log.error("Upload front card failed! Error: " + e.getMessage());
                    throw new RuntimeException("Failed to upload front card");
                }
            });

            CompletableFuture<String> backImageFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return fileService.uploadToCloudinary(backCard).getData().toString();
                } catch (Exception e) {
                    log.error("Upload back card failed! Error: " + e.getMessage());
                    throw new RuntimeException("Failed to upload back card");
                }
            });
            shop.setFrontIdentityCard(frontImageFuture.join());
            shop.setBackIdentityCard(backImageFuture.join());

        } catch (Exception e) {
            log.error("Upload images failed! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to upload images");
        }
    }
}
