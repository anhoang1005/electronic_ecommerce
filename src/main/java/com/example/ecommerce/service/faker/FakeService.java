package com.example.ecommerce.service.faker;

import com.example.ecommerce.config.Constant;
import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.mapper.order.OrderMapper;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.category.CategoryBreadcrumb;
import com.example.ecommerce.rabbit.OrderItemProducer;
import com.example.ecommerce.rabbit.OrderProducer;
import com.example.ecommerce.rabbit.ProductProducer;
import com.example.ecommerce.rabbit.elastic.OrderItemDocument;
import com.example.ecommerce.rabbit.elastic.OrderShopDocument;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.utils.GlobalUtils;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductProducer productProducer;
    private final ShopCategoryProductRepository shopCategoryProductRepository;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderShopRepository orderShopRepository;
    private final OrderProducer orderProducer;
    private final OrderMapper orderMapper;
    private final OrderItemProducer  orderItemProducer;

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
            int countCategory = random.nextInt(6)+1;
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
                int countProduct = 20;
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (int i = 0; i < countProduct; i++) {
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> createProduct(shop), executorService)
                            .thenAcceptAsync(product -> {
                                if (product != null) {
                                    log.info("create product success");
                                    productProducer.sendProductToQueue(ProductMapper.mapperProductToDocument(product));
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
    public void sendOrderItemShopToElasticsearch(){
        int threadPoolSize = 10;
        ExecutorService itemExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        try{
            List<OrdersItems> ordersItemsList = orderItemRepository.findAll();

            for(OrdersItems ordersItems : ordersItemsList){
                itemExecutorService.submit(() -> {
                    try{
                        OrderItemDocument orderItemDocument = orderMapper.mapToOrderItemDocument(ordersItems);
                        orderItemProducer.sendItemToQueue(orderItemDocument);
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("Failed to send item: {}, error: {}", ordersItems.getId(), e.getMessage(), e);

                    }
                });
            }
        } finally {
            itemExecutorService.shutdown();
            try {
                if (!itemExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("Forcing shutdown of orderExecutorService due to timeout.");
                    itemExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("ExecutorService shutdown interrupted.", e);
                itemExecutorService.shutdownNow();
            }
        }
    }
    public  List<OrderShopDocument> getOrderShopDocumentList(){
        return orderShopRepository.findAllWithItems()
                .stream()
                .map(orderMapper::mapToOrderDocument)
                .collect(Collectors.toList());
    }
    @Transactional
    public void sendOrderShopToElasticSearch() {
        int threadPoolSize = 10;
        ExecutorService orderExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        try {
            List<OrdersShop> ordersShopList = orderShopRepository.findAll();

            for (OrdersShop shop : ordersShopList) {
                Hibernate.initialize(shop.getListOrdersItemsOfOrdersShop());
            }

            for (OrdersShop ordersShop : ordersShopList) {
                orderExecutorService.submit(() -> {
                    try {
                        OrderShopDocument orderDocument = orderMapper.mapToOrderDocument(ordersShop);
                        orderProducer.sendOrderToQueue(orderDocument);
                    } catch (Exception e) {
                        log.error("Failed to send order: {}, error: {}", ordersShop.getId(), e.getMessage(), e);
                    }
                });
            }
        } finally {

        }
    }
    @Transactional
    public Product createProduct(Shop shop) {
        try {
            Category category = categoryRepository.findRandomCategory();
            ShopCategory shopCategory = shopCategoryRepository.findRandomShopCategoriesByShop(shop.getId()).get(0);
            Brand brand = brandRepository.findRandomBrand();

            List<CategoryBreadcrumb> breadcrumbs = new ArrayList<>();
            breadcrumbs.add(CategoryBreadcrumb.builder()
                    .id(category.getId())
                    .category(category.getCategoryName())
                    .build());
            if (category.getCategoryLevel() > 0) {
                while (category != null && category.getCategoryLevel() > 0) {
                    Category parent = category.getParentCategory();
                    if (parent != null) {
                        breadcrumbs.add(CategoryBreadcrumb.builder()
                                .id(parent.getId())
                                .category(parent.getCategoryName())
                                .build());
                    }
                    category = parent;
                }
                Collections.reverse(breadcrumbs);
            }

            Product product = new Product();
            product.setBreadcrumb(objectMapper.writeValueAsString(breadcrumbs));
            String title = faker.funnyName().name();
            product.setTitle(title);
            product.setDescription(faker.lorem().toString());
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setIsAccepted(random.nextBoolean());
            product.setProductState(Product.ProductState.ACTIVE);
            product.setCategory(category);
            product.setBrandOfProduct(brand);
            product.setProductCode("PID" + randomUtils.generateRandomString(15));
            product.setShopOfProduct(shop);
            product.setSoldCount(random.nextInt(100, 10000));
            product.setRating(faker.number().randomDouble(1, 1, 10));
            //Product image
            ProductImage image = new ProductImage();
            image.setImageUrl("https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lzd2qfj9xc99f4.webp");
            image.setActive(true);
            image.setIsMainImage(true);
            image.setProductOfProductImage(product);
            List<String> images = List.of(image).stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
            product.setListImageOfProduct(List.of(image));
            product.setImageList(objectMapper.writeValueAsString(images));


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
                        long max = 10000000;
                        long randomValue = min + (long) (Math.random() * (max - min));
                        randomValue = (randomValue / 1000) * 1000;
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
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("defailt");
                        addVariantAttributeValueAndProductAttributeValue(variant, attribute, value, product, stringBuilder);
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
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("defailt");
                        addVariantAttributeValueAndProductAttributeValue(defaultVariant, attribute, value, product, stringBuilder);
                    }
                }
            }

            List<BigDecimal> validPrices = prices.stream().filter(Objects::nonNull).collect(Collectors.toList());
            product.setMinInitialPrice(validPrices.stream().min(BigDecimal::compareTo).orElse(null));
            product.setMaxInitialPrice(validPrices.stream().max(BigDecimal::compareTo).orElse(null));

            productRepository.save(product);

            //luu thong tin
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category);
            categoryProduct.setProduct(product);
            categoryProductRepository.save(categoryProduct);

            ShopCategoryProduct shopCategoryProduct = new ShopCategoryProduct();
            shopCategoryProduct.setProduct(product);
            shopCategoryProduct.setShopCategory(shopCategory);
            product.getShopCategoryProducts().add(shopCategoryProduct);
            shopCategoryProductRepository.save(shopCategoryProduct);
            return product;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating product", e);
        }
    }

    @Transactional
    public void generateAndInsertOrders() {
        for(int month = 1; month <= 12; month++) {
            for(int i = 0; i < 100; i++) {
                // get 1 buyer random
                Users buyer = usersRepository.findUsersById(random.nextLong(99) + 1);
                for(int orderStt = 1; orderStt <= random.nextInt(10) + 1; orderStt++) {
                    // Create and populate Orders first
                    Orders orders = new Orders();

                    LocalDateTime modifyDate = generateRandomDateInMonth(month);
                    log.info("modify date :{}", modifyDate);
                    orders.setModifiedAt(modifyDate);
                    orders.setClientAddress(faker.address().streetAddress() + ", " + faker.address().city() + ", " + faker.address().country());
                    orders.setClientName(faker.name().firstName() + " " + faker.name().lastName());
                    orders.setClientTelephone(faker.phoneNumber().cellPhone());
                    orders.setUsersOfOrder(buyer);
                    String deliveryMethod = OrderShipping.Method.GHN.name();
                    orders.setDeliveryMethod(deliveryMethod);

                    boolean isPayment = random.nextBoolean();
                    orders.setIsPayment(isPayment);

                    String paymentMethod = OrderPayment.Method.values()[random.nextInt(OrderPayment.Method.values().length)].name();
                    orders.setPaymentMethod(paymentMethod);
                    Orders.Status status = Orders.Status.values()[random.nextInt(Orders.Status.values().length)];
                    orders.setStatus(status);

                    BigDecimal totalAmount = BigDecimal.ZERO;
                    BigDecimal totalDiscount = BigDecimal.ZERO;
                    BigDecimal totalServiceFee = BigDecimal.ZERO;
                    BigDecimal totalShipping = BigDecimal.ZERO;
                    BigDecimal totalProductAmount = BigDecimal.ZERO;

                    orders.setTotalDiscount(totalDiscount);
                    orders.setTotalFee(totalServiceFee);
                    orders.setTotalShipping(totalShipping);
                    orders.setTotalAmount(totalAmount);

                    // Save Orders first to get ID
                    orders = orderRepository.save(orders);

                    List<OrdersShop> ordersShops = new ArrayList<>();

                    for(int shopStt = 1; shopStt <= random.nextInt(2) + 1; shopStt++) {
                        Shop shop = shopRepository.findShopById(random.nextLong(99) + 1);
                        OrdersShop ordersShop = new OrdersShop();
                        ordersShop.setModifiedAt(modifyDate);
                        BigDecimal totalAmountProductShop = BigDecimal.ZERO;

                        ordersShop.setShopOfOrdersShop(shop);
                        ordersShop.setDeliveryMethod(deliveryMethod);
                        ordersShop.setEcommerceTotalAmount(randomFeeForSomething());
                        ordersShop.setIsPayment(isPayment);

                        OrdersShop.Status orderShopStatus = OrdersShop.Status.values()[random.nextInt(OrdersShop.Status.values().length)];
                        ordersShop.setOrderStatus(orderShopStatus);
                        ordersShop.setPaymentMethod(paymentMethod);

                        BigDecimal totalShopServiceFee = randomFeeForSomething();
                        ordersShop.setServiceFee(totalShopServiceFee);

                        BigDecimal totalShopShippingFee = randomFeeForSomething();
                        ordersShop.setShopShippingFee(totalShopShippingFee);

                        // Set the saved Orders
                        ordersShop.setOrdersOfOrdersShop(orders);
                        ordersShop.setShopOfOrdersShop(shop);

                        ordersShop.setTotalProduct(totalAmountProductShop);
                        ordersShop.setShopShippingFee(totalShopShippingFee);
                        ordersShop.setShopDiscount(BigDecimal.ZERO);
                        ordersShop.setShopTotalAmount(BigDecimal.ZERO);
                        // Save OrdersShop before creating OrderItems
                        ordersShop = orderShopRepository.save(ordersShop);

                        log.info(shop.getId().toString());
                        for(int orderItemProductStt = 1; orderItemProductStt <= (random.nextInt(shop.getProducts().size()) + 1); orderItemProductStt++) {
                            Product itemProduct = shop.getProducts().get(random.nextInt(shop.getProducts().size()));
                            BigDecimal totalAmountItemProduct = BigDecimal.ZERO;
                            List<Long> items =new ArrayList<>();
                            for(int orderItemVariantStt = 0; orderItemVariantStt <= (random.nextInt(itemProduct.getVariants().size())); orderItemVariantStt++) {
                                ProductVariant itemVariant = itemProduct.getVariants().get(orderItemVariantStt);
                                BigDecimal totalAmountItemVariant = itemVariant.getPrice();

                                OrdersItems ordersItems = new OrdersItems();
                                ordersItems.setModifiedAt(modifyDate);
                                ordersItems.setProductId(itemProduct.getId());
                                ordersItems.setVariantId(itemVariant.getId());
                                ordersItems.setDescription(faker.lorem().sentence());
                                ordersItems.setVariantName(faker.name().title());
                                ordersItems.setProductImage(
                                        itemProduct.getListImageOfProduct()
                                                .stream()
                                                .filter(image -> image.getIsMainImage())
                                                .findFirst()
                                                .map(image -> image != null ? image.getImageUrl() : GlobalUtils.DEFAULT_URL)
                                                .orElse(null));
                                ordersItems.setProductTitle(itemProduct.getTitle());
                                Long totalQuantity = random.nextLong(10) + 1;
                                ordersItems.setQuantity(totalQuantity);
                                ordersItems.setPrice(totalAmountItemVariant);

                                totalAmountItemProduct = totalAmountItemProduct.add(totalAmountItemVariant.multiply(BigDecimal.valueOf(totalQuantity)));
                                ordersItems.setOrdersShopOfOrdersItems(ordersShop);

                                ordersItems = orderItemRepository.save(ordersItems);
                                items.add(ordersItems.getId());
                            }
                            log.info("items : {}",items.stream().toArray());
                            ordersShop.setItemIds(items.toString());
                            totalAmountProductShop = totalProductAmount.add(totalAmountItemProduct);
                        }

                        BigDecimal totalShopDiscount = randomFeeForSomething();
                        ordersShop.setTotalProduct(totalAmountProductShop);
                        ordersShop.setShopShippingFee(totalShopShippingFee);
                        ordersShop.setShopDiscount(totalShopDiscount);

                        BigDecimal totalShopAmount = totalAmountProductShop
                                .subtract(totalShopDiscount)
                                .subtract(totalShopShippingFee);
                        ordersShop.setShopTotalAmount(totalShopAmount);

                        // Update OrdersShop with final calculations
                        orderShopRepository.save(ordersShop);

                        totalProductAmount = totalProductAmount.add(totalAmountProductShop);
                        totalServiceFee = totalServiceFee.add(totalShopServiceFee);
                        totalShipping = totalShipping.add(totalShopShippingFee);
                        totalDiscount = totalDiscount.add(totalShopDiscount);
                    }

                    // Update final Orders calculations
                    orders.setTotalDiscount(totalDiscount);
                    orders.setTotalFee(totalServiceFee);
                    orders.setTotalShipping(totalShipping);

                    totalAmount = totalProductAmount
                            .add(totalServiceFee)
                            .add(totalShipping)
                            .subtract(totalDiscount);
                    BigDecimal totalEcommerceServiceFee = totalServiceFee.multiply(BigDecimal.valueOf(0.05));
                    orders.setEcommerceServiceFee(totalEcommerceServiceFee);
                    totalAmount = totalProductAmount.subtract(totalEcommerceServiceFee);
                    orders.setTotalAmount(totalAmount);

                    orderRepository.save(orders);
                }
            }
        }
    }

    // Helper method to add variant and product attribute values
    private void addVariantAttributeValueAndProductAttributeValue(ProductVariant variant, Attribute attribute,
                                                                  AttributeValue value, Product product, StringBuilder stringBuilder) {
        VariantAttributeValue variantAttributeValue = new VariantAttributeValue();
        variantAttributeValue.setAttribute(attribute);
        variantAttributeValue.setAttributeValue(value);
        variantAttributeValue.setVariant(variant);
        variant.getVariantAttributeValueList().add(variantAttributeValue);

        stringBuilder.append(attribute.getAttributeName().toLowerCase())
                .append("=")
                .append(value.getValue().toLowerCase())
                .append(";");

        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        productAttributeValue.setAttribute(attribute);
        productAttributeValue.setAttributeValue(value);
        productAttributeValue.setProduct(product);
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

    private BigDecimal randomFeeForSomething(){
        double randomValue = 1000 + (100000 - 1000) * random.nextDouble();
        return new BigDecimal(randomValue).setScale(2, RoundingMode.HALF_UP);
    }
    private LocalDateTime generateRandomDateInMonth(int month) {
        int day = random.nextInt(28) + 1; // Giả sử mỗi tháng có 28 ngày để tránh lỗi ngày không hợp lệ
        return LocalDateTime.of(2024, month, day, random.nextInt(24), random.nextInt(60), random.nextInt(60));
    }
}
