package com.example.ecommerce.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMBER = "0123456789";
    private static final int VOUCHER_CODE_LENGTH = 10;
    private final Random random;

    public String generateRandomVoucherCode(){
        StringBuilder voucherCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < VOUCHER_CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            voucherCode.append(CHARACTERS.charAt(index));
        }
        return voucherCode.toString();
    }
    public  String generateRandomString(int length){
        StringBuilder code = new StringBuilder();
        for(int i = 0 ; i < length ; i++){
            int index = random.nextInt(NUMBER.length());
            code.append(index);
        }
        return code.toString();
    }

    private static String getRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }

    public static String generateOrderCode(String code){
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomPart = getRandomString(8);
        return code + "-" + String.format("%s-%s", timeStamp, randomPart);
    }
}
