package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ICategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final RandomUtils randomUtils;
    private final AuthenticationUtils authenticationUtils;
    @Override
    public ResponseBody createCategory(CategoryDto dto) {
        List<String> roles = authenticationUtils.getRoleFromAuthentication();
        if(roles.isEmpty()){
            throw new UnauthorizedException("Sth wrong");
        }
        log.info(roles.toString());
        Category category = new Category();
        if(roles.contains(BaseRole.ROLE_QUANLI.toString()) || roles.contains(BaseRole.ROLE_ROOT.toString())){
            category.setCategoryType(CategoryType.SYSTEM);
            category.setActive(true);
        }else{
            category.setCategoryType(CategoryType.SHOP);
            if(!ObjectUtils.isEmpty(dto.getShopId())){
                Shop shop = shopRepository.findShopById(dto.getShopId());
                if(ObjectUtils.isEmpty(shop)){
                    throw new RequestNotFoundException("Shop havent exists");
                }
            }
        }
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setActive(false);
        if(!ObjectUtils.isEmpty(dto.getParentId())){
            Category parent = categoryRepository.findCategoryById(dto.getParentId());
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
        category.setCategoryCode("CTGY"+randomUtils.generateRandomString(15));
        category.setDescription(dto.getDescription());

        categoryRepository.save(category);
        //Todo : thieu response

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());
        return new ResponseBody(
                categoryResponse,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody findCategoryById(Long id) {
        return null;
    }

    @Override
    public ResponseBody updateCategory(Long id, CategoryDto dto) {
        return null;
    }

    @Override
    public ResponseBody unActiveCategory(Long id) {
        return null;
    }

    @Override
    public ResponseBody removeProductFromCategory(Long id, Long productId) {
        return null;
    }

    @Override
    public ResponseBody addProductToCategory(Long id, Long productId) {
        return null;
    }

    @Override
    public ResponseBody getListCategory(Long parentId, Integer level) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        if(ObjectUtils.isEmpty(parentId)){
            List<Category> categories = categoryRepository.findCategoriesByCategoryLevel(level);
            for(Category category : categories){
                CategoryResponse response = new CategoryResponse();
                response.setCategoryId(category.getId());
                response.setCategoryName(category.getCategoryName());
                categoryResponses.add(response);
            }
        }else{
            Category parent = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RequestNotFoundException("Category parent not found")
            );
            List<Category> categories = categoryRepository.findCategoriesByCategoryLevelAndParentCategory(level,parent);
            for(Category category : categories){
                CategoryResponse response = new CategoryResponse();
                response.setCategoryId(category.getId());
                response.setCategoryName(category.getCategoryName());
                categoryResponses.add(response);
            }
        }

        return new ResponseBody(categoryResponses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }
}
