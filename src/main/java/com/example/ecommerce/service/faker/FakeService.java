package com.example.ecommerce.service.faker;

import com.example.ecommerce.config.Constant;
import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.mapper.category.CategoryMapper;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeService {

    private final UsersRepository usersRepository;
    private final Faker faker = new Faker(new Locale("vi"));
    private final PasswordEncoder passwordEncoder;
    private final RandomUtils randomUtils;
    private final RolesRepository rolesRepository;
    private final ShopRepository shopRepository;
    private final AccountService accountService;
    private final ShopCategoryRepository shopCategoryRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final JsonConvert jsonConvert;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final Random random;
    private final CategoryMapper categoryMapper;
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    //private final ProductProducer productProducer;
    private final ShopCategoryProductRepository shopCategoryProductRepository;


    //user - fake
    @Transactional
    public void generateAndInsertFakeUserData(){
        Set<String> uniqueEmails = new HashSet<>();
        for(int i = 0 ; i < 100 ; i++){
            Optional<Roles> roleOptional = rolesRepository.findByRoleName("KHACHHANG");
            List<Roles> roleEntityList = new ArrayList<>();
            roleOptional.ifPresent(roleEntityList::add);
            Users users = new Users();
            users.setFirstName(faker.name().firstName());
            users.setLastName(faker.name().lastName());
            users.setDob(generateFakeLocalDate());
            users.setGender(faker.options().option(Users.Gender.class));
            users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            users.setPasswordHash(passwordEncoder.encode("1234"));
            users.setActive(true);
            users.setImageUrl(Constant.USER_IMAGE);
            users.setVerifyCode(null);
            users.setListRolesOfUsers(roleEntityList);
            users.setEnable(true);
            users.setUsername("userkh" + i);
            String email;
            do {
                email = faker.internet().emailAddress();
            } while (uniqueEmails.contains(email));
            uniqueEmails.add(email);
            users.setEmail(email);
            usersRepository.save(users);
        }
    }

    public LocalDate generateFakeLocalDate(){
        Date fakeDate = faker.date().birthday();
        return fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void createRootAccount(){
        Optional<Roles> root = rolesRepository.findByRoleName(BaseRole.ROOT.toString());
        List<Roles> roleEntityList = new ArrayList<>();
        root.ifPresent(roleEntityList::add);

        Users users = new Users();
        users.setFirstName(faker.name().firstName());
        users.setLastName(faker.name().lastName());
        users.setDob(generateFakeLocalDate());
        users.setGender(faker.options().option(Users.Gender.class));
        users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
        users.setPasswordHash(passwordEncoder.encode("1234"));
        users.setActive(true);
        users.setImageUrl(Constant.USER_IMAGE);
        users.setVerifyCode(null);
        users.setListRolesOfUsers(roleEntityList);
        users.setEnable(true);
        users.setUsername("root");
        users.setEmail("root@gmail.com");
        usersRepository.save(users);
    }
    //shop - fake

    @Transactional
    public void generateAndInsertFakeShopDate(){
        List<Users> users = usersRepository.find100UserWithoutRoleId3();
        for(int i = 0 ; i < 100 ;i++){
            Users user = users.get(i);
            Shop shop = new Shop();
            shop.setBusinessType(faker.options().option(Shop.BusinessType.class));
            shop.setShopName(faker.name().username());
            shop.setShopLogo(Constant.SHOP_IMAGE);
            shop.setShopHotLine(faker.phoneNumber().subscriberNumber(10));
            shop.setFullName(faker.name().fullName());
            shop.setTaxCode(faker.phoneNumber().subscriberNumber(8));
            shop.setAddress(faker.address().fullAddress());
            shop.setShopEmail(faker.internet().emailAddress());
            shop.setDescription(faker.lorem().toString());
            shop.setIdentityCode(faker.idNumber().invalid());
            shop.setWardId(String.valueOf(faker.number().numberBetween(0,1000)));
            shop.setDistrictId(String.valueOf(faker.number().numberBetween(0,1000)));
            shop.setProvinceId(String.valueOf(faker.number().numberBetween(0,1000)));
            shop.setUsersInShop(user);
            shopRepository.save(shop);
            accountService.changeRoleUsers(user.getUserCode(),BaseRole.CHUCUAHANG.toString(),true);
        }
    }
    private Shop.BusinessType getRandomBusinessType() {
        List<Shop.BusinessType> businessTypes = Arrays.asList(Shop.BusinessType.values());
        return businessTypes.get(random.nextInt(businessTypes.size()));
    }

    @Transactional
    public void generateAndInsertFakeShopCategoryData(){
        List<Shop> shops = shopRepository.findAll();
        for(Shop shop : shops){
            int countCategory = random.nextInt(6);
            for(int i = 0 ; i < countCategory ; i++){
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setShopOfCategory(shop);
                shopCategory.setCategoryLevel(0);
                shopCategory.setCategoryName(faker.commerce().department());
                shopCategory.setActive(true);
                shopCategory.setDescription(faker.lorem().toString());
                shopCategory.setCategoryType(CategoryType.SHOP);
                shopCategory.setCategoryCode("SCTGY"+randomUtils.generateRandomString(15));
                shopCategoryRepository.save(shopCategory);
            }
        }
    }

    @Transactional
    public void generateAndInsertFakeAttributeData(){
        for(int i = 0 ; i < 20 ; i ++){
            Attribute attribute = new Attribute();
            attribute.setAttributeName(faker.commerce().productName());
            attribute.setValueJson("[]");
            attributeRepository.save(attribute);
            int countAttValue = random.nextInt(3)+2;
            for(int j = 0 ; j < countAttValue ; j++){
                AttributeValue attributeValue = new AttributeValue();
                attributeValue.setValue(faker.name().lastName());
                attributeValue.setAttribute(attribute);
                attributeValueRepository.save(attributeValue);
                try{
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
        }
    }
    @Transactional
    public void generateAndInsertFakeBrand(){
        for(int i = 0 ; i < 30 ; i++){
            Brand brand = new Brand();
            brand.setBrandName(faker.company().name());
            brand.setDescription(faker.lorem().toString());
            brand.setActive(true);
            brandRepository.save(brand);
        }
    }
    @Transactional
    public void generateAndInsertFakeProductData() {
        List<Shop> shops = shopRepository.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorService rabbitMqExecutor = Executors.newFixedThreadPool(10);
        try {
            for (Shop shop : shops) {
                int countProduct = 5;
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (int i = 0; i < countProduct; i++) {
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> createProduct(shop), executorService)
                            .thenAcceptAsync(product -> {
                                if (product != null) {
                                    //productProducer.sendProductToQueue(ProductMapper.mapperProductToDocument(product));
                                    log.info("create product success");
                                }
                            }, rabbitMqExecutor);

                    futures.add(future);
                }

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        } finally {
            executorService.shutdown();
            rabbitMqExecutor.shutdown();
        }
    }

    @Transactional
    public Product createProduct(Shop shop) {
        try {
            Category category = categoryRepository.findRandomCategory();
            ShopCategory shopCategory = shopCategoryRepository.findRandomShopCategory();
            Brand brand = brandRepository.findRandomBrand();

            Product product = new Product();
            String title = faker.funnyName().name();
            product.setTitle(title);
            product.setDescription(faker.lorem().toString());
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setProductState(Product.ProductState.ACTIVE);
            product.setCategory(category);
            product.setBrandOfProduct(brand);
            product.setProductCode("PID" + randomUtils.generateRandomString(15));
            product.setShopOfProduct(shop);
            product.setSoldCount(random.nextInt(100, 10000));

            List<Attribute> attributes = randomAttributes(2);
            if (attributes.isEmpty()) {
                // Tao thuoc tính mac dinh neu danh sach rong
                Attribute defaultAttribute = new Attribute();
                defaultAttribute.setAttributeName("Default Attribute" + UUID.randomUUID());
                attributes.add(defaultAttribute);
            }

            Map<Attribute, List<AttributeValue>> mapAttribute = new HashMap<>();
            for (Attribute attribute : attributes) {
                List<AttributeValue> attributeValues = randomAttributeValues(attribute, 2); // 2 giá trị thuộc tính
                if (attributeValues.isEmpty()) {
                    // tao gia tri thuoc tính mac dinh neu rong
                    AttributeValue defaultValue = new AttributeValue();
                    defaultValue.setValue("Default Value");
                    attributeValues.add(defaultValue);
                }
                mapAttribute.put(attribute, attributeValues);
            }

            Set<String> pairedCombinations = new HashSet<>();
            List<BigDecimal> prices = new ArrayList<>();

            for (Attribute attribute : attributes) {
                for (AttributeValue value : mapAttribute.get(attribute)) {
                    String pair = attribute.getId() + "-" + value.getId();
                    if (!pairedCombinations.contains(pair)) {
                        pairedCombinations.add(pair);
                        ProductVariant variant = new ProductVariant();

                        long min = 50000;
                        long max = 20000000;
                        long randomValue = min + (long) (Math.random() * (max - min));
                        randomValue = (randomValue / 100) * 100;
                        BigDecimal price = BigDecimal.valueOf(randomValue);
                        variant.setPrice(price);
                        prices.add(price);

                        variant.setStockQuantity(faker.number().randomDigitNotZero());
                        variant.setSku(faker.code().toString());
                        variant.setVariantSlug("default-variant");
                        variant.setHeight(10.0);
                        variant.setWeight(1.0);
                        variant.setLength(10.0);
                        variant.setWidth(10.0);
                        variant.setStatus(ProductVariant.Status.CON_HANG);
                        variant.setProduct(product);
                        product.getVariants().add(variant);
                    }
                }
            }

            // Neu khong tao bien the duoc, them bien the mac dinh
            if (product.getVariants().isEmpty()) {
                ProductVariant defaultVariant = new ProductVariant();
                defaultVariant.setPrice(BigDecimal.valueOf(100000));
                defaultVariant.setStockQuantity(10);
                defaultVariant.setSku("DEFAULT-SKU");
                defaultVariant.setVariantSlug("default-variant");
                defaultVariant.setHeight(10.0);
                defaultVariant.setWeight(1.0);
                defaultVariant.setLength(10.0);
                defaultVariant.setWidth(10.0);
                defaultVariant.setStatus(ProductVariant.Status.CON_HANG);

                defaultVariant.setProduct(product);
                product.getVariants().add(defaultVariant);
                prices.add(defaultVariant.getPrice());
            }

            List<BigDecimal> validPrices = prices.stream().filter(Objects::nonNull).collect(Collectors.toList());
            product.setMinInitialPrice(validPrices.stream().min(BigDecimal::compareTo).orElse(null));
            product.setMaxInitialPrice(validPrices.stream().max(BigDecimal::compareTo).orElse(null));

            product = productRepository.save(product);

            //luu thong tin
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category);
            categoryProduct.setProduct(product);
            categoryProductRepository.save(categoryProduct);

            ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
            shopCategoryProduct.setProduct(product);
            shopCategoryProduct.setShopCategory(shopCategory);
            shopCategoryProductRepository.save(shopCategoryProduct);

            return product;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating product", e);
        }
    }




    // Helper method to add variant and product attribute values
    private void addVariantAttributeValueAndProductAttributeValue(ProductVariant variant, Attribute attribute,
                                                                  AttributeValue value, Product product, StringBuilder stringBuilder) {
        VariantAttributeValue variantAttributeValue = new VariantAttributeValue();
        variantAttributeValue.setAttribute(attribute);
        variantAttributeValue.setAttributeValue(value);
        variant.getVariantAttributeValueList().add(variantAttributeValue);

        stringBuilder.append(attribute.getAttributeName().toLowerCase())
                .append("=")
                .append(value.getValue().toLowerCase())
                .append(";");

        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        productAttributeValue.setAttribute(attribute);
        productAttributeValue.setAttributeValue(value);
        product.getProductAttributeValues().add(productAttributeValue);
    }

    private List<Attribute> randomAttributes(int count){
        List<Attribute> attributes = attributeRepository.findAll();
        Collections.shuffle(attributes);
        return attributes.subList(0, count);
    }
    private List<AttributeValue> randomAttributeValues(Attribute attribute,int count){
        List<AttributeValue> attributeValues = attributeValueRepository.findAttributeValuesByAttributeId(attribute.getId());
        Collections.shuffle(attributeValues);
        return attributeValues.subList(0,count);

    }
}
