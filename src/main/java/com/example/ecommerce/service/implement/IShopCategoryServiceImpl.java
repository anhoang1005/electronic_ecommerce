package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
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
import com.example.ecommerce.payload.UpdateObjectsResponse;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.IShopCategoryService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final RolesRepository rolesRepository;
    private final ProductRepository productRepository;
    private final ShopCategoryProductRepository shopCategoryProductRepository;
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
        category.setShopOfCategory(shop);
        category.setShopOfCategory(shop);
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

            List<ShopCategoryProduct> shopCategoryProducts = shop.getShopCategoryProducts().stream().toList();
            for(ShopCategoryProduct scp : shopCategoryProducts){
                scp.setActive(false);
            }

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
    @Transactional
    public ResponseBody addProduct(Long id, Long shopId, Long productId) {
        Shop shop = shopRepository.findShopById(shopId);
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));

        ShopCategory shopCategory = shopCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorType.CATEGORY_NOT_FOUND.toString())
                );

        Product product = productRepository.findById(productId)
                        .orElseThrow(
                                () -> new RequestNotFoundException(ErrorType.PRODUCT_NOT_FOUND.toString())
                        );
        ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
        shopCategoryProduct.setProduct(product);
        shopCategoryProduct.setShopCategory(shopCategory);
        shopCategoryProduct.setActive(true);
        shopCategoryProductRepository.save(shopCategoryProduct);

        UpdateObjectsResponse updateObjectsResponse = new UpdateObjectsResponse();
        updateObjectsResponse.setUpdatedObjectId(shopCategory.getId());
        updateObjectsResponse.setAfterUpdatedAmount(shopCategory.getShopCategoryProducts().size());
        return new ResponseBody(
                updateObjectsResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    @Transactional
    public ResponseBody removeProduct(Long id, Long shopId, Long productId) {
        Shop shop = shopRepository.findShopById(shopId);
        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));

        ShopCategory shopCategory = shopCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorType.CATEGORY_NOT_FOUND.toString())
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorType.PRODUCT_NOT_FOUND.toString())
                );

        ShopCategoryProduct shopCategoryProduct = shopCategoryProductRepository.findShopCategoryProductByProductIdAndShopCategoryId(product.getId(), shopCategory.getId());
        shopCategoryProduct.setActive(false);
        shopCategoryProductRepository.save(shopCategoryProduct);

        UpdateObjectsResponse updateObjectsResponse = new UpdateObjectsResponse();
        updateObjectsResponse.setUpdatedObjectId(shopCategory.getId());
        updateObjectsResponse.setAfterUpdatedAmount(shopCategory.getShopCategoryProducts().size());
        return new ResponseBody(
                updateObjectsResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }
}
