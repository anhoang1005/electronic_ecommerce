package com.example.ecommerce.service.faker;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.ShopVoucher;
import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class FakeVoucherService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private final Faker faker = new Faker(new Locale("vi"));
    private final ShopRepository shopRepository;
    private final ShopVoucherRepository shopVoucherRepository;
    private final VoucherRepository voucherRepository;

    private final DateTimeMapperUtils timeUtils;
    private final RandomUtils randomUtils;
    private final JsonConvert jsonConvert;

    @Transactional
    public void generateAndInsertFakeShopVoucherData() {
        long start = System.nanoTime();
        List<Shop> shops = shopRepository.findAll();
        int count = 0;
        for (Shop shop : shops) {
            for (int i = 0; i < 3; i++) {
                ShopVoucher voucher = new ShopVoucher();
                voucher.setShopOfShopVoucher(shop);
                voucher.setVoucherCode("SVC" + randomUtils.generateRandomString(15));
                voucher.setTitle("Giảm giá đầu năm " + (i + 1));
                voucher.setVoucherCount(random.nextInt(10, 150));
                voucher.setRemainingCount(random.nextInt(5, 50));
                voucher.setStatus(Voucher.Status.ENABLE);
                voucher.setDiscountType(faker.options().option(Voucher.DiscountType.class));
                if (voucher.getDiscountType() == Voucher.DiscountType.AMOUNT) {
                    voucher.setDiscountValue(random.nextLong(5, 50)*10000.0);
                    voucher.setMinTotalOrder(random.nextLong(voucher.getDiscountValue().longValue()*2, voucher.getDiscountValue().longValue()*5)*1.0);
                } else {
                    voucher.setDiscountValue(random.nextLong(3, 20)*1.0);
                    voucher.setMinTotalOrder(random.nextLong(20, 100)*10000.0); // Khoảng giá trị hợp lý
                }
                double maxDiscountValue = random.nextLong(10, 50)*10000.0;
                voucher.setMaxDiscountValue(Math.max(voucher.getDiscountValue(), maxDiscountValue));
                voucher.setStartedAt(timeUtils.stringToLocalDateTime("2025-01-01T00:00:00"));
                voucher.setExpiredAt(timeUtils.stringToLocalDateTime("2025-01-10T00:00:00"));
                voucher = shopVoucherRepository.save(voucher);
                count++;
            }
        }
        log.info("Fake {} voucherShop for {} shop in: " + ((System.nanoTime() - start) / 1_000_000) + "ms", count, shops.size());
    }

    @Transactional
    public void generateAndInsertFakeVoucherData() {
        long start = System.nanoTime();
        for (int i = 0; i < 30; i++) {
            Voucher voucher = new Voucher();
            // Sinh ngẫu nhiên các thuộc tính cơ bản
            voucher.setVoucherCode("VC" + randomUtils.generateRandomString(15)); // Mã voucher ngẫu nhiên
            voucher.setTitle("Voucher tri ân đầu năm " + random.nextInt(1, 100)); // Tên voucher ngẫu nhiên
            voucher.setVoucherCount(random.nextInt(50, 500));
            voucher.setRemainingCount(random.nextInt(10, 50));
            voucher.setStatus(Voucher.Status.ENABLE);
            voucher.setStartedAt(timeUtils.stringToLocalDateTime("2025-01-01T00:00:00")); // Thời gian bắt đầu
            voucher.setExpiredAt(timeUtils.stringToLocalDateTime("2025-01-10T23:59:59")); // Thời gian kết thúc
            // Kiểm tra loại voucher: DISCOUNT hoặc SHIPPING
            if (random.nextBoolean()) {
                voucher.setType(Voucher.Type.DISCOUNT);
                voucher.setDiscountType(random.nextBoolean() ? Voucher.DiscountType.AMOUNT : Voucher.DiscountType.PERCENT);
                if (voucher.getDiscountType() == Voucher.DiscountType.AMOUNT) {
                    voucher.setDiscountValue(random.nextLong(5, 50)*10000.0);
                } else {
                    voucher.setDiscountValue(random.nextLong(5, 20)*1.0);
                }
                // Đảm bảo giá trị giảm giá tối đa >= giá trị giảm giá
                double maxDiscount = random.nextLong(20, 200)*10000.0;
                voucher.setMaxDiscountValue(Math.max(voucher.getDiscountValue(), maxDiscount));

                // Xác định minTotalOrder cho DISCOUNT voucher
                voucher.setMinTotalOrder(random.nextLong(300, 2000)*10000.0); // Giá trị đơn hàng từ 3tr đến 20tr
            } else {
                // SHIPPING VOUCHER
                voucher.setType(Voucher.Type.SHIPPING);
                voucher.setDiscountType(Voucher.DiscountType.AMOUNT);
                voucher.setShippingDiscount(random.nextLong(2, 30)*10000.0);
                voucher.setMaxShippingDiscount(random.nextLong(2, 30)*10000.0);
            }
            // Xác định minTotalOrder cho SHIPPING voucher
            voucher.setMinTotalOrder(random.nextLong(300, 2000)*10000.0); // Giá trị đơn hàng từ 3tr đến 20tr
            voucher = voucherRepository.save(voucher);
        }
        log.info("Fake {} voucher in: " + ((System.nanoTime() - start) / 1_000_000) + "ms", 30);
    }
}
