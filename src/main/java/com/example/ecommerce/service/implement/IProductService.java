package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.ErrorMessage;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.attribute.AttributeDTO;
import com.example.ecommerce.models.category.CategoryBreadcrumb;
import com.example.ecommerce.models.product.*;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.models.variant.VariantDTO;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.payload.object.ObjectChangedActiveResponse;
import com.example.ecommerce.rabbit.ProductProducer;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;
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
    private final FileService fileService;
    private final CategoryProductRepository categoryProductRepository;
    private final BrandRepository brandRepository;
    private final AuthenticationUtils authenticationUtils;
    private final ShopCategoryProductRepository shopCategoryProductRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final ProductMapper productMapper;
    private final ShopRepository shopRepository;
    private final ProductProducer productProducer;

    @Override
    @Transactional
    public ResponseBody createProduct(ProductDTO dto, List<MultipartFile> productFile) {
        try {
            String shopCode = authenticationUtils.getUserFromAuthentication().getShopCode();
            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                    () -> new RequestNotFoundException("Category not found with id : " + dto.getCategoryId()));
            Brand brand = brandRepository.findById(dto.getBrandId()).orElseThrow(
                    () -> new RequestNotFoundException("Brand not found"));
            Shop shop = shopRepository.findShopByShopCode(shopCode);

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

            Map<String, Set<String>> productAttributeValues = new HashMap<>();

            //save anh product
            CompletableFuture<List<String>> imagesUrl =
                    fileService.uploadAndSaveImagesProductAsync(productFile);

            //Tao bien the
            for (VariantDTO variantDTO : dto.getVariantDTOS()) {
                ProductVariant variant = new ProductVariant();
                BigDecimal price = variantDTO.getPrice();
                prices.add(price);
                variant.setPrice(price);
                variant.setStockQuantity(variantDTO.getQuantity());
                variant.setSku(variantDTO.getSku());
                variant.setVariantSlug(StringFormatUtils.convertToSlug(variantDTO.getSku()));
                variant.setHeight(variantDTO.getHeight());
                variant.setWeight(variantDTO.getWeight());
                variant.setLength(variantDTO.getLength());
                variant.setWidth(variantDTO.getWidth());
                variant.setStockQuantity(variantDTO.getStockQuantity());
                StringBuilder stringBuilder = new StringBuilder();

                //Tao attribute cua bien the
                for (AttributeDTO attributeDTO : variantDTO.getAttributeDTOList()) {
                    Attribute attribute = attributeRepository.findAttributeByAttributeName(attributeDTO.getName());
                    if (ObjectUtils.isEmpty(attribute)) {
                        attribute = new Attribute();
                        attribute.setAttributeName(attributeDTO.getName());
                        attribute.setValueJson("[]");
                        attributeRepository.save(attribute);
                    }

                    //Tao gia tri cua attribute product
                    AttributeValue attributeValue = attributeValueRepository.findAttributeValueByValueAndAttributeId(attribute.getId(), attributeDTO.getValue());
                    if (ObjectUtils.isEmpty(attributeValue)) {
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
                    if (productAttributeValues.containsKey(attribute.getAttributeName())) {
                        productAttributeValues.get(attribute.getAttributeName()).add(attributeValue.getValue());
                    } else {
                        productAttributeValues.put(attribute.getAttributeName(), new HashSet<>(List.of(attributeValue.getValue())));
                    }

                }


                if (stringBuilder.length() > 0) {
                    stringBuilder.substring(0, stringBuilder.length() - 1);
                }
                variant.setStatus(ProductVariant.Status.CON_HANG);
                variant.setProductVariation(stringBuilder.toString());
                variantRepository.save(variant);
                CompletableFuture<String> imageUrl = fileService.uploadImageVariantAsync(variantDTO.getProductImage());
                CompletableFuture<Void> processImg = fileService.processImageAndSaveAsync(variant, imageUrl);

                //Tao gia tri attribute cua variant
                for (VariantAttributeValue variantAttributeValue : variant.getVariantAttributeValueList()) {
                    variantAttributeValue.setVariant(variant);
                    variantAttributeValueRepository.save(variantAttributeValue);
                }
                product.getVariants().add(variant);
            }

            //
            for (ProductAttributeValue productAttributeValue : product.getProductAttributeValues()) {
                productAttributeValue.setProduct(product);
                productAttributeValueRepository.save(productAttributeValue);
            }

            //
            for (ProductVariant variant : product.getVariants()) {
                variant.setProduct(product);
                variantRepository.save(variant);
            }

            List<CategoryBreadcrumb> categoryResponses = new ArrayList<>();
            categoryResponses.add(CategoryBreadcrumb.builder()
                    .id(category.getId())
                    .category(category.getCategoryName())
                    .build());
            Category current = category;
            while (!ObjectUtils.isEmpty(current.getParentCategory())) {
                category = category.getParentCategory();
                categoryResponses.add(CategoryBreadcrumb.builder()
                        .id(category.getId())
                        .category(category.getCategoryName())
                        .build());
                current = current.getParentCategory();
            }
            Collections.reverse(categoryResponses);
            product.setBreadcrumb(jsonConvert.convertListObjectToJson(categoryResponses));
            List<ProductVariant> variants = product.getVariants();
            try {
                product.setVariations(jsonConvert.convertListObjectToJson(variants));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            product.setMaxInitialPrice(prices.stream().min(BigDecimal::compareTo).orElse(null));
            product.setMinInitialPrice(prices.stream().max(BigDecimal::compareTo).orElse(null));

            ObjectMapper objectMapper = new ObjectMapper();
            product.setProductSpecifications(objectMapper.writeValueAsString(productAttributeValues));
            product = productRepository.save(product);
            productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));

            for (Long id : dto.getShopCategoryIds()) {
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
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("create product failed!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody shopGetDashboard(ShopProductFilter filter) {
        try {
            String shopCode = authenticationUtils.getUserFromAuthentication().getShopCode();
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("id")));
            Page<Product> page = productRepository.shopGetDashboard(shopCode, filter.getTitle(), filter.getBrandId(),
                    filter.getCategory(), filter.getMinPrice(), filter.getMaxPrice(), pageable);
            PageData pageData = PageData.builder()
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageNumber())
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .data(page.stream().map(productMapper::entityToProductDetail).collect(Collectors.toList()))
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get product failed!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody shopUpdateProduct(Long id, ProductUpdateDTO dto) {
        return null;
    }

    @Override
    public ResponseBody shopDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name()));
        product.setIsDeleted(true);
        productRepository.save(product);
        return new ResponseBody(
                new ObjectChangedActiveResponse(product.getId(), product.getIsAccepted()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody adminGetListProductUnAccepted(ShopProductFilter filter) {
        try{
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), sort);
            Page<Product> page = productRepository.findAdminDashboard(
                    filter.getShopCode(), filter.getTitle(), filter.getBrandId(),
                    filter.getCategory(), pageable);
            PageData pageData = PageData.builder()
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageNumber())
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .data(page.stream().map(productMapper::entityToProductDetail).collect(Collectors.toList()))
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            e.printStackTrace();
            log.error("get product failed!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody checkProduct(Long id, boolean isAccepted) {
        Product product = productRepository.findById(id).orElseThrow(
                        () -> new RequestNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND.name()));
        product.setIsAccepted(isAccepted);
        product.setIsHidden(false);
        productRepository.save(product);
        return new ResponseBody(
                new ObjectChangedActiveResponse(product.getId(), product.getIsAccepted()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Override
    public ResponseBody guestGetListProduct(ShopProductFilter filter) {
        try {
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), sort);
            Page<Product> page = productRepository.guestGetListProduct(filter.getShopCode(),
                    filter.getTitle(), filter.getBrandId(), filter.getCategory(),
                    filter.getShopCategoryId(), pageable);
            PageData pageData = PageData.builder()
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageNumber())
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .data(page.stream().map(productMapper::entityToProductCard).collect(Collectors.toList()))
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get list product failed!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody guestSearchProduct(String keyword) {
        try{
            List<Product> products = productRepository.searchByTitleOrAttributeValue(keyword);
            List<ProductCard> responses = products.stream()
                    .map(productMapper::entityToProductCard)
                    .collect(Collectors.toList());
            return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("search product failed!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody guestGetAllBrand() {
        try {
            List<Brand> brands = brandRepository.findAll();
            List<BrandDto> responses = brands.stream()
                    .map((brand) -> {
                        return BrandDto.builder()
                                .id(brand.getId())
                                .brand(brand.getBrandName())
                                .productCount(productRepository.countProductByBrandId(brand.getId()))
                                .build();
                    })
                    .collect(Collectors.toList());
            return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get brand error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }

    @Override
    public ResponseBody guestGetAllCategoryOfLevel(Long categoryId) {
        try {
            if (categoryId != null) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RequestNotFoundException("ERROR"));
                List<Category> categories = categoryRepository
                        .findCategoriesByCategoryLevelAndParentCategory(category.getCategoryLevel() + 1, category);
                List<CategoryDto> responses = categories.stream()
                        .map((cate) -> {
                            return CategoryDto.builder()
                                    .id(cate.getId())
                                    .categoryName(cate.getCategoryName())
                                    .childrenCount(categoryRepository.countOfCategoryByParentCategory(cate))
                                    .build();
                        })
                        .collect(Collectors.toList());
                return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                List<Category> categories = categoryRepository.findCategoriesByCategoryLevel(0);
                List<CategoryDto> responses = categories.stream()
                        .map((cate) -> {
                            return CategoryDto.builder()
                                    .id(cate.getId())
                                    .categoryName(cate.getCategoryName())
                                    .childrenCount(categoryRepository.countOfCategoryByParentCategory(cate))
                                    .build();
                        })
                        .collect(Collectors.toList());
                return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.error("Get category error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }

    @Override
    public ResponseBody getProductHome(int pageNumber, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id")));
            Page<Product> page = productRepository.findAll(pageable);
            List<ProductCard> responses = page.stream()
                    .map(productMapper::entityToProductCard)
                    .collect(Collectors.toList());
            return new ResponseBody(responses, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get product error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }

    @Override
    public ResponseBody getProductDetail(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            return new ResponseBody(productMapper.entityToProductDetail(product), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get product error!" + e.getMessage());
            throw new RequestNotFoundException("Error " + e.getMessage());
        }
    }


}
