package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.shop.ShopMapper;
import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.ShopService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@Service
public class IShopService implements ShopService {
    private final AuthenticationUtils authUtils;
    private final ShopRepository shopRepository;
    private final UsersRepository usersRepository;
    private final FileService fileService;
    private final ShopMapper shopMapper;

    @Override
    public ResponseBody customerGetTheirShopDetail() {
        return null;
    }

    @Override
    public ResponseBody customerSubscribeShop(SubscribeShopRequest req, MultipartFile frontCard, MultipartFile backCard) {
        try{
            String email = authUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            Shop shop = new Shop();
            shop.setBusinessType(req.getBusinessType());
            shop.setShopName(req.getShopName());
            shop.setShopLogo("shop.png");
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
            shop.setUsersInShop(users);
            ResponseBody frontResponse = fileService.uploadToCloudinary(frontCard);
            ResponseBody backResponse = fileService.uploadToCloudinary(backCard);
            shop.setFrontIdentityCard(frontResponse.getData().toString());
            shop.setBackIdentityCard(backResponse.getData().toString());
            shop.setActive(true);
            shop.setRating(0.0);
            shop.setProductQuantity(0);
            shop.setFollowCount(0);
            shop.setWaitingOrder(0);
            shop.setProcessOrder(0);
            shop.setDeliveringOrder(0);
            shop.setSuccessOrder(0);
            shop.setCancelOrder(0);
            shop.setReturnOrder(0);
            shop = shopRepository.save(shop);
            log.info("Subscribe shop success");
            return new ResponseBody(shopMapper.entityToShopDetailDto(shop),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.info("Subscribe shop failed!" + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody customerUpdateTheirShopInfo(SubscribeShopRequest req) {
        return null;
    }

    @Override
    public ResponseBody customerRemoveTheirShop(String shopCode) {
        return null;
    }

    @Override
    public ResponseBody adminAcceptSubscribeShop(String shopCode) {
        return null;
    }

    @Override
    public ResponseBody adminChangeActiveShop(String shopCode, Boolean active) {
        return null;
    }
}
