package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.entities.enums.ErrorType;
import com.example.ecommerce.exceptions.BaseException;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.payload.object.ObjectChangedActiveResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.object.UpdateObjectsResponse;
import com.example.ecommerce.rabbit.producer.ProductProducer;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.IShopCategoryService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    private final ProductProducer productProducer;
    @Override
    public ResponseBody createCategory(CategoryDto dto) {
        Shop currentShop = handleGetCurrentShop();

        ShopCategory category = new ShopCategory();
        category.setCategoryType(CategoryType.SHOP);
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setActive(false);
        category.setShopOfCategory(currentShop);
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
        Shop currentShop = handleGetCurrentShop();
        ShopCategory shopCategory = handleGetShopCategory(id);
        Product product= handleGetProduct(productId,currentShop.getId());
        if(handleCheckProductExistsInShopCategory(id,productId)){
            throw new BaseException(ErrorType.PRODUCT_EXISTS_IN_CATEGORY.toString());
        }
        ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
        shopCategoryProduct.setProduct(product);
        shopCategoryProduct.setShopCategory(shopCategory);
        shopCategoryProduct.setActive(true);
        shopCategoryProductRepository.save(shopCategoryProduct);

        UpdateObjectsResponse updateObjectsResponse = new UpdateObjectsResponse();
        updateObjectsResponse.setUpdatedObjectId(shopCategory.getId());
        updateObjectsResponse.setAfterUpdatedAmount(shopCategory.getShopCategoryProducts().size());

        //elk
        ProductDocument productDocument = ProductMapper.mapperProductToDocument(product);
        handleProductProducer(productDocument);

        return new ResponseBody(
                updateObjectsResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    @Transactional
    public ResponseBody removeProduct(Long id, Long productId) {
        Shop currentShop = handleGetCurrentShop();
        ShopCategory shopCategory = handleGetShopCategory(id);
        Product product= handleGetProduct(productId,currentShop.getId());
        if(!handleCheckProductExistsInShopCategory(id,productId)){
            throw new BaseException(ErrorType.PRODUCT_DONT_EXISTS_IN_CATEGORY.toString());
        }

        ShopCategoryProduct shopCategoryProduct = shopCategoryProductRepository.findShopCategoryProductByProductIdAndShopCategoryId(product.getId(), shopCategory.getId());
        shopCategoryProduct.setActive(false);
        shopCategoryProductRepository.save(shopCategoryProduct);

        UpdateObjectsResponse updateObjectsResponse = new UpdateObjectsResponse();
        updateObjectsResponse.setUpdatedObjectId(shopCategory.getId());
        updateObjectsResponse.setAfterUpdatedAmount(shopCategory.getShopCategoryProducts().size());

        //elk
//        handleProductProducer(productId);

        return new ResponseBody(
                updateObjectsResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody getListCategories(Long shopId,Long parentId, Integer level) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        List<ShopCategory> categories = new ArrayList<>();
        if(ObjectUtils.isEmpty(parentId)){
             categories = shopCategoryRepository.findShopCategoriesByShopOfCategoryIdAndCategoryLevel(shopId,level);

        }else{
            ShopCategory parent = shopCategoryRepository.findById(parentId).orElseThrow(
                    () -> new RequestNotFoundException("Category parent not found")
            );
            categories = shopCategoryRepository.findShopCategoriesByParentCategoryIdAndShopOfCategoryIdAndCategoryLevel(parentId,shopId,level);

        }
        for(ShopCategory category : categories){
            CategoryResponse response = new CategoryResponse();
            response.setCategoryId(category.getId());
            response.setCategoryName(category.getCategoryName());
            categoryResponses.add(response);
        }
        return new ResponseBody(categoryResponses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    //helper function...
    public Shop handleGetCurrentShop(){
        Users users = usersRepository.findById(authenticationUtils.getUserIdFromAuthentication()).orElseThrow(
                () -> new RequestNotFoundException("User not found"));
        return shopRepository.findById(users.getShopOfUsers().getId()).orElseThrow(
                () -> new RequestNotFoundException("Shop not found")
        );
    }
    public ShopCategory handleGetShopCategory(Long id){
        return shopCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorType.CATEGORY_NOT_FOUND.toString())
                );
    }
    public Product handleGetProduct(Long id,Long shopId){
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new BaseException(ErrorType.PRODUCT_NOT_FOUND.toString())
                );

        if(!product.getShopOfProduct().getId().equals(shopId)){
            throw new BaseException(ErrorType.PRODUCT_NOT_FOUND.toString());
        }
        return product;
    }

    public boolean handleCheckProductExistsInShopCategory(Long id, Long productId){
        return shopCategoryProductRepository.existsByProductIdAndShopCategoryId(productId,id);
    }

    public void handleProductProducer(ProductDocument productDocument){
        CompletableFuture.runAsync(() -> {
            try {
                productProducer.sendProductToQueue((productDocument));
            } catch (Exception e) {
                log.error("Error updating Elasticsearch for product {}: {}", productDocument.getId(), e.getMessage());

            }
        });
    }

}
