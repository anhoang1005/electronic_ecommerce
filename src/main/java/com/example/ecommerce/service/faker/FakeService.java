package com.example.ecommerce.service.faker;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.entities.enums.ProductState;
import com.example.ecommerce.mapper.category.CategoryMapper;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.payload.category.CategoryResponse;
import com.example.ecommerce.rabbit.ProductProducer;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.repository.elastic.ProductDocumentRepository;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.service.elastic.ProductDocumentService;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeService {

    private final UsersRepository usersRepository;
    private final Faker faker;
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
    private final VariantAttributeValueRepository variantAttributeValueRepository;
    private final ProductProducer productProducer;
    private final EntityManagerFactory entityManagerFactory;
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
            users.setGender(randomGender());
            users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            users.setPasswordHash(passwordEncoder.encode("1234"));
            users.setActive(true);
            users.setImageUrl("user.jpg");
            users.setVerifyCode(randomUtils.generateRandomString(9));
            users.setListRolesOfUsers(roleEntityList);
            users.setEnable(true);
            users.setUsername(faker.name().username());
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
    public Users.Gender randomGender(){
        Users.Gender[] genders = Users.Gender.values();
        int randomIndex = new Random().nextInt(genders.length);
        return genders[randomIndex];
    }

    public void createRootAccount(){
        Optional<Roles> root = rolesRepository.findByRoleName(BaseRole.ROOT.toString());
        List<Roles> roleEntityList = new ArrayList<>();
        root.ifPresent(roleEntityList::add);
//        Optional<Roles> root = rolesRepository.findByRoleName("ROOT");
//        List<Roles> roleEntityList = new ArrayList<>();
//        root.ifPresent(roleEntityList::add);

        Users users = new Users();
        users.setFirstName(faker.name().firstName());
        users.setLastName(faker.name().lastName());
        users.setDob(generateFakeLocalDate());
        users.setGender(randomGender());
        users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
        users.setPasswordHash(passwordEncoder.encode("1234"));
        users.setActive(true);
        users.setImageUrl("user.jpg");
        users.setVerifyCode(randomUtils.generateRandomString(9));
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
            shop.setBusinessType(getRandomBusinessType());
            shop.setShopName(faker.name().username());
            shop.setShopLogo("shop.png");
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
    public void generateAndInserFakeShopCategoryData(){
        List<Shop> shops = shopRepository.findAll();
        for(Shop shop : shops){
            int countCategory = random.nextInt(6);
            for(int i = 0 ; i < countCategory ; i++){
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setShopOfCategory(shop);
                shopCategory.setCategoryLevel(0);
                shopCategory.setCategoryName(faker.name().firstName());
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
            attribute.setAttributeName(faker.name().firstName());
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

        // Tạo ExecutorService một lần duy nhất, có thể thay đổi kích thước pool tùy theo nhu cầu
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Executor cho tạo sản phẩm
        ExecutorService rabbitMqExecutor = Executors.newFixedThreadPool(10); // Executor cho RabbitMQ

        try {
            // Sử dụng CompletableFuture để làm việc bất đồng bộ mạnh mẽ hơn
            for (Shop shop : shops) {
                int countProduct = 5;

                // Danh sách chứa tất cả các CompletableFuture cho các sản phẩm
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                // Tạo danh sách sản phẩm và gửi vào RabbitMQ
                for (int i = 0; i < countProduct; i++) {
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> createProduct(shop), executorService)
                            .thenAcceptAsync(product -> {
                                if (product != null) {
                                    productProducer.sendProductToQueue(ProductMapper.mapperProductToDocument(product));
                                }
                            }, rabbitMqExecutor);

                    futures.add(future);
                }

                // Đợi tất cả các công việc hoàn thành
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        } finally {
            // Đảm bảo shutdown dù có lỗi xảy ra
            executorService.shutdown();
            rabbitMqExecutor.shutdown();
        }
    }



    @Transactional
    public Product createProduct(Shop shop) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();

            Category category = em.merge(categoryRepository.findRandomCategory());
            ShopCategory shopCategory = em.merge(shopCategoryRepository.findRandomShopCategory());
            List<ShopCategory> shopCategories = new ArrayList<>();
            shopCategories.add(shopCategory);
            Brand brand = em.merge(brandRepository.findRandomBrand());
            Product product = new Product();
            String title = faker.funnyName().name();
            product.setTitle(title);
            product.setDescription(faker.lorem().toString());
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setProductState(ProductState.ACTIVE);
            product.setCategory(category);
            product.setBrand(brand);
            product.setProductCode("PID" + randomUtils.generateRandomString(15));
            product.setShop(shop);
            product.setSold(random.nextInt(100,10000));
            product.setRating(Double.parseDouble(String.format("%.1f", faker.number().randomDouble(1, 1, 5))));


            List<BigDecimal> prices = new ArrayList<>();

            int countAttribute = 2;
            Map<Attribute, List<AttributeValue>> mapAttribute = new HashMap<>();
            List<Attribute> attributes = randomAttributes(countAttribute);
            for (Attribute attribute : attributes) {
                int countAttributeValue = random.nextInt(attributeValueRepository.findAttributeValuesByAttributeId(attribute.getId()).size());
                List<AttributeValue> attributeValues = randomAttributeValues(attribute, countAttributeValue);
                mapAttribute.put(attribute, attributeValues);
            }

            Set<String> pairedCombinations = new HashSet<>();
            Attribute attribute1 = attributes.get(0);
            List<AttributeValue> values1 = mapAttribute.get(attribute1);

            Attribute attribute2 = attributes.get(1);
            List<AttributeValue> values2 = mapAttribute.get(attribute2);

            for (AttributeValue value1 : values1) {
                for (AttributeValue value2 : values2) {
                    String pair = value1.getId() + "-" + value2.getId();
                    if (!pairedCombinations.contains(pair)) {
                        pairedCombinations.add(pair);

                        Variant variant = new Variant();
                        BigDecimal price = new BigDecimal(String.format("%.2f", faker.number().randomDouble(2, 1, 1000)));
                        variant.setPrice(price);
                        prices.add(price);
                        variant.setQuantity(faker.number().randomDigit());
                        variant.setSku(faker.code().toString());
                        variant.setVariantSlug("demo_");
                        variant.setHeight(faker.number().randomDouble(1, 1, 10));
                        variant.setWeight(faker.number().randomDouble(1, 1, 10));
                        variant.setLength(faker.number().randomDouble(1, 1, 10));
                        variant.setWidth(faker.number().randomDouble(1, 1, 10));

                        StringBuilder stringBuilder = new StringBuilder();

                        variant.setProductVariation(stringBuilder.toString());
                        em.merge(variant);
                        addVariantAttributeValueAndProductAttributeValue(variant, attribute1, value1, product, stringBuilder);
                        addVariantAttributeValueAndProductAttributeValue(variant, attribute2, value2, product, stringBuilder);


                        for (VariantAttributeValue variantAttributeValue : variant.getVariantAttributeValueList()) {
                            variantAttributeValue.setVariant(variant);
                            em.merge(variantAttributeValue);
                        }

                        product.getVariants().add(variant);
                    }
                }
            }
            ///
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            categoryResponses.add(categoryMapper.entityToCategoryResponse(category));
            Category current = category;
            while (!ObjectUtils.isEmpty(current.getParentCategory())) {
                categoryResponses.add(categoryMapper.entityToCategoryResponse(category.getParentCategory()));
                try {
                    product.setBreadcrumb(jsonConvert.convertListObjectToJson(categoryResponses));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                current = current.getParentCategory();
            }

            try {
                product.setVariations(jsonConvert.convertListObjectToJson(product.getVariants()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            product.setMinInitialPrice(prices.stream().min(BigDecimal::compareTo).orElse(null));
            product.setMaxInitialPrice(prices.stream().max(BigDecimal::compareTo).orElse(null));

            productRepository.save(product);
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category);
            categoryProduct.setProduct(product);
            categoryProductRepository.save(categoryProduct);

            ///
            ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
            shopCategoryProduct.setProduct(product);
            shopCategoryProduct.setShopCategory(shopCategory);
            shopCategoryProductRepository.save(shopCategoryProduct);

            ///
            for (ProductAttributeValue productAttributeValue : product.getProductAttributeValues()) {
                productAttributeValue.setProduct(product);
                productAttributeValueRepository.save(productAttributeValue);
            }

            for (Variant variant : product.getVariants()) {
                variant.setProduct(product);
                variantRepository.save(variant);
            }

            return product;
        }catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(em.isOpen()){
                em.close();
            }
        }
    }

    // Helper method to add variant and product attribute values
    private void addVariantAttributeValueAndProductAttributeValue(Variant variant, Attribute attribute,
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

    // Helper method to save product attributes and variants
    private void saveProductAttributesAndVariants(Product product) {

    }

    // Helper method to save breadcrumb and variations
    private void saveBreadcrumbAndProductDetails(Product product, Category category) {

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
    private Category randomCategories(){
        return categoryRepository.findRandomCategory();
    }
    private Brand randomBrand(){
        return brandRepository.findRandomBrand();
    }


}
