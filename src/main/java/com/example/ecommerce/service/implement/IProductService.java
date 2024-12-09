package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.ErrorMessage;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.category.CategoryMapper;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.attribute.AttributeDTO;
import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.product.ProductUpdateDTO;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.models.variant.VariantDTO;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.object.ObjectChangedActiveResponse;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IProductService implements ProductService {
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final VariantAttributeValueRepository variantAttributeValueRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final JsonConvert jsonConvert;
    private final CategoryMapper categoryMapper;
    private final FileService fileService;
    private final CategoryProductRepository categoryProductRepository;
    private final BrandRepository brandRepository;
    private final UsersRepository usersRepository;
    private final AuthenticationUtils authenticationUtils;
    private final ShopCategoryProductRepository shopCategoryProductRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final ProductMapper productMapper;
//  private final ProductProducer productProducer;

    @Override
    @Transactional
    public ResponseBody createProduct(ProductDTO dto, List<MultipartFile> productFile) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new RequestNotFoundException("Category not found with id : " + dto.getCategoryId()));
        Brand brand = brandRepository.findById(dto.getBrandId()).orElseThrow(
                () -> new RequestNotFoundException("Brand not found"));
        Shop shop = usersRepository.findUsersById(authenticationUtils.getUserIdFromAuthentication()).getShopOfUsers();

        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setProductState(Product.ProductState.PENDING);
        product.setProductSlug(StringFormatUtils.convertToSlug(dto.getTitle()));
        product.setCurrency(dto.getCurrency());
        product.setIsHidden(dto.isHidden());
        product.setCategory(category);
        product.setBrandOfProduct(brand);
        product.setShopOfProduct(shop);
        List<BigDecimal> prices = new ArrayList<>();

        //save anh product
        CompletableFuture<List<String>> imagesUrl = fileService.uploadAndSaveImagesProductAsync(productFile);

        //Tao bien the
        for(VariantDTO variantDTO : dto.getVariantDTOS()){
            ProductVariant variant = new ProductVariant();
            BigDecimal price = variantDTO.getPrice();
            prices.add(price);
            variant.setPrice(price);
            variant.setStockQuantity(variantDTO.getQuantity());
            variant.setSku(variantDTO.getSku());
            variant.setVariantSlug(StringFormatUtils.convertToSlug(variantDTO.getSku()));
            variant.setVariantSlug("demo_");
            variant.setHeight(variantDTO.getHeight());
            variant.setWeight(variant.getWeight());
            variant.setLength(variant.getLength());
            variant.setWidth(variant.getWidth());

            StringBuilder stringBuilder = new StringBuilder();

            //Tao attribute cua bien the
            for(AttributeDTO attributeDTO : variantDTO.getAttributeDTOList()){
                Attribute attribute = attributeRepository.findAttributeByAttributeName(attributeDTO.getName());
                if(ObjectUtils.isEmpty(attribute)){
                    attribute = new Attribute();
                    attribute.setAttributeName(attributeDTO.getName());
                    attribute.setValueJson("[]");
                    attributeRepository.save(attribute);
                }

                //
                AttributeValue attributeValue = attributeValueRepository.findAttributeValueByValueAndAttributeId(attribute.getId(),attributeDTO.getValue());
                if(ObjectUtils.isEmpty(attributeValue)){
                    attributeValue = new AttributeValue();
                    attributeValue.setAttribute(attribute);
                    attributeValue.setValue(attributeDTO.getValue());
                    attributeValueRepository.save(attributeValue);
                    try {
                        List<String> valueJson = jsonConvert.convertValueJsonToList(attribute.getValueJson());
                        if (valueJson == null) {
                            valueJson = new ArrayList<>();
                        }
                        valueJson.add(attributeValue.getValue());
                        attribute.setValueJson(jsonConvert.convertListObjectToJson(valueJson));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                stringBuilder.append(attribute.getAttributeName().toLowerCase())
                        .append("=")
                        .append(attributeValue.getValue().toLowerCase())
                        .append(";");
                VariantAttributeValue variantAttributeValue = new VariantAttributeValue();
                variantAttributeValue.setAttributeValue(attributeValue);
                variantAttributeValue.setAttribute(attribute);
                variantAttributeValueRepository.save(variantAttributeValue);
                variant.getVariantAttributeValueList().add(variantAttributeValue);

                ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                productAttributeValue.setAttribute(attribute);
                productAttributeValue.setAttributeValue(attributeValue);
                productAttributeValueRepository.save(productAttributeValue);
                product.getProductAttributeValues().add(productAttributeValue);
            }
            if(stringBuilder.length() > 0) {
                stringBuilder.substring(0,stringBuilder.length()-1);
            }

            variant.setProductVariation(stringBuilder.toString());
            variantRepository.save(variant);
            CompletableFuture<String> imageUrl = fileService.uploadImageVariantAsync(variantDTO.getProductImage());
            CompletableFuture<Void> processImg = fileService.processImageAndSaveAsync(variant,imageUrl);
            //
            for(VariantAttributeValue variantAttributeValue : variant.getVariantAttributeValueList()){
                variantAttributeValue.setVariant(variant);
                variantAttributeValueRepository.save(variantAttributeValue);
            }
            product.getVariants().add(variant);
        }

        //
        for(ProductAttributeValue productAttributeValue : product.getProductAttributeValues()){
            productAttributeValue.setProduct(product);
            productAttributeValueRepository.save(productAttributeValue);
        }
        //
        for(ProductVariant variant : product.getVariants()){
            variant.setProduct(product);
            variantRepository.save(variant);
        }
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categoryResponses.add(categoryMapper.entityToCategoryResponse(category));
        Category current = category;
        while(!ObjectUtils.isEmpty(current.getParentCategory())) {
            categoryResponses.add(categoryMapper.entityToCategoryResponse(category.getParentCategory()));
            try {
                product.setBreadcrumb(jsonConvert.convertListObjectToJson(categoryResponses));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            current = current.getParentCategory();
        }
        List<ProductVariant> variants = product.getVariants();
        try {
            product.setVariations(jsonConvert.convertListObjectToJson(variants));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        product.setMaxInitialPrice(prices.stream().min(BigDecimal::compareTo).orElse(null));
        product.setMinInitialPrice(prices.stream().max(BigDecimal::compareTo).orElse(null));

        productRepository.save(product);

        for(Long id : dto.getShopCategoryIds()){
            ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
            ShopCategory shopCategory = shopCategoryRepository.findById(id).orElseThrow(
                    () -> new RequestNotFoundException(ErrorMessage.SHOP_CATEGORY_NOT_FOUND.toString())
            );
            shopCategoryProduct.setShopCategory(shopCategory);
            shopCategoryProduct.setProduct(product);
            shopCategoryProductRepository.save(shopCategoryProduct);

            product.getShopCategoryProducts().add(shopCategoryProduct);
        }
        CategoryProduct categoryProduct = new CategoryProduct();
        categoryProduct.setCategory(category);
        categoryProduct.setProduct(product);
        categoryProductRepository.save(categoryProduct);
        product.getCategoryProducts().add(categoryProduct);

        CompletableFuture<Void> processImgs = fileService.processImagesAndSaveAsync(product, imagesUrl);
        return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody findProductById(Long id) {
        Product product = productRepository.findProductByIdIfAcceptedAndNotHidden(id);
        if(ObjectUtils.isEmpty(product)){
            throw new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name());
        }
        return new ResponseBody(
                ProductMapper.mapperProductToResponse(product),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody findProductHiddenById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name())
        );
        return new ResponseBody(
                ProductMapper.mapperProductToResponse(product),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody updateProduct(Long id, ProductUpdateDTO dto) {
        return null;
    }

    @Override
    public ResponseBody checkProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name())
                );
        product.setIsAccepted(true);
        product.setIsHidden(true);
        productRepository.save(product);
//        CompletableFuture.runAsync(
//                () -> productProducer.sendProductToQueue(ProductMapper.mapperProductToDocument(product))
//        );
        return new ResponseBody(
                new ObjectChangedActiveResponse(product.getId(),product.getIsAccepted()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name())
                );
        product.setIsDeleted(true);
        productRepository.save(product);

//        CompletableFuture.runAsync(
//                () -> {
//                    ProductDocument productDocument = productDocumentRepository.findById(id)
//                            .orElseThrow(
//                                    () -> new RequestNotFoundException(ErrorMessage.PRODUCT_UNACTIVE.name())
//                            );
//
//                    productDocument.setIsDeleted(true);
//                    productDocumentRepository.save(productDocument);
//                }
//        );
        return new ResponseBody(
                new ObjectChangedActiveResponse(product.getId(),product.getIsAccepted()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody getProductHome(int pageNumber, int pageSize) {
        try{
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id")));
            Page<Product> page = productRepository.findAll(pageable);
            List<ProductCard> responses = page.stream()
                    .map(productMapper::entityToProductCard)
                    .collect(Collectors.toList());
            return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get product error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }

    @Override
    public ResponseBody getProductDetail(Long productId) {
        try{
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            return new ResponseBody(productMapper.entityToProductDetail(product), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get product error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }
}
