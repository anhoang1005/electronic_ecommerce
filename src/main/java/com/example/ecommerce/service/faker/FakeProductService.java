package com.example.ecommerce.service.faker;

import co.elastic.clients.util.Pair;
import com.example.ecommerce.entities.*;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.models.category.CategoryBreadcrumb;
import com.example.ecommerce.rabbit.ProductProducer;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FakeProductService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private final Faker faker = new Faker(new Locale("vi"));
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final BrandRepository brandRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ShopCategoryProductRepository shopCategoryProductRepository;

    private final ProductProducer productProducer;
    private final ProductMapper productMapper;

    private final RandomUtils randomUtils;
    private final JsonConvert jsonConvert;

    @Transactional
    public void generateAndInsertFakeBrand() {
        long start = System.nanoTime();
        List<Pair<String, String>> phonePairList = Arrays.asList(
                Pair.of("iPhone 14", "Apple"),
                Pair.of("Samsung Galaxy S22", "Samsung"),
                Pair.of("Google Pixel 6", "Google"),
                Pair.of("OnePlus 9 Pro", "OnePlus"),
                Pair.of("Xiaomi Mi 11", "Xiaomi"),
                Pair.of("Oppo Find X3 Pro", "Oppo"),
                Pair.of("Sony Xperia 1 III", "Sony"),
                Pair.of("Motorola Edge 20 Pro", "Motorola"),
                Pair.of("Vivo X60 Pro", "Vivo"),
                Pair.of("Huawei P50 Pro", "Huawei"),
                Pair.of("Realme GT", "Realme"),
                Pair.of("Asus ROG Phone 5", "Asus"),
                Pair.of("iPhone 13", "Apple"),
                Pair.of("Samsung Galaxy Z Fold 3", "Samsung"),
                Pair.of("Google Pixel 5", "Google"),
                Pair.of("OnePlus 9", "OnePlus"),
                Pair.of("Xiaomi Redmi Note 10 Pro", "Xiaomi"),
                Pair.of("Oppo Reno 6 Pro", "Oppo"),
                Pair.of("Sony Xperia 5 II", "Sony"),
                Pair.of("Motorola Edge Plus", "Motorola"),
                Pair.of("Vivo V21 5G", "Vivo"),
                Pair.of("Huawei Mate 40 Pro", "Huawei"),
                Pair.of("Realme 8 Pro", "Realme"),
                Pair.of("Asus Zenfone 8", "Asus"),
                Pair.of("iPhone SE (2020)", "Apple"),
                Pair.of("Samsung Galaxy S21", "Samsung"),
                Pair.of("Google Pixel 4a", "Google"),
                Pair.of("OnePlus 8T", "OnePlus"),
                Pair.of("Xiaomi Mi 10", "Xiaomi"),
                Pair.of("Oppo F19 Pro", "Oppo"),
                Pair.of("Sony Xperia 10 III", "Sony"),
                Pair.of("Motorola Moto G Power", "Motorola"),
                Pair.of("Vivo Y73", "Vivo"),
                Pair.of("Huawei Nova 8i", "Huawei"),
                Pair.of("Realme Narzo 30 Pro", "Realme"),
                Pair.of("Asus ROG Phone 3", "Asus"),
                Pair.of("iPhone 12", "Apple"),
                Pair.of("Samsung Galaxy A72", "Samsung"),
                Pair.of("Google Pixel 4 XL", "Google"),
                Pair.of("OnePlus Nord 2", "OnePlus"),
                Pair.of("Xiaomi Poco X3 Pro", "Xiaomi"),
                Pair.of("Oppo A94 5G", "Oppo"),
                Pair.of("Sony Xperia L4", "Sony"),
                Pair.of("Motorola Moto G100", "Motorola"),
                Pair.of("Vivo Y20s", "Vivo"),
                Pair.of("Huawei P40 Pro", "Huawei"),
                Pair.of("Realme GT 5G", "Realme"),
                Pair.of("Asus Zenfone 7 Pro", "Asus"),
                Pair.of("iPhone 11", "Apple"),
                Pair.of("Samsung Galaxy A52", "Samsung"),
                Pair.of("Google Pixel 3a", "Google"),
                Pair.of("OnePlus Nord CE 5G", "OnePlus"),
                Pair.of("Xiaomi Mi 9T Pro", "Xiaomi"),
                Pair.of("Oppo F17 Pro", "Oppo"),
                Pair.of("Sony Xperia 1 II", "Sony"),
                Pair.of("Motorola Moto E 2020", "Motorola"),
                Pair.of("Vivo Y51", "Vivo"),
                Pair.of("Huawei Honor 30 Pro", "Huawei"),
                Pair.of("Realme X50 Pro 5G", "Realme"),
                Pair.of("Asus Zenfone 6", "Asus"),
                Pair.of("iPhone XR", "Apple"),
                Pair.of("Samsung Galaxy Note 20 Ultra", "Samsung"),
                Pair.of("Google Pixel 3", "Google"),
                Pair.of("OnePlus 7T Pro", "OnePlus"),
                Pair.of("Xiaomi Mi Mix 4", "Xiaomi"),
                Pair.of("Oppo Reno 4 Pro", "Oppo"),
                Pair.of("Sony Xperia 5", "Sony"),
                Pair.of("Motorola Moto Z4", "Motorola"),
                Pair.of("Vivo X50 Pro", "Vivo"),
                Pair.of("Huawei P30 Pro", "Huawei"),
                Pair.of("Realme X3 SuperZoom", "Realme"),
                Pair.of("Asus Zenfone 5Z", "Asus"),
                Pair.of("iPhone X", "Apple"),
                Pair.of("Samsung Galaxy S20", "Samsung"),
                Pair.of("Google Pixel 2 XL", "Google"),
                Pair.of("OnePlus 6T", "OnePlus"),
                Pair.of("Xiaomi Mi 8", "Xiaomi"),
                Pair.of("Oppo F11 Pro", "Oppo"),
                Pair.of("Sony Xperia XZ2", "Sony"),
                Pair.of("Motorola Moto G6", "Motorola"),
                Pair.of("Vivo V15 Pro", "Vivo"),
                Pair.of("Huawei Mate 20 Pro", "Huawei"),
                Pair.of("Realme 7 Pro", "Realme"),
                Pair.of("Asus Zenfone 4 Pro", "Asus"),
                Pair.of("iPhone 8 Plus", "Apple"),
                Pair.of("Samsung Galaxy A51", "Samsung"),
                Pair.of("Google Pixel 2", "Google"),
                Pair.of("OnePlus 5T", "OnePlus"),
                Pair.of("Xiaomi Mi A3", "Xiaomi"),
                Pair.of("Oppo F9 Pro", "Oppo"),
                Pair.of("Sony Xperia XZ1", "Sony"),
                Pair.of("Motorola Moto X4", "Motorola"),
                Pair.of("Vivo V11 Pro", "Vivo"),
                Pair.of("Huawei P20 Pro", "Huawei"),
                Pair.of("Realme 6 Pro", "Realme"),
                Pair.of("Asus Zenfone 3 Deluxe", "Asus"),
                Pair.of("iPhone 7", "Apple"),
                Pair.of("Samsung Galaxy S9", "Samsung"),
                Pair.of("Google Nexus 5X", "Google"),
                Pair.of("OnePlus 2", "OnePlus"),
                Pair.of("Xiaomi Mi 6", "Xiaomi"),
                Pair.of("Oppo F3 Plus", "Oppo"),
                Pair.of("Sony Xperia Z5", "Sony"),
                Pair.of("Motorola Moto G5 Plus", "Motorola"),
                Pair.of("Vivo V5 Plus", "Vivo"),
                Pair.of("Huawei P10", "Huawei"),
                Pair.of("Realme 3 Pro", "Realme"),
                Pair.of("Asus Zenfone 4", "Asus")
        );

        List<Pair<String, String>> laptopPairList = Arrays.asList(
                Pair.of("Apple MacBook Air M2", "Apple"),
                Pair.of("Apple MacBook Pro 16-inch M2", "Apple"),
                Pair.of("Dell XPS 13 Plus", "Dell"),
                Pair.of("Dell XPS 15", "Dell"),
                Pair.of("HP Spectre x360 14", "HP"),
                Pair.of("HP Envy 13", "HP"),
                Pair.of("Lenovo ThinkPad X1 Carbon Gen 11", "Lenovo"),
                Pair.of("Lenovo Yoga 9i", "Lenovo"),
                Pair.of("Asus ZenBook Duo 14", "Asus"),
                Pair.of("Asus ROG Zephyrus G14", "Asus"),
                Pair.of("Microsoft Surface Laptop Studio", "Microsoft"),
                Pair.of("Microsoft Surface Pro 9", "Microsoft"),
                Pair.of("Razer Blade 16", "Razer"),
                Pair.of("Razer Blade Stealth 13", "Razer"),
                Pair.of("Acer Swift 3", "Acer"),
                Pair.of("Acer Predator Helios 300", "Acer"),
                Pair.of("MSI GE76 Raider", "MSI"),
                Pair.of("MSI Prestige 14 Evo", "MSI"),
                Pair.of("Samsung Galaxy Book Pro 360", "Samsung"),
                Pair.of("Samsung Galaxy Book3 Ultra", "Samsung"),
                Pair.of("Gigabyte Aero 15 OLED", "Gigabyte"),
                Pair.of("Gigabyte G5", "Gigabyte"),
                Pair.of("Huawei MateBook X Pro", "Huawei"),
                Pair.of("Huawei MateBook D15", "Huawei"),
                Pair.of("LG Gram 17", "LG"),
                Pair.of("LG Gram Style", "LG"),
                Pair.of("Alienware x17 R2", "Alienware"),
                Pair.of("Alienware m15 R7", "Alienware"),
                Pair.of("Google Pixelbook Go", "Google"),
                Pair.of("Framework Laptop 13", "Framework"),
                Pair.of("Chuwi CoreBook X", "Chuwi"),
                Pair.of("Xiaomi Mi Notebook Pro", "Xiaomi"),
                Pair.of("Xiaomi RedmiBook 15", "Xiaomi"),
                Pair.of("Vaio Z", "Vaio"),
                Pair.of("Vaio SX14", "Vaio"),
                Pair.of("Toshiba Dynabook Portégé X30L-G", "Toshiba"),
                Pair.of("Toshiba Tecra A50-J", "Toshiba")
        );


        List<Brand> brands = new ArrayList<>();
        Set<String> name = new HashSet<>();

        for (Pair<String, String> phone : phonePairList) {
            if(!name.contains(phone.value())){
                Brand brand = new Brand();
                brand.setBrandName(phone.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(phone.value());
            }
        }
        for (Pair<String, String> laptop : laptopPairList) {
            if(!name.contains(laptop.value())){
                Brand brand = new Brand();
                brand.setBrandName(laptop.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(laptop.value());
            }
        }
        brandRepository.saveAll(brands);
        log.info("Fake {} brand in: " + ((System.nanoTime() - start) / 1_000_000) + "ms", name.size());
    }

    @Transactional
    public Product createPhoneProduct(Shop shop) {
        List<Pair<String, String>> phonePairList = Arrays.asList(
                Pair.of("iPhone 14", "Apple"),
                Pair.of("Samsung Galaxy S22", "Samsung"),
                Pair.of("Google Pixel 6", "Google"),
                Pair.of("OnePlus 9 Pro", "OnePlus"),
                Pair.of("Xiaomi Mi 11", "Xiaomi"),
                Pair.of("Oppo Find X3 Pro", "Oppo"),
                Pair.of("Sony Xperia 1 III", "Sony"),
                Pair.of("Motorola Edge 20 Pro", "Motorola"),
                Pair.of("Vivo X60 Pro", "Vivo"),
                Pair.of("Huawei P50 Pro", "Huawei"),
                Pair.of("Realme GT", "Realme"),
                Pair.of("Asus ROG Phone 5", "Asus"),
                Pair.of("iPhone 13", "Apple"),
                Pair.of("Samsung Galaxy Z Fold 3", "Samsung"),
                Pair.of("Google Pixel 5", "Google"),
                Pair.of("OnePlus 9", "OnePlus"),
                Pair.of("Xiaomi Redmi Note 10 Pro", "Xiaomi"),
                Pair.of("Oppo Reno 6 Pro", "Oppo"),
                Pair.of("Sony Xperia 5 II", "Sony"),
                Pair.of("Motorola Edge Plus", "Motorola"),
                Pair.of("Vivo V21 5G", "Vivo"),
                Pair.of("Huawei Mate 40 Pro", "Huawei"),
                Pair.of("Realme 8 Pro", "Realme"),
                Pair.of("Asus Zenfone 8", "Asus"),
                Pair.of("iPhone SE (2020)", "Apple"),
                Pair.of("Samsung Galaxy S21", "Samsung"),
                Pair.of("Google Pixel 4a", "Google"),
                Pair.of("OnePlus 8T", "OnePlus"),
                Pair.of("Xiaomi Mi 10", "Xiaomi"),
                Pair.of("Oppo F19 Pro", "Oppo"),
                Pair.of("Sony Xperia 10 III", "Sony"),
                Pair.of("Motorola Moto G Power", "Motorola"),
                Pair.of("Vivo Y73", "Vivo"),
                Pair.of("Huawei Nova 8i", "Huawei"),
                Pair.of("Realme Narzo 30 Pro", "Realme"),
                Pair.of("Asus ROG Phone 3", "Asus"),
                Pair.of("iPhone 12", "Apple"),
                Pair.of("Samsung Galaxy A72", "Samsung"),
                Pair.of("Google Pixel 4 XL", "Google"),
                Pair.of("OnePlus Nord 2", "OnePlus"),
                Pair.of("Xiaomi Poco X3 Pro", "Xiaomi"),
                Pair.of("Oppo A94 5G", "Oppo"),
                Pair.of("Sony Xperia L4", "Sony"),
                Pair.of("Motorola Moto G100", "Motorola"),
                Pair.of("Vivo Y20s", "Vivo"),
                Pair.of("Huawei P40 Pro", "Huawei"),
                Pair.of("Realme GT 5G", "Realme"),
                Pair.of("Asus Zenfone 7 Pro", "Asus"),
                Pair.of("iPhone 11", "Apple"),
                Pair.of("Samsung Galaxy A52", "Samsung"),
                Pair.of("Google Pixel 3a", "Google"),
                Pair.of("OnePlus Nord CE 5G", "OnePlus"),
                Pair.of("Xiaomi Mi 9T Pro", "Xiaomi"),
                Pair.of("Oppo F17 Pro", "Oppo"),
                Pair.of("Sony Xperia 1 II", "Sony"),
                Pair.of("Motorola Moto E 2020", "Motorola"),
                Pair.of("Vivo Y51", "Vivo"),
                Pair.of("Huawei Honor 30 Pro", "Huawei"),
                Pair.of("Realme X50 Pro 5G", "Realme"),
                Pair.of("Asus Zenfone 6", "Asus"),
                Pair.of("iPhone XR", "Apple"),
                Pair.of("Samsung Galaxy Note 20 Ultra", "Samsung"),
                Pair.of("Google Pixel 3", "Google"),
                Pair.of("OnePlus 7T Pro", "OnePlus"),
                Pair.of("Xiaomi Mi Mix 4", "Xiaomi"),
                Pair.of("Oppo Reno 4 Pro", "Oppo"),
                Pair.of("Sony Xperia 5", "Sony"),
                Pair.of("Motorola Moto Z4", "Motorola"),
                Pair.of("Vivo X50 Pro", "Vivo"),
                Pair.of("Huawei P30 Pro", "Huawei"),
                Pair.of("Realme X3 SuperZoom", "Realme"),
                Pair.of("Asus Zenfone 5Z", "Asus"),
                Pair.of("iPhone X", "Apple"),
                Pair.of("Samsung Galaxy S20", "Samsung"),
                Pair.of("Google Pixel 2 XL", "Google"),
                Pair.of("OnePlus 6T", "OnePlus"),
                Pair.of("Xiaomi Mi 8", "Xiaomi"),
                Pair.of("Oppo F11 Pro", "Oppo"),
                Pair.of("Sony Xperia XZ2", "Sony"),
                Pair.of("Motorola Moto G6", "Motorola"),
                Pair.of("Vivo V15 Pro", "Vivo"),
                Pair.of("Huawei Mate 20 Pro", "Huawei"),
                Pair.of("Realme 7 Pro", "Realme"),
                Pair.of("Asus Zenfone 4 Pro", "Asus"),
                Pair.of("iPhone 8 Plus", "Apple"),
                Pair.of("Samsung Galaxy A51", "Samsung"),
                Pair.of("Google Pixel 2", "Google"),
                Pair.of("OnePlus 5T", "OnePlus"),
                Pair.of("Xiaomi Mi A3", "Xiaomi"),
                Pair.of("Oppo F9 Pro", "Oppo"),
                Pair.of("Sony Xperia XZ1", "Sony"),
                Pair.of("Motorola Moto X4", "Motorola"),
                Pair.of("Vivo V11 Pro", "Vivo"),
                Pair.of("Huawei P20 Pro", "Huawei"),
                Pair.of("Realme 6 Pro", "Realme"),
                Pair.of("Asus Zenfone 3 Deluxe", "Asus"),
                Pair.of("iPhone 7", "Apple"),
                Pair.of("Samsung Galaxy S9", "Samsung"),
                Pair.of("Google Nexus 5X", "Google"),
                Pair.of("OnePlus 2", "OnePlus"),
                Pair.of("Xiaomi Mi 6", "Xiaomi"),
                Pair.of("Oppo F3 Plus", "Oppo"),
                Pair.of("Sony Xperia Z5", "Sony"),
                Pair.of("Motorola Moto G5 Plus", "Motorola"),
                Pair.of("Vivo V5 Plus", "Vivo"),
                Pair.of("Huawei P10", "Huawei"),
                Pair.of("Realme 3 Pro", "Realme"),
                Pair.of("Asus Zenfone 4", "Asus")
        );
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
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Điện Thoại & Phụ Kiện", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Điện thoại di động", shop.getShopCode());
            Pair<String, String> pair1 = phonePairList.get(random.nextInt(0, phonePairList.size()));
            Brand brand = brandRepository.findBrandByBrandName(pair1.value());

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
            String title = pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            product.setDescription(faker.lorem().sentence(40));
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setIsAccepted(true);
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

                        String variantName = attributeList.get(0).getAttributeName() + ": " + value1.getValue() +
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
        } catch (
                Exception e) {
            throw new RuntimeException("Error while creating product", e);
        }
    }

    @Transactional
    public Product createLaptopProduct(Shop shop) {
        List<Pair<String, String>> laptopPairList = Arrays.asList(
                Pair.of("Apple MacBook Air M2", "Apple"),
                Pair.of("Apple MacBook Pro 16-inch M2", "Apple"),
                Pair.of("Dell XPS 13 Plus", "Dell"),
                Pair.of("Dell XPS 15", "Dell"),
                Pair.of("HP Spectre x360 14", "HP"),
                Pair.of("HP Envy 13", "HP"),
                Pair.of("Lenovo ThinkPad X1 Carbon Gen 11", "Lenovo"),
                Pair.of("Lenovo Yoga 9i", "Lenovo"),
                Pair.of("Asus ZenBook Duo 14", "Asus"),
                Pair.of("Asus ROG Zephyrus G14", "Asus"),
                Pair.of("Microsoft Surface Laptop Studio", "Microsoft"),
                Pair.of("Microsoft Surface Pro 9", "Microsoft"),
                Pair.of("Razer Blade 16", "Razer"),
                Pair.of("Razer Blade Stealth 13", "Razer"),
                Pair.of("Acer Swift 3", "Acer"),
                Pair.of("Acer Predator Helios 300", "Acer"),
                Pair.of("MSI GE76 Raider", "MSI"),
                Pair.of("MSI Prestige 14 Evo", "MSI"),
                Pair.of("Samsung Galaxy Book Pro 360", "Samsung"),
                Pair.of("Samsung Galaxy Book3 Ultra", "Samsung"),
                Pair.of("Gigabyte Aero 15 OLED", "Gigabyte"),
                Pair.of("Gigabyte G5", "Gigabyte"),
                Pair.of("Huawei MateBook X Pro", "Huawei"),
                Pair.of("Huawei MateBook D15", "Huawei"),
                Pair.of("LG Gram 17", "LG"),
                Pair.of("LG Gram Style", "LG"),
                Pair.of("Alienware x17 R2", "Alienware"),
                Pair.of("Alienware m15 R7", "Alienware"),
                Pair.of("Google Pixelbook Go", "Google"),
                Pair.of("Framework Laptop 13", "Framework"),
                Pair.of("Chuwi CoreBook X", "Chuwi"),
                Pair.of("Xiaomi Mi Notebook Pro", "Xiaomi"),
                Pair.of("Xiaomi RedmiBook 15", "Xiaomi"),
                Pair.of("Vaio Z", "Vaio"),
                Pair.of("Vaio SX14", "Vaio"),
                Pair.of("Toshiba Dynabook Portégé X30L-G", "Toshiba"),
                Pair.of("Toshiba Tecra A50-J", "Toshiba")
        );
        List<String> imageUrls = List.of(
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/macbook_air_13_m2_midnight_1_35053fbcf9.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/dell_latitude_15_3540_9950b79986.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_5_13_638512086854166670_8F157PA-thumb.jpg"
        );
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Máy Tính & Laptop", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Máy tính xách tay", shop.getShopCode());
            Pair<String, String> pair1 = laptopPairList.get(random.nextInt(0, laptopPairList.size()));
            Brand brand = brandRepository.findBrandByBrandName(pair1.value());

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
            String title = pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            product.setDescription(faker.lorem().sentence(40));
            product.setProductSlug(StringFormatUtils.convertToSlug(title));
            product.setCurrency("VND");
            product.setIsHidden(false);
            product.setIsAccepted(true);
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

                        String variantName = attributeList.get(0).getAttributeName() + ": " + value1.getValue() +
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
        } catch (
                Exception e) {
            throw new RuntimeException("Error while creating product", e);
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
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> createPhoneProduct(shop), executorService)
                            .thenAcceptAsync(product -> {
                                if (product != null) {
                                    //productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
                                }
                            }, rabbitMqExecutor);

                    futures.add(future);
                }
                for (int i = 0; i < countProduct; i++) {
                    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> createLaptopProduct(shop), executorService)
                            .thenAcceptAsync(product -> {
                                if (product != null) {
                                    //productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
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
