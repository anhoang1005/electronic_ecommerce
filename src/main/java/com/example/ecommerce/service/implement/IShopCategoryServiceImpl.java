package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.ShopCategory;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.models.category.CategoryDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopCategoryRepository;
import com.example.ecommerce.repository.ShopRepository;
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
    @Override
    public ResponseBody createCategory(CategoryDto dto) {
        List<String> roles = authenticationUtils.getRoleFromAuthentication();
        if(roles.isEmpty()){
            throw new UnauthorizedException("Sth wrong");
        }
        log.info(roles.toString());
        ShopCategory category = new ShopCategory();
        category.setCategoryType(CategoryType.SHOP);
        if(!ObjectUtils.isEmpty(dto.getShopId())){
            Shop shop = shopRepository.findShopById(dto.getShopId());
            if(ObjectUtils.isEmpty(shop)){
                throw new RequestNotFoundException("Shop havent exists");
            }
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
        //Todo : thieu response
        return null;

    }
}
