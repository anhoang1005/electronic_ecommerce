package com.example.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class StringFormatUtils {
    public static final String convertToSlug(String str){
        str = Normalizer.normalize(str,Normalizer.Form.NFD);
        str = str.replaceAll("\\p{M}","");
        return str.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    public String formatPrice(long price) {
        if (price >= 1_000_000) {
            double millions = price / 1_000_000.0;
            return String.format("%.1f", millions).replace(".0", "") + "tr";
        } else if (price >= 1_000) {
            double thousands = price / 1_000.0;
            return String.format("%.1f", thousands).replace(".0", "") + "k";
        } else {
            return String.valueOf(price);
        }
    }
}
