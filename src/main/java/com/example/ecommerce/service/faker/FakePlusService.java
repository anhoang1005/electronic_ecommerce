package com.example.ecommerce.service.faker;

import com.example.ecommerce.config.Constant;
import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.category.CategoryBreadcrumb;
import com.example.ecommerce.rabbit.ProductProducer;
import com.example.ecommerce.rabbit.elastic.ProductDocument;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FakePlusService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private final Faker faker = new Faker(new Locale("vi"));
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final BrandRepository brandRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ShopCategoryProductRepository shopCategoryProductRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ShopReviewRepository shopReviewRepository;

    private final AccountService accountService;
    private final ProductProducer productProducer;
    private final ProductMapper productMapper;

    private final RandomUtils randomUtils;
    private final JsonConvert jsonConvert;

    private final EntityManager entityManager;

    @Transactional
    public void createRootAccount(){
        long start = System.nanoTime();
        LocalDate dob = faker.date().birthday(18, 65).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        Optional<Roles> root = rolesRepository.findByRoleName(BaseRole.ROOT.toString());
        Optional<Roles> admin = rolesRepository.findByRoleName(BaseRole.QUANLY.toString());
        Optional<Roles> owner = rolesRepository.findByRoleName(BaseRole.CHUCUAHANG.toString());
        Optional<Roles> client = rolesRepository.findByRoleName(BaseRole.KHACHHANG.toString());
        List<Roles> roleEntityList = new ArrayList<>();
        roleEntityList.add(root.get());
        roleEntityList.add(admin.get());
        roleEntityList.add(owner.get());
        roleEntityList.add(client.get());
        Users users = new Users();
        users.setFirstName(faker.name().firstName());
        users.setLastName(faker.name().lastName());
        users.setDob(dob);
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
        log.info("Fake root account in: " + ((System.nanoTime() - start)/1_000_000) + "ms");
    }

    @Transactional
    public void createUserAccount(int count){
        long start = System.nanoTime();
        Optional<Roles> root = rolesRepository.findByRoleName(BaseRole.KHACHHANG.toString());
        List<Roles> roleEntityList = new ArrayList<>();
        roleEntityList.add(root.get());
        List<Users> usersList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            LocalDate dob = faker.date().birthday(18, 65).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String username = StringFormatUtils.convertNameToCustomFormat(firstName + lastName) + i;
            Users users = new Users();
            users.setFirstName(firstName);
            users.setLastName(lastName);
            users.setDob(dob);
            users.setGender(faker.options().option(Users.Gender.class));
            users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            users.setPasswordHash(passwordEncoder.encode("1234"));
            users.setActive(true);
            users.setImageUrl(Constant.USER_IMAGE);
            users.setVerifyCode(null);
            users.setListRolesOfUsers(roleEntityList);
            users.setEnable(true);
            users.setUsername(username);
            users.setEmail(username + "@gmail.com");
            usersList.add(users);
        }
        usersRepository.saveAll(usersList);
        log.info("Fake {} client account in: " + ((System.nanoTime() - start)/1_000_000) + "ms", count);
    }

    @Transactional
    public void generateAndInsertFakeShopDate(){
        long start = System.nanoTime();
        List<Users> users = usersRepository.find100UserWithoutRoleId3();
        for(int i = 0 ; i < 100 ;i++){
            Users user = users.get(i);
            String shopName = user.getUsername() + "shop";
            Shop shop = new Shop();
            shop.setBusinessType(faker.options().option(Shop.BusinessType.class));
            shop.setShopName(shopName);
            shop.setShopLogo(Constant.SHOP_IMAGE);
            shop.setShopHotLine(faker.phoneNumber().subscriberNumber(10));
            shop.setFullName(user.getFirstName() + " " + user.getLastName());
            shop.setTaxCode(faker.phoneNumber().subscriberNumber(8));
            shop.setAddress(faker.address().fullAddress());
            shop.setShopEmail(user.getEmail());
            shop.setDescription(faker.lorem().sentence(20));
            shop.setIdentityCode(faker.idNumber().invalid());
            shop.setWardId("1B1507");
            shop.setDistrictId("1542");
            shop.setProvinceId(String.valueOf(faker.number().numberBetween(0,1000)));
            shop.setUsersInShop(user);
            shopRepository.save(shop);
            accountService.changeRoleUsers(user.getUserCode(),BaseRole.CHUCUAHANG.toString(),true);
        }
        log.info("Fake 100 shop in: " + ((System.nanoTime() - start)/1_000_000) + "ms");
    }

    @Transactional
    public void generateAndInsertFakeShopCategoryData(){
        long start = System.nanoTime();
        List<String> electronicsCategoryNames = Arrays.asList(
                "Điện thoại di động", "Máy tính xách tay", "Máy tính để bàn",
                "Laptop gaming", "Phụ kiện điện thoại", "Máy ảnh kỹ thuật số",
                "Tivi và thiết bị giải trí", "Máy chiếu", "Tai nghe và loa Bluetooth",
                "Thiết bị gia dụng thông minh", "Điện tử ô tô", "Đồng hồ thông minh và thiết bị đeo",
                "Máy tính bảng", "Camera an ninh", "Dịch vụ sửa chữa điện thoại",
                "Thiết bị mạng và Wi-Fi", "Ổ cứng và thiết bị lưu trữ", "Màn hình máy tính"
        );
        List<Shop> shops = shopRepository.findAll();
        for(Shop shop : shops){
            List<ShopCategory> shopCategories = new ArrayList<>();
            for(String categoryName : electronicsCategoryNames){
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setShopOfCategory(shop);
                shopCategory.setCategoryLevel(0);
                shopCategory.setCategoryName(categoryName);
                shopCategory.setActive(true);
                shopCategory.setDescription(null);
                shopCategory.setCategoryType(CategoryType.SHOP);
                shopCategory.setCategoryCode("SCTGY" + randomUtils.generateRandomString(15));
                shopCategories.add(shopCategory);
            }
            shopCategoryRepository.saveAll(shopCategories);
        }
        log.info("Fake {} category for {} shop in: " + ((System.nanoTime() - start)/1_000_000) + "ms", shops.size()*electronicsCategoryNames.size(), shops.size());
    }

    @Transactional
    public void generateAndInsertFakeBrand(){
        long start = System.nanoTime();
        List<String> electronicsBrandNames = Arrays.asList(
                "Samsung", "Apple", "Sony", "LG", "Xiaomi", "Huawei", "Asus", "Dell", "Acer", "Lenovo",
                "HP", "Toshiba", "Canon", "Nikon", "Panasonic", "Sharp", "Bose", "JBL", "Sennheiser", "Beats", "Philips",
                "Casio", "Razer", "Logitech", "Microsoft", "Intel", "AMD", "Seagate", "Western Digital", "Anker", "Epson"
        );
        List<Brand> brands = new ArrayList<>();
        for(String brandName : electronicsBrandNames){
            Brand brand = new Brand();
            brand.setBrandName(brandName);
            brand.setDescription(null);
            brand.setActive(true);
            brands.add(brand);
        }
        brandRepository.saveAll(brands);
        log.info("Fake {} brand in: " + ((System.nanoTime() - start)/1_000_000) + "ms", electronicsBrandNames.size());

    }

    @Transactional
    public void generateAndInsertFakeAttributeData() {
        long start = System.nanoTime();
        // Danh sách thuộc tính và giá trị thực tế
        Map<String, List<String>> attributesData = new HashMap<>();
        attributesData.put("Màu sắc", Arrays.asList("Đen", "Trắng", "Đỏ", "Xanh dương", "Xanh lá", "Vàng", "Hồng", "Bạc", "Xám", "Tím"));
        attributesData.put("Kích thước màn hình", Arrays.asList("5 inch", "6 inch", "6.5 inch", "7 inch", "14 inch", "15.6 inch", "17 inch"));
        attributesData.put("Dung lượng pin", Arrays.asList("3000mAh", "4000mAh", "4500mAh", "5000mAh", "6000mAh"));
        attributesData.put("Hệ điều hành", Arrays.asList("Android", "iOS", "Windows", "macOS", "Linux", "HarmonyOS"));
        attributesData.put("Thương hiệu", Arrays.asList("Samsung", "Apple", "Xiaomi", "Oppo", "Vivo", "Sony", "Dell", "HP", "Asus", "Acer"));
        attributesData.put("Độ phân giải màn hình", Arrays.asList("HD", "Full HD", "2K", "4K", "8K"));
        attributesData.put("Dung lượng RAM", Arrays.asList("2GB", "4GB", "6GB", "8GB", "12GB", "16GB", "32GB"));
        attributesData.put("Dung lượng bộ nhớ trong", Arrays.asList("32GB", "64GB", "128GB", "256GB", "512GB", "1TB", "2TB"));
        attributesData.put("Loại kết nối", Arrays.asList("Wi-Fi", "4G", "5G", "Bluetooth", "USB-C", "HDMI", "NFC"));
        attributesData.put("Tốc độ xử lý (CPU)", Arrays.asList("1.8GHz", "2.0GHz", "2.5GHz", "3.0GHz", "3.5GHz", "4.0GHz"));
        attributesData.put("Loại màn hình", Arrays.asList("LCD", "LED", "OLED", "AMOLED", "Retina"));
        attributesData.put("Tần số quét màn hình", Arrays.asList("60Hz", "90Hz", "120Hz", "144Hz", "240Hz"));
        attributesData.put("Chống nước", Arrays.asList("IP67", "IP68", "IPX4", "Không chống nước"));
        attributesData.put("Thời gian sử dụng pin", Arrays.asList("8 giờ", "10 giờ", "12 giờ", "15 giờ", "20 giờ"));
        attributesData.put("Loại cổng sạc", Arrays.asList("Micro USB", "USB-C", "Lightning"));
        attributesData.put("Công nghệ âm thanh", Arrays.asList("Dolby Atmos", "Hi-Res Audio", "3D Surround Sound", "Không hỗ trợ"));
        attributesData.put("Hỗ trợ thẻ nhớ", Arrays.asList("Có", "Không"));
        attributesData.put("Loại pin", Arrays.asList("Li-ion", "Li-polymer", "NiMH"));
        attributesData.put("Thời gian sạc đầy pin", Arrays.asList("1 giờ", "1.5 giờ", "2 giờ", "2.5 giờ"));
        attributesData.put("Loại cảm biến", Arrays.asList("Vân tay", "Face ID", "Ánh sáng", "Gia tốc", "Con quay hồi chuyển"));
        attributesData.put("Công nghệ GPU", Arrays.asList("Adreno", "Mali", "PowerVR", "Apple GPU"));
        attributesData.put("Công nghệ chống rung", Arrays.asList("OIS", "EIS", "Không hỗ trợ"));
        attributesData.put("Hỗ trợ định vị GPS", Arrays.asList("GPS", "GLONASS", "BeiDou", "Galileo"));
        attributesData.put("Loại chipset", Arrays.asList("Snapdragon", "Exynos", "MediaTek", "Apple A-Series", "Kirin"));
        attributesData.put("Phiên bản Bluetooth", Arrays.asList("4.0", "4.1", "4.2", "5.0", "5.1", "5.2"));
        attributesData.put("Khối lượng sản phẩm", Arrays.asList("1kg", "1.5kg", "2kg", "2.5kg", "3kg"));
        attributesData.put("Loại ổ cứng", Arrays.asList("HDD", "SSD", "eMMC"));
        attributesData.put("Chất liệu vỏ", Arrays.asList("Nhựa", "Kim loại", "Hợp kim nhôm", "Thủy tinh"));
        attributesData.put("Công suất tiêu thụ", Arrays.asList("50W", "75W", "100W", "150W", "200W"));
        attributesData.put("Độ sáng màn hình", Arrays.asList("250 nits", "300 nits", "400 nits", "500 nits", "600 nits"));
        attributesData.put("Hỗ trợ HDR", Arrays.asList("HDR10", "HDR10+", "Dolby Vision", "Không hỗ trợ"));
        attributesData.put("Loại phím bấm", Arrays.asList("Cơ học", "Membrane", "Scissor Switch"));
        attributesData.put("Độ nhạy cảm biến", Arrays.asList("100 DPI", "400 DPI", "800 DPI", "1600 DPI", "3200 DPI"));
        attributesData.put("Loại tai nghe", Arrays.asList("In-ear", "Over-ear", "On-ear", "True Wireless"));
        attributesData.put("Chất lượng camera", Arrays.asList("5MP", "8MP", "12MP", "48MP", "108MP"));
        attributesData.put("Khả năng quay video", Arrays.asList("1080p", "4K", "8K", "Không hỗ trợ"));
        attributesData.put("Dung lượng lưu trữ đám mây", Arrays.asList("5GB", "15GB", "50GB", "100GB", "1TB"));
        attributesData.put("Loại pin dự phòng", Arrays.asList("Li-ion", "Li-polymer"));
        attributesData.put("Thời gian bảo hành", Arrays.asList("6 tháng", "12 tháng", "24 tháng", "Không bảo hành"));
        attributesData.put("Loại đèn nền", Arrays.asList("RGB", "Single Color", "Không có đèn nền"));
        attributesData.put("Công nghệ sạc nhanh", Arrays.asList("Quick Charge 3.0", "Quick Charge 4.0", "PD", "Không hỗ trợ"));
        attributesData.put("Loại loa", Arrays.asList("2.1", "5.1", "7.1", "Mono", "Stereo"));
        attributesData.put("Độ bền sản phẩm", Arrays.asList("5 năm", "10 năm", "Không xác định"));
        attributesData.put("Tính năng đặc biệt", Arrays.asList("Chống nước", "Chống bụi", "Hỗ trợ 5G", "Hỗ trợ VR", "Hỗ trợ AR", "Không có tính năng đặc biệt"));
        attributesData.put("Chất liệu màn hình", Arrays.asList("Gorilla Glass", "Sapphire", "Acrylic", "Plastic", "Glass"));
        attributesData.put("Công nghệ màn hình", Arrays.asList("IPS", "OLED", "QLED", "AMOLED", "Super AMOLED"));
        attributesData.put("Cổng kết nối", Arrays.asList("HDMI", "VGA", "USB-C", "DisplayPort", "Thunderbolt"));
        attributesData.put("Loại bàn phím", Arrays.asList("Có dây", "Không dây", "Bluetooth", "Cơ học", "Membrane"));
        attributesData.put("Tần số quét", Arrays.asList("50Hz", "60Hz", "120Hz", "144Hz", "240Hz"));
        attributesData.put("Loại bộ vi xử lý", Arrays.asList("Intel Core i3", "Intel Core i5", "Intel Core i7", "Intel Core i9", "AMD Ryzen 5", "AMD Ryzen 7"));
        attributesData.put("Chất liệu thân máy", Arrays.asList("Nhôm", "Nhựa ABS", "Thép không gỉ", "Carbon Fiber", "Magnesium Alloy"));
        attributesData.put("Độ phân giải camera", Arrays.asList("720p", "1080p", "4K", "8K"));
        attributesData.put("Dung lượng lưu trữ SSD", Arrays.asList("128GB", "256GB", "512GB", "1TB", "2TB"));
        attributesData.put("Kích thước sản phẩm", Arrays.asList("Nhỏ", "Trung bình", "Lớn", "Rất lớn"));
        attributesData.put("Loại tản nhiệt", Arrays.asList("Quạt", "Liquid Cooling", "Passive Cooling"));
        attributesData.put("Công nghệ sạc không dây", Arrays.asList("Qi", "MagSafe", "Không hỗ trợ"));
        attributesData.put("Độ dày sản phẩm", Arrays.asList("10mm", "15mm", "20mm", "25mm", "30mm"));
        attributesData.put("Thời gian sử dụng tối đa", Arrays.asList("4 giờ", "6 giờ", "8 giờ", "10 giờ", "12 giờ"));
        attributesData.put("Loại loa ngoài", Arrays.asList("Stereo", "Mono", "Surround", "Không có loa ngoài"));
        attributesData.put("Loại cổng tai nghe", Arrays.asList("3.5mm", "USB-C", "Lightning", "Không có cổng tai nghe"));
        attributesData.put("Loại kính bảo vệ", Arrays.asList("Gorilla Glass 3", "Gorilla Glass 5", "Victus", "Sapphire"));
        attributesData.put("Loại kết nối Internet", Arrays.asList("Ethernet", "Wi-Fi", "4G", "5G"));
        attributesData.put("Công nghệ bảo mật", Arrays.asList("Vân tay", "Face ID", "Mã PIN", "Password"));
        attributesData.put("Loại cảm ứng", Arrays.asList("Điện dung", "Điện trở", "Optical", "Không cảm ứng"));
        attributesData.put("Định dạng video hỗ trợ", Arrays.asList("MP4", "AVI", "MKV", "MOV", "WMV"));
        attributesData.put("Định dạng âm thanh hỗ trợ", Arrays.asList("MP3", "AAC", "FLAC", "WAV", "OGG"));
        attributesData.put("Khả năng mở rộng bộ nhớ", Arrays.asList("Có", "Không"));
        attributesData.put("Loại đèn flash", Arrays.asList("LED", "Xenon", "Không có flash"));
        attributesData.put("Chức năng hỗ trợ game", Arrays.asList("FPS Booster", "Cooling System", "Customizable Buttons"));
        attributesData.put("Chế độ tiết kiệm pin", Arrays.asList("Tiết kiệm tối đa", "Tự động", "Không hỗ trợ"));
        attributesData.put("Công nghệ quét vân tay", Arrays.asList("Optical", "Ultrasonic", "Side-mounted", "Rear-mounted"));
        attributesData.put("Hỗ trợ mạng", Arrays.asList("GSM", "CDMA", "LTE", "NR"));

        // Lặp qua danh sách để tạo Attribute và AttributeValue
        for (Map.Entry<String, List<String>> entry : attributesData.entrySet()) {
            String attributeName = entry.getKey();
            List<String> values = entry.getValue();

            // Tạo và lưu Attribute
            Attribute attribute = new Attribute();
            attribute.setAttributeName(attributeName);

            attribute = attributeRepository.save(attribute);

            // Tạo và lưu AttributeValue
            List<String> valueJsonList = new ArrayList<>();
            List<AttributeValue> attributeValues = new ArrayList<>();
            for (String value : values) {
                AttributeValue attributeValue = new AttributeValue();
                attributeValue.setValue(value);
                attributeValue.setAttribute(attribute);
                // Thêm vào danh sách JSON
                valueJsonList.add(value);
                attributeValues.add(attributeValue);
            }
            attributeValueRepository.saveAll(attributeValues);

            // Cập nhật ValueJson trong Attribute
            try {
                String valueJson = jsonConvert.convertListObjectToJson(valueJsonList);
                attribute.setValueJson(valueJson);
                attributeRepository.save(attribute);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error while converting valueJson to JSON format", e);
            }
        }
        log.info("Fake {} attribute in: " + ((System.nanoTime() - start)/1_000_000) + "ms", attributesData.size());
    }

    @Transactional
    public Product createProduct(Shop shop) {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2011%20Pro/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2011%20Pro/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/Iphone%2011%20Pro%20max/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012%20Pro/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012%20Pro%20Max/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013%20Pro/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013%20Pro%20Max/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/7.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%202%20SIM/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%204G%20Pro/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20110%204G%20Pro/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20110%204G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20215%204G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20C21%20Plus%203GB/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20C32%204GB/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%205G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20T%204G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/6.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/7.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%202%20SIM/6.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/6.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/7.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/6.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/7.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20S22%20Ultra/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20S22%20Ultra/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/6.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/7.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/2.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/3.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/4.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/5.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%20POCO%20X6%20Pro%205G/1.jpg?raw=true");
        imageUrls.add("https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%20POCO%20X6%20Pro%205G/2.jpg?raw=true");
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.findRandomCategory();
            ShopCategory shopCategory = shopCategoryRepository.findRandomShopCategoriesByShop(shop.getId()).get(0);
            Brand brand = brandRepository.findRandomBrand();

            // Tạo breadcrumbs cho danh mục
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

            // Tạo đối tượng sản phẩm mới
            Product product = new Product();
            product.setBreadcrumb(objectMapper.writeValueAsString(breadcrumbs));
            String title = faker.lorem().sentence(random.nextInt(4, 8));
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            product.setDescription(faker.lorem().sentence(40));
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setIsAccepted(random.nextBoolean());
            product.setProductState(Product.ProductState.ACTIVE);
            product.setCategory(category);
            product.setBrandOfProduct(brand);
            product.setProductCode("PID" + randomUtils.generateRandomString(15));
            product.setShopOfProduct(shop);
            product.setSoldCount(random.nextInt(10, 1000));
            product.setRating(faker.number().randomDouble(1, 1, 5));

            // Tạo sản phẩm hình ảnh
            ProductImage image = new ProductImage();
            image.setImageUrl(imageUrls.get(random.nextInt(0, imageUrls.size())));
            image.setActive(true);
            image.setIsMainImage(true);
            image.setProductOfProductImage(product);
            List<String> images = List.of(image).stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
            product.setListImageOfProduct(List.of(image));
            product.setImageList(objectMapper.writeValueAsString(images));

            // Bước 2: Lấy các thuộc tính và giá trị ngẫu nhiên
            List<Attribute> attributeList = attributeRepository.findRandomAttributes(2);
            Map<Attribute, List<AttributeValue>> mapAttribute = new HashMap<>();
            for (Attribute attribute : attributeList) {
                List<AttributeValue> attributeValues = attributeValueRepository
                        .findRandomAttributesValue(attribute.getId(), 2); // 2 giá trị thuộc tính
                mapAttribute.put(attribute, attributeValues);
            }

            // Bước 3: Tạo tất cả các biến thể sản phẩm
            Set<String> pairedCombinations = new HashSet<>();
            List<BigDecimal> prices = new ArrayList<>();

            // Tạo một danh sách các danh sách các giá trị thuộc tính
            List<List<AttributeValue>> attributeValueLists = new ArrayList<>();

            // Lấy các giá trị của từng thuộc tính
            for (Attribute attribute : attributeList) {
                List<AttributeValue> attributeValues = mapAttribute.get(attribute);
                attributeValueLists.add(attributeValues);
            }

            // Sử dụng một vòng lặp để kết hợp tất cả các giá trị của các thuộc tính
            // Tạo tất cả các kết hợp của giá trị thuộc tính
            for (int i = 0; i < attributeValueLists.get(0).size(); i++) {
                for (int j = 0; j < attributeValueLists.get(1).size(); j++) {
                    // Nếu bạn có 2 thuộc tính, bạn sẽ tạo các kết hợp của các giá trị tương ứng từ 2 danh sách thuộc tính
                    AttributeValue value1 = attributeValueLists.get(0).get(i);
                    AttributeValue value2 = attributeValueLists.get(1).get(j);

                    String pair = value1.getId() + ":" + value2.getId(); // Kết hợp ID của các giá trị
                    if (!pairedCombinations.contains(pair)) {
                        pairedCombinations.add(pair);

                        // Tạo một biến thể sản phẩm mới cho kết hợp này
                        ProductVariant variant = new ProductVariant();
                        long min = 50000;
                        long max = 10000000;
                        long randomValue = min + (long) (Math.random() * (max - min));
                        randomValue = (randomValue / 1000) * 1000; // Làm tròn xuống 1000
                        BigDecimal price = BigDecimal.valueOf(randomValue);
                        variant.setPrice(price);
                        prices.add(price);
                        variant.setStockQuantity(faker.number().randomDigitNotZero());
                        variant.setSku(faker.number().digits(10));
                        variant.setHeight(10.0);
                        variant.setWeight(1.0);
                        variant.setLength(10.0);
                        variant.setWidth(10.0);
                        variant.setStatus(ProductVariant.Status.CON_HANG);
                        variant.setProduct(product);
                        product.getVariants().add(variant);

                        // Thêm các giá trị thuộc tính vào biến thể
                        VariantAttributeValue variantAttributeValue1 = new VariantAttributeValue();
                        variantAttributeValue1.setAttribute(attributeList.get(0));
                        variantAttributeValue1.setAttributeValue(value1);
                        variantAttributeValue1.setVariant(variant);
                        variant.getVariantAttributeValueList().add(variantAttributeValue1);
                        VariantAttributeValue variantAttributeValue2 = new VariantAttributeValue();
                        variantAttributeValue2.setAttribute(attributeList.get(1));
                        variantAttributeValue2.setAttributeValue(value2);
                        variantAttributeValue2.setVariant(variant);
                        variant.getVariantAttributeValueList().add(variantAttributeValue2);

                        ProductAttributeValue productAttributeValue1 = new ProductAttributeValue();
                        productAttributeValue1.setAttribute(attributeList.get(0));
                        productAttributeValue1.setAttributeValue(value1);
                        productAttributeValue1.setProduct(product);
                        product.getProductAttributeValues().add(productAttributeValue1);
                        ProductAttributeValue productAttributeValue2 = new ProductAttributeValue();
                        productAttributeValue2.setAttribute(attributeList.get(1));
                        productAttributeValue2.setAttributeValue(value2);
                        productAttributeValue2.setProduct(product);
                        product.getProductAttributeValues().add(productAttributeValue2);

                        String variantName = attributeList.get(0).getAttributeName() + ": " +  value1.getValue() +
                                ", " + attributeList.get(1).getAttributeName() + ": " + value2.getValue();
                        variant.setVariantSlug(StringFormatUtils.convertToSlug(variantName));
                        variant.setProductVariation(variantName);
                    }
                }
            }

            // Bước 4: Xác định giá trị min và max của sản phẩm
            List<BigDecimal> validPrices = prices.stream().filter(Objects::nonNull).collect(Collectors.toList());
            product.setMinInitialPrice(validPrices.stream().min(BigDecimal::compareTo).orElse(null));
            product.setMaxInitialPrice(validPrices.stream().max(BigDecimal::compareTo).orElse(null));

            product.setVariations(jsonConvert.convertListObjectToJson(product.getVariants()));
            // Lưu sản phẩm vào cơ sở dữ liệu
            productRepository.save(product);

            // Lưu thông tin danh mục cho sản phẩm
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category);
            categoryProduct.setProduct(product);
            categoryProductRepository.save(categoryProduct);

            // Lưu thông tin danh mục cửa hàng cho sản phẩm
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
    public void fakeShopReview() {
        log.info("Start fake shop review...");
        long startTime = System.currentTimeMillis();
        List<Shop> shops = shopRepository.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            for (Shop shop : shops) {
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                List<Users> users = usersRepository.findRandomUsers(random.nextInt(0, 30));
                for (Users user : users) {
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                        ShopReview shopReview = new ShopReview();
                        shopReview.setShopOfShopReview(shop);
                        shopReview.setUsersOfShopReview(user);
                        shopReview.setActive(true);
                        shopReview.setRating(faker.number().numberBetween(1, 6));
                        shopReview.setComment(faker.lorem().sentence(random.nextInt(1, 20)));
                        boolean shopReply = faker.bool().bool();
                        shopReview.setIsReply(shopReply);
                        if (shopReply) {
                            shopReview.setReply(faker.lorem().sentence(random.nextInt(1, 20)));
                        }
                        return shopReview;
                    }, executorService).thenAcceptAsync(shopReviewRepository::save, executorService);
                    futures.add(future);
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        } finally {
            executorService.shutdown();
            log.info("End fake shop review in " + (System.currentTimeMillis() - startTime) + "ms");
        }
    }

    @Transactional
    public void fakeProductReview() {
        log.info("Start fake product review...");
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(10);  // Thread pool để xử lý bất đồng bộ
        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Product product : products) {
                List<Users> users = usersRepository.findRandomUsers(random.nextInt(0, 30));
                for (Users user : users) {
                    // Lưu review vào cơ sở dữ liệu
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                        ProductReview productReview = new ProductReview();
                        productReview.setProductOfProductReview(product);
                        productReview.setUsersOfProductReview(user);
                        productReview.setActive(true);
                        productReview.setRating(faker.number().numberBetween(1, 5));
                        productReview.setTextComment(faker.lorem().sentence(random.nextInt(1, 20)));
                        productReview.setTag(faker.lorem().word() + ", " + faker.lorem().word() + ", " + faker.lorem().word());
                        productReview.setImageComment(faker.internet().image());
                        boolean shopReply = faker.bool().bool();
                        productReview.setIsShopReply(shopReply);
                        if (shopReply) {
                            productReview.setShopReply(faker.lorem().sentence());
                        } else {
                            productReview.setShopReply(null);
                        }
                        return productReview;
                    }, executorService)
                            .thenAcceptAsync(productReviewRepository::save, executorService);
                    futures.add(future);
                }
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executorService.shutdown();
            log.info("End fake product review in " + (System.currentTimeMillis() - startTime) + "ms");
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
                                    productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
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
}
