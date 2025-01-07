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

    private final List<Pair<String, String>> phonePairList = new ArrayList<>(Arrays.asList(
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
    ));
    private final List<Pair<String, String>> laptopPairList = new ArrayList<>(Arrays.asList(
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
    ));
    private final List<Pair<String, String>> tvProducts = new ArrayList<>(List.of(
            Pair.of("LG OLED55C1 55-Inch 4K Smart OLED TV", "LG"),
            Pair.of("Samsung QN90B Neo QLED 4K Smart TV", "Samsung"),
            Pair.of("Sony Bravia XR X90K 65-Inch Full Array LED TV", "Sony"),
            Pair.of("Sony A80J 55-Inch 4K OLED TV with Google TV", "Sony"),
            Pair.of("Samsung Q60A 50-Inch QLED 4K UHD Smart TV", "Samsung"),
            Pair.of("LG NanoCell75 65-Inch 4K UHD Smart TV", "LG"),
            Pair.of("Hisense U8H 55-Inch Mini-LED Quantum ULED 4K TV", "Hisense"),
            Pair.of("Vizio P-Series Quantum 65-Inch 4K HDR Smart TV", "Vizio"),
            Pair.of("Samsung The Frame 65-Inch 4K QLED Smart TV", "Samsung"),
            Pair.of("TCL C825 55-Inch Mini-LED 4K Android TV", "TCL"),
            Pair.of("Sony X95K 75-Inch Mini-LED 4K Smart TV", "Sony"),
            Pair.of("LG OLED77G2 77-Inch Gallery Edition 4K OLED TV", "LG"),
            Pair.of("Samsung QN800B 65-Inch Neo QLED 8K Smart TV", "Samsung"),
            Pair.of("Sony Bravia XR A95K 65-Inch 4K OLED TV", "Sony"),
            Pair.of("TCL 6-Series R646 65-Inch Mini-LED 4K Roku TV", "TCL"),
            Pair.of("Vizio M-Series Quantum 55-Inch 4K HDR Smart TV", "Vizio"),
            Pair.of("Hisense A6H 43-Inch 4K UHD Smart Google TV", "Hisense"),
            Pair.of("LG NanoCell90 75-Inch 4K UHD Smart TV", "LG"),
            Pair.of("Samsung TU8000 50-Inch Crystal UHD 4K Smart TV", "Samsung"),
            Pair.of("Sony X80K 43-Inch 4K Ultra HD LED Smart TV", "Sony"),
            Pair.of("TCL S535 50-Inch QLED 4K Roku Smart TV", "TCL"),
            Pair.of("Vizio D-Series 32-Inch Full HD Smart TV", "Vizio"),
            Pair.of("Hisense H9G 65-Inch Quantum Series 4K TV", "Hisense"),
            Pair.of("LG OLED65CXPUA 65-Inch 4K Smart OLED TV", "LG"),
            Pair.of("Samsung Serif QLED 43-Inch 4K UHD Smart TV", "Samsung"),
            Pair.of("Sony Bravia XR X95J 85-Inch 4K HDR Smart TV", "Sony"),
            Pair.of("TCL 4-Series S435 55-Inch 4K Roku Smart TV", "TCL"),
            Pair.of("Vizio OLED55-H1 55-Inch 4K Smart OLED TV", "Vizio"),
            Pair.of("Hisense L9G 100-Inch Laser TV with Screen", "Hisense"),
            Pair.of("Samsung QN85B 85-Inch Neo QLED 4K Smart TV", "Samsung")
    ));

    private final List<Pair<String, String>> watchProducts = new ArrayList<>(Arrays.asList(
            new Pair<>("Rolex Submariner Date", "Rolex"),
            new Pair<>("Omega Speedmaster Professional", "Omega"),
            new Pair<>("Tag Heuer Carrera Calibre 16", "Tag Heuer"),
            new Pair<>("Casio G-Shock GA-2100", "Casio"),
            new Pair<>("Seiko Prospex SRP777", "Seiko"),
            new Pair<>("Citizen Eco-Drive Promaster", "Citizen"),
            new Pair<>("Tissot Le Locle Powermatic 80", "Tissot"),
            new Pair<>("Fossil Townsman Chronograph", "Fossil"),
            new Pair<>("Hamilton Khaki Field Mechanical", "Hamilton"),
            new Pair<>("Patek Philippe Nautilus 5711", "Patek Philippe"),
            new Pair<>("Audemars Piguet Royal Oak Offshore", "Audemars Piguet"),
            new Pair<>("Breitling Navitimer B01 Chronograph", "Breitling"),
            new Pair<>("Longines HydroConquest", "Longines"),
            new Pair<>("Montblanc TimeWalker Chronograph", "Montblanc"),
            new Pair<>("Hublot Big Bang Unico", "Hublot"),
            new Pair<>("IWC Portugieser Automatic", "IWC"),
            new Pair<>("Cartier Ballon Bleu de Cartier", "Cartier"),
            new Pair<>("Panerai Luminor Marina", "Panerai"),
            new Pair<>("Zenith Chronomaster Sport", "Zenith"),
            new Pair<>("Oris Aquis Date", "Oris"),
            new Pair<>("Richard Mille RM 11-03", "Richard Mille"),
            new Pair<>("Vacheron Constantin Overseas", "Vacheron Constantin"),
            new Pair<>("Bulgari Octo Finissimo Automatic", "Bulgari"),
            new Pair<>("Jaeger-LeCoultre Reverso Classic", "Jaeger-LeCoultre"),
            new Pair<>("Grand Seiko Spring Drive Snowflake", "Grand Seiko"),
            new Pair<>("Swatch Big Bold Jellyfish", "Swatch"),
            new Pair<>("Timex Weekender", "Timex"),
            new Pair<>("Movado Museum Classic", "Movado"),
            new Pair<>("Rado Captain Cook Automatic", "Rado"),
            new Pair<>("Blancpain Fifty Fathoms", "Blancpain"),
            new Pair<>("Maurice Lacroix Aikon Automatic", "Maurice Lacroix"),
            new Pair<>("Bell & Ross BR 03-92", "Bell & Ross"),
            new Pair<>("Breitling Superocean Automatic", "Breitling"),
            new Pair<>("Tudor Black Bay Fifty-Eight", "Tudor"),
            new Pair<>("Victorinox Maverick Chronograph", "Victorinox"),
            new Pair<>("Skagen Ancher Chronograph", "Skagen"),
            new Pair<>("Michael Kors Lexington Chronograph", "Michael Kors"),
            new Pair<>("Emporio Armani Luigi Chronograph", "Emporio Armani"),
            new Pair<>("Diesel Mr. Daddy 2.0", "Diesel"),
            new Pair<>("Guess Legacy Chronograph", "Guess")
    ));
    private final List<Pair<String, String>> refrigerators = new ArrayList<>(List.of(
            Pair.of("Samsung RT38K5982S8/NS", "Samsung"),
            Pair.of("LG GR-B247JDS", "LG"),
            Pair.of("Panasonic NR-BL267PSV1", "Panasonic"),
            Pair.of("Sharp SJ-X186E-SL", "Sharp"),
            Pair.of("Electrolux ETB3400K-A", "Electrolux"),
            Pair.of("Hitachi R-VG540PGV2", "Hitachi"),
            Pair.of("Toshiba GR-A31V", "Toshiba"),
            Pair.of("Midea HD-306FN", "Midea"),
            Pair.of("Samsung RS62K6000SA/NS", "Samsung"),
            Pair.of("LG GS-X698PS", "LG"),
            Pair.of("Panasonic NR-F610GT-X5", "Panasonic"),
            Pair.of("Beko RCNA365E60ZX", "Beko"),
            Pair.of("Sharp SJ-PX535PG", "Sharp"),
            Pair.of("Fisher & Paykel RF522W", "Fisher & Paykel"),
            Pair.of("Sanyo SR-DC43", "Sanyo"),
            Pair.of("Whirlpool WRF757SDHZ", "Whirlpool"),
            Pair.of("Bosch KGN49XIDP", "Bosch"),
            Pair.of("Siemens KG39NXI45", "Siemens"),
            Pair.of("Frigidaire FFTR1821TS", "Frigidaire"),
            Pair.of("Haier HRF-628", "Haier"),
            Pair.of("Liebherr CBNes 6256", "Liebherr"),
            Pair.of("GE GNE27JYMFS", "GE"),
            Pair.of("Maytag MRT311FFFZ", "Maytag"),
            Pair.of("Kenmore 71223", "Kenmore"),
            Pair.of("Samsung RB33J3030SA", "Samsung"),
            Pair.of("LG InstaView Door-in-Door Refrigerator", "LG"),
            Pair.of("Whirlpool WRS588FIHZ", "Whirlpool"),
            Pair.of("Frigidaire FG4H2272UF", "Frigidaire"),
            Pair.of("Sharp SJ-PT6500", "Sharp"),
            Pair.of("Electrolux EHE5570AA", "Electrolux"),
            Pair.of("Beko GNE60520X", "Beko"),
            Pair.of("Hitachi R-VG660PGV2", "Hitachi"),
            Pair.of("Samsung RF28R7351SG", "Samsung"),
            Pair.of("Midea MR-17FS", "Midea")
    ));
    @Transactional
    public void generateAndInsertFakeBrand() {
        long start = System.nanoTime();
        List<Brand> brands = new ArrayList<>();
        Set<String> name = new HashSet<>();
        for (Pair<String, String> phone : phonePairList) {
            if (!name.contains(phone.value())) {
                Brand brand = new Brand();
                brand.setBrandName(phone.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(phone.value());
            }
        }
        for (Pair<String, String> laptop : laptopPairList) {
            if (!name.contains(laptop.value())) {
                Brand brand = new Brand();
                brand.setBrandName(laptop.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(laptop.value());
            }
        }
        for (Pair<String, String> tv : tvProducts) {
            if (!name.contains(tv.value())) {
                Brand brand = new Brand();
                brand.setBrandName(tv.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(tv.value());
            }
        }
        for (Pair<String, String> watch : watchProducts) {
            if (!name.contains(watch.value())) {
                Brand brand = new Brand();
                brand.setBrandName(watch.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(watch.value());
            }
        }
        for (Pair<String, String> computer  : refrigerators ) {
            if (!name.contains(computer.value())) {
                Brand brand = new Brand();
                brand.setBrandName(computer.value());
                brand.setDescription(null);
                brand.setActive(true);
                brands.add(brand);
                name.add(computer.value());
            }
        }
        brandRepository.saveAll(brands);
        log.info("Fake {} brand in: " + ((System.nanoTime() - start) / 1_000_000) + "ms", name.size());
    }

    @Transactional
    public Product createPhoneProduct(Shop shop) {
        List<String> imageUrls = new ArrayList<>(Arrays.asList(
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2011%20Pro/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2011%20Pro/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/Iphone%2011%20Pro%20max/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012%20Pro/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2012%20Pro%20Max/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013%20Pro/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2013%20Pro%20Max/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/7.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/IPhone/IPhone%2015/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%202%20SIM/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%204G%20Pro/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20110%204G%20Pro/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20110%204G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20215%204G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20C21%20Plus%203GB/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20C32%204GB/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20G22%204GB/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%205G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20T%204G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/6.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/7.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno7%20Z/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Oppo/OPPO%20Reno8%20Z/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Nokia/Nokia%20105%202%20SIM/6.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/6.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A23/7.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/6.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20A25%205G/7.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20S22%20Ultra/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/SamSung/Samsung%20Galaxy%20S22%20Ultra/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/6.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2012T%20Pro/7.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/2.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/3.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/4.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%2013T%20Pro%205G/5.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%20POCO%20X6%20Pro%205G/1.jpg?raw=true",
                "https://github.com/HiepF5/Db_Ecommercer/blob/main/Xiaomi/Xiaomi%20POCO%20X6%20Pro%205G/2.jpg?raw=true"));
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Điện Thoại & Phụ Kiện", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Điện thoại di động", shop.getShopCode());
            int randomIndex = random.nextInt(0, phonePairList.size());
            Pair<String, String> pair1 = phonePairList.get(randomIndex);
            phonePairList.remove(randomIndex);
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
            String title = "Điện thoại " + pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            String description = "Điện thoại thông minh cao cấp với camera vượt trội\n" +
                    "\n" +
                    "Chiếc điện thoại này được trang bị một màn hình lớn, sắc nét với độ phân giải cao, mang lại trải nghiệm xem phim, chơi game và lướt web tuyệt vời. Công nghệ màn hình OLED hoặc AMOLED giúp hình ảnh hiển thị sống động với màu sắc rực rỡ, độ tương phản ấn tượng, và khả năng tiết kiệm năng lượng tối ưu. Với độ sáng cao và khả năng hiển thị dưới ánh sáng mạnh, bạn có thể dễ dàng sử dụng điện thoại ngoài trời mà không gặp phải vấn đề khó nhìn.\n" +
                    "\n" +
                    "Điện thoại sở hữu một bộ vi xử lý mạnh mẽ, giúp chạy mượt mà các ứng dụng, từ việc duyệt web, chơi game đến chỉnh sửa ảnh và video. Kết hợp với RAM lớn và bộ nhớ trong rộng, chiếc điện thoại này giúp bạn thực hiện nhiều tác vụ đồng thời mà không gặp phải tình trạng giật lag. Hệ điều hành thông minh giúp tối ưu hóa hiệu suất và cung cấp các tính năng tiện lợi như nhận diện khuôn mặt, điều khiển bằng cử chỉ và chế độ tối.\n" +
                    "\n" +
                    "Một trong những điểm nổi bật của chiếc điện thoại này chính là hệ thống camera chất lượng cao. Với camera chính có độ phân giải cao và các tính năng như chụp ảnh ban đêm, quay video 4K và chế độ xóa phông, bạn có thể dễ dàng chụp được những bức ảnh và video đẹp mắt trong mọi điều kiện ánh sáng. Các tính năng AI cũng giúp nhận diện cảnh vật và điều chỉnh các thông số tự động để cho ra những bức ảnh chất lượng cao mà không cần chỉnh sửa nhiều.\n" +
                    "\n" +
                    "Với thiết kế tinh tế, vỏ kim loại hoặc kính cường lực và khả năng chống nước, chiếc điện thoại này không chỉ bền bỉ mà còn rất thời trang. Hỗ trợ sạc nhanh và pin dung lượng lớn giúp bạn sử dụng suốt cả ngày dài mà không cần lo lắng về việc hết pin. Các kết nối 5G và Wi-Fi mới nhất mang đến tốc độ internet nhanh chóng, giúp bạn duyệt web, xem video hoặc chơi game trực tuyến mượt mà.";
            product.setDescription(description);
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
                        long min = 3000000;
                        long max = 15000000;
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

        List<String> imageUrls = List.of(
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/macbook_air_13_m2_midnight_1_35053fbcf9.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/dell_latitude_15_3540_9950b79986.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_5_13_638512086854166670_8F157PA-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_5_13_638512084171838845_X1504ZA-NJ517W-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/hp_15_fc0089au_a94xwpa_5dade7e606.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/dell_inspiron_15_3520_black_d3c2a2c3c6.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/macbook_pro_14_m4_pro_max_space_black_1_49c97ac737.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/00909839_dell_vostro_3420_gray_88f0e892df.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/dell_latitude_15_3540_9950b79986.png"
        );
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Máy Tính & Laptop", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Máy tính xách tay", shop.getShopCode());
            int randomIndex = random.nextInt(0, laptopPairList.size());
            Pair<String, String> pair1 = laptopPairList.get(randomIndex);
            laptopPairList.remove(randomIndex);
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
            String title = "Laptop " + pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            String description = "Laptop cao cấp với hiệu suất mạnh mẽ\n" +
                    "\n" +
                    "Chiếc laptop này được thiết kế để đáp ứng mọi nhu cầu công việc và giải trí, với một cấu hình mạnh mẽ và màn hình sắc nét. Màn hình có độ phân giải Full HD hoặc 4K, mang lại hình ảnh rõ ràng, sống động, lý tưởng cho cả công việc đồ họa và xem phim. Với công nghệ IPS hoặc OLED, góc nhìn rộng và độ sáng tuyệt vời, bạn sẽ có những trải nghiệm thị giác tuyệt hảo dù ở trong môi trường ánh sáng mạnh.\n" +
                    "\n" +
                    "Laptop được trang bị bộ vi xử lý mới nhất và card đồ họa rời, cho phép bạn xử lý đa nhiệm mượt mà và chạy các ứng dụng yêu cầu hiệu suất cao như thiết kế đồ họa, chỉnh sửa video, hoặc chơi game. Bộ nhớ RAM lớn và ổ cứng SSD tốc độ cao giúp bạn khởi động hệ điều hành và ứng dụng nhanh chóng, đồng thời cung cấp không gian lưu trữ rộng rãi cho các tệp và dự án công việc.\n" +
                    "\n" +
                    "Với trọng lượng nhẹ và thiết kế mỏng, chiếc laptop này dễ dàng mang theo trong các chuyến công tác hay di chuyển hàng ngày. Bàn phím có đèn nền giúp bạn làm việc hiệu quả trong điều kiện ánh sáng yếu, trong khi pin lâu dài đảm bảo bạn có thể làm việc suốt cả ngày mà không phải lo lắng về việc sạc pin.\n" +
                    "\n" +
                    "Ngoài ra, laptop còn tích hợp các cổng kết nối hiện đại như USB-C, HDMI, và Thunderbolt, cho phép bạn kết nối dễ dàng với các thiết bị ngoại vi, màn hình ngoài, hoặc máy chiếu. Hệ thống bảo mật vân tay và camera hồng ngoại giúp bảo vệ dữ liệu cá nhân và nâng cao sự tiện lợi khi đăng nhập.";
            product.setDescription(description);
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
                        long min = 3000000;
                        long max = 15000000;
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
    public Product createTVProduct(Shop shop) {
        List<String> imageUrls = List.of(
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/samsung_ua65du7700wxxxy_1_fa7e6d7422.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/tv_casper_32hgk610_d5de8bfb28.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2023_6_13_638222661356566356_600-A2-32.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/tv_lg_uhd_43_inch_43ut8050psb_7625ba3bd2.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/samsung_ua50du8000kxxv_1_fb6571dfa1.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/lg_65qned80tsa_1_9b62657a48.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/lg_65ut7350psb_1_5607be89cb.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_13_638538979602266996_00908788-tcl-75p79b-pro-thumb.jpg"
        );
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Tivi Box", 1);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Tivi và thiết bị giải trí", shop.getShopCode());
            int randomIndex = random.nextInt(0, tvProducts.size());
            Pair<String, String> pair1 = tvProducts.get(randomIndex);
            tvProducts.remove(randomIndex);
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
            String title = "TV " + pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            String description = "Tivi 4K Smart OLED cao cấp\n" +
                    "Đây là một chiếc tivi 4K OLED cao cấp, được thiết kế với màn hình rộng, mang đến trải nghiệm hình ảnh tuyệt vời với độ phân giải 4K sắc nét và màu sắc sống động. Mỗi điểm ảnh tự phát sáng, cho phép tái tạo màu đen hoàn hảo và độ tương phản cực cao, mang lại những cảnh quay rực rỡ, chân thực nhất. Công nghệ OLED không chỉ cải thiện chất lượng hình ảnh mà còn giúp tiết kiệm năng lượng và giảm thiểu hiện tượng \"burn-in\" (lưu ảnh) thường gặp trên các màn hình khác.\n" +
                    "Với hệ điều hành thông minh, chiếc tivi này dễ dàng kết nối và truy cập vào hàng loạt ứng dụng giải trí trực tuyến như Netflix, YouTube, Disney+, và nhiều dịch vụ khác, mang lại trải nghiệm giải trí phong phú ngay tại nhà. Bạn cũng có thể điều khiển tivi bằng giọng nói nhờ tích hợp trợ lý ảo như Google Assistant hoặc Amazon Alexa, giúp việc tìm kiếm nội dung trở nên nhanh chóng và tiện lợi hơn bao giờ hết.\n" +
                    "Ngoài chất lượng hình ảnh vượt trội, chiếc tivi này còn tích hợp âm thanh vòm sống động với công nghệ âm thanh tiên tiến, giúp mang lại hiệu ứng âm thanh 3D, khiến bạn cảm nhận được không gian âm thanh mạnh mẽ, bao trùm. Thiết kế mỏng nhẹ, thanh lịch của tivi giúp phù hợp với mọi không gian nội thất, tạo điểm nhấn sang trọng cho phòng khách, phòng ngủ hoặc không gian giải trí của gia đình.";
            product.setDescription(description);
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
                        long min = 3000000;
                        long max = 15000000;
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
    public Product createClockProduct(Shop shop) {
        List<String> imageUrls = List.of(
            "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/00905683_amazfit_active_hong_0cfceb6436.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/myfirst_fone_r1s_xanh_3_3e804abba7.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/myfirst_fone_s3_xanh_1_3a64fc50d3.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_12_638538031033026326_zwatch-Z10-Pro-dd.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_3_11_638457733130725371_garmin-forerunner-165-den%20(4).jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/huawei_watch_ultimate_2_d60f823a46.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/kieslect_watch_actor_den_1_a92a69a034.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_2_28_638447370207945481_amazfit-active-edge-dd.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2023_8_31_638290717863326144_dong-ho-thong-minh-garmin-venu-3-den-3.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/garmin_forerunner_965_black_yellow_47fda5393f.png"
        );
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Đồng Hồ", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Đồng hồ thông minh và thiết bị đeo", shop.getShopCode());
            int randomIndex = random.nextInt(0, watchProducts.size());
            Pair<String, String> pair1 = watchProducts.get(randomIndex);
            watchProducts.remove(randomIndex);
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
            String title = "Đồng hồ " + pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            String description = "Một chiếc đồng hồ với thiết kế sang trọng và đẳng cấp, mang lại vẻ đẹp vượt thời gian phù hợp cho cả những người yêu phong cách cổ điển và hiện đại. Mặt kính sapphire chống xước chất lượng cao, kết hợp với khung vỏ thép không gỉ, tạo nên một sản phẩm không chỉ bền bỉ mà còn tỏa sáng dưới mọi ánh nhìn. Mặt số được thiết kế tối giản nhưng đầy tinh tế, với các cọc số và kim được phủ lớp phát quang giúp dễ dàng quan sát trong điều kiện thiếu sáng. Bộ máy tự động được chế tác với độ chính xác cao, hoạt động mượt mà, mang đến trải nghiệm đeo tuyệt vời cho người dùng.\n" +
                    "\n" +
                    "Chiếc đồng hồ này đi kèm dây đeo bằng da thật mềm mại, không chỉ đảm bảo sự thoải mái khi đeo cả ngày mà còn tăng thêm sự sang trọng và đẳng cấp. Với khả năng chống nước lên tới 100m, bạn hoàn toàn có thể sử dụng trong các hoạt động thường ngày hoặc thậm chí khi bơi lội. Sản phẩm là sự kết hợp hoàn hảo giữa công nghệ chế tác hiện đại và vẻ đẹp nghệ thuật tinh tế, trở thành phụ kiện lý tưởng cho những ai muốn thể hiện phong cách cá nhân trong các buổi họp quan trọng, sự kiện sang trọng, hay những khoảnh khắc đời thường";
            product.setDescription(description);
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
                        long min = 3000000;
                        long max = 15000000;
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
    public Product createElectronicProduct(Shop shop) {
        List<String> imageUrls = List.of(
            "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_14_638539752622137847_00908859-toshiba-tw-t21bu115uwv-mg-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_5_6_638505937146582420_WT-85NG1-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_14_638539752622137847_00908859-toshiba-tw-t21bu115uwv-mg-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_14_638539750466252595_00908783-lg-fv1412s3b-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/00909171_may_giat_cua_truoc_samsung_inverter_95_kg_ww95ta046axsv_5bb221467d.png",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_14_638539697664443873_00906211-lg-fv1410s4w1-thumb.jpg",
                "https://cdn2.fptshop.com.vn/unsafe/150x0/filters:quality(100)/2024_6_14_638539747393307665_00908784-lg-fb1209s6w-thumb.jpg"
        );
        try {
            // Bước 1: Lấy các thông tin cơ bản cho sản phẩm
            Category category = categoryRepository.
                    findCategoryByCategoryNameAndCategoryLevel("Thiết Bị Điện Tử", 0);
            ShopCategory shopCategory = shopCategoryRepository
                    .findShopCategoryByCategoryNameAndShop("Thiết bị gia dụng thông minh", shop.getShopCode());
            int randomIndex = random.nextInt(0, refrigerators.size());
            Pair<String, String> pair1 = refrigerators.get(randomIndex);
            refrigerators.remove(randomIndex);
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
            String title = "Tủ lạnh " + pair1.key();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1); // Xóa dấu chấm ở cuối
            }
            product.setTitle(title);
            String description = "Đây là một tủ lạnh được thiết kế với khả năng tiết kiệm năng lượng tối ưu, phù hợp với mọi gia đình hiện đại. Với công nghệ làm lạnh nhanh chóng, tủ lạnh này giữ thực phẩm luôn tươi ngon và kéo dài tuổi thọ thực phẩm lâu hơn. Bên trong, các ngăn được phân chia hợp lý, giúp bạn dễ dàng sắp xếp thực phẩm một cách khoa học, tiết kiệm không gian tối đa.\n" +
                    "\n" +
                    "Các ngăn chứa rau quả và thịt được thiết kế đặc biệt để duy trì độ ẩm và giữ thực phẩm luôn tươi mới trong suốt thời gian dài. Tủ lạnh cũng được trang bị hệ thống khử mùi hiện đại, giúp loại bỏ các mùi khó chịu, mang lại không gian tủ lạnh sạch sẽ và thơm tho.\n" +
                    "\n" +
                    "Với thiết kế sang trọng và tinh tế, tủ lạnh này không chỉ là một thiết bị điện gia dụng, mà còn là điểm nhấn nổi bật trong không gian bếp của bạn. Các tính năng như điều chỉnh nhiệt độ dễ dàng, màn hình hiển thị LED và cửa kính chống bám vân tay mang đến sự tiện lợi và đẳng cấp cho người sử dụng.";
            product.setDescription(description);
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
                        long min = 3000000;
                        long max = 15000000;
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
        System.out.println("Phone: " + phonePairList.size());
        System.out.println("Laptop: " + laptopPairList.size());
        System.out.println("TV: " + tvProducts.size());
        System.out.println("Watch: " + watchProducts.size());
        for (Shop shop : shops) {
            for (int i = 0; i < 3; i++) {
                if(phonePairList.size()<=0) break;
                Product product = createPhoneProduct(shop);
                productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
            }
            for (int i = 0; i<1; i++){
                if(laptopPairList.size()<=0) break;
                Product product = createLaptopProduct(shop);
                productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
            }
            for (int i = 0; i<1; i++){
                if(tvProducts.size()<=0) break;
                Product product = createTVProduct(shop);
                productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
            }
            for (int i = 0; i<1; i++){
                if(watchProducts.size()<=0) break;
                Product product = createClockProduct(shop);
                productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
            }
            for (int i = 0; i<1; i++){
                if(refrigerators.size()<=0) break;
                Product product = createElectronicProduct(shop);
                productProducer.sendProductToQueue(productMapper.mapperProductToDocument(product));
            }
        }
    }

}
