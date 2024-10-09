package com.example.ecommerce.utils;

import java.util.Random;

public class RandomUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int VOUCHER_CODE_LENGTH = 10;

    public String generateRandomVoucherCode(){
        StringBuilder voucherCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < VOUCHER_CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            voucherCode.append(CHARACTERS.charAt(index));
        }
        return voucherCode.toString();
    }


}
