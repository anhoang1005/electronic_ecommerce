package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.entities.enums.ErrorType;
import com.example.ecommerce.exceptions.BaseException;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.payload.object.ObjectChangedActiveResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.object.UpdateObjectsResponse;
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
    private final UsersRepository usersRepository;
    @Override
    public ResponseBody createCategory(CategoryDto dto, Long shopId) {
        Shop shop = shopRepository.findShopById(shopId);
        ShopCategory category = new ShopCategory();
        category.setCategoryType(CategoryType.SHOP);

        if(ObjectUtils.isEmpty(shop)){
            throw new RequestNotFoundException("Shop havent exists");
        }
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setActive(false);
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
    public ResponseBody unActiveCategory(Long id) {
        Users users = usersRepository.findUsersById(authenticationUtils.getUserIdFromAuthentication());
        Shop shop = users.getShopOfUsers();

        ShopCategory shopCategory = shopCategoryRepository.findById(id).orElseThrow(
                () -> new RequestNotFoundException(ErrorType.CATEGORY_NOT_FOUND.toString())
        );
        if(!shopCategory.getActive()){
            shopCategory.setActive(false);
            List<ShopCategoryProduct> shopCategoryProducts = shopCategory.getShopCategoryProducts().stream().toList();
            for(ShopCategoryProduct scp : shopCategoryProducts){
                scp.setActive(false);
            }
            shopCategoryRepository.save(shopCategory);


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
    public ResponseBody addProduct(Long id,Long productId) {
        Users users = usersRepository.findUsersById(authenticationUtils.getUserIdFromAuthentication());
        Shop shop = users.getShopOfUsers();
//        authenticationUtils.globalCheckOwnerObjectItemByUserId(shop.getUsersInShop().getId(),
//                rolesRepository.findRolesByRoleName(BaseRole.CHUCUAHANG.toString()));

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
    public ResponseBody removeProduct(Long id, Long productId) {
        Users users = usersRepository.findUsersById(authenticationUtils.getUserIdFromAuthentication());
        Shop shop = users.getShopOfUsers();

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
