package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.ShopCategory;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.entities.enums.ErrorType;
import com.example.ecommerce.exceptions.BaseException;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.payload.ObjectChangedActiveResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.ShopCategoryRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.IShopCategoryService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IShopCategoryServiceImpl implements IShopCategoryService {
    private final ShopCategoryRepository shopCategoryRepository;
    private final ShopRepository shopRepository;
    private final RandomUtils randomUtils;
    private final AuthenticationUtils authenticationUtils;
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    @Override
    public ResponseBody createCategory(CategoryDto dto, Long shopId) {
        Shop shop = shopRepository.findShopById(shopId);
//        Long userId = usersRepository.findUsersEntitiesByEmail(authenticationUtils.getEmailFromAuthentication()).getId();
//        if(!shop.getUsersInShop().getId().equals(userId)){
//            throw new UnauthorizedException("Unauthorized!");
//        }
//
//        List<String> roles = authenticationUtils.getRoleFromAuthentication();
//        if(roles.isEmpty()){
//            throw new UnauthorizedException("Sth wrong");
//        }
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));
        ShopCategory category = new ShopCategory();
        category.setCategoryType(CategoryType.SHOP);

        if(ObjectUtils.isEmpty(shop)){
            throw new RequestNotFoundException("Shop havent exists");
        }
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setActive(false);
        if(!ObjectUtils.isEmpty(dto.getParentId())){
            ShopCategory parent = shopCategoryRepository.findShopCategoriesById(dto.getParentId());
            if(ObjectUtils.isEmpty(parent)){
                throw new RequestNotFoundException("Category parent not found");
            }
            Integer parentLevel = parent.getCategoryLevel();
            Integer childLevel = parentLevel + 1;
            category.setCategoryLevel(childLevel);
            category.setParentCategory(parent);

        }else{
            category.setCategoryLevel(0);
        }
        category.setCategoryCode("SCTGY"+randomUtils.generateRandomString(15));
        category.setDescription(dto.getDescription());

        shopCategoryRepository.save(category);

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());

        ResponseBody responseBody = new ResponseBody(
                categoryResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
        return responseBody;

    }

    @Override
    public ResponseBody unActiveCategory(Long id, Long shopId) {
        Shop shop = shopRepository.findShopById(shopId);
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));
        if(!shop.getActive()){
            shop.setActive(false);
            shopRepository.save(shop);

            ObjectChangedActiveResponse objectChangedActiveResponse = new ObjectChangedActiveResponse();
            objectChangedActiveResponse.setObjectActive(shop.getActive());
            objectChangedActiveResponse.setObjectChangedId(shop.getId());
            ResponseBody responseBody = new ResponseBody(
                    objectChangedActiveResponse,
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            );
            return responseBody;
        }
        throw new BaseException(ErrorType.SOMETHING_WRONG.toString());
    }

    @Override
    public ResponseBody addProduct(Long id, Long shopId, Long productId) {
        Shop shop = shopRepository.findShopById(shopId);
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));
        return null;
    }

    @Override
    public ResponseBody removeProduct(Long id, Long shopId, Long productId) {
        Shop shop = shopRepository.findShopById(shopId);
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));
        return null;
    }
}
