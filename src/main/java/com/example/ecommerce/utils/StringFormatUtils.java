package com.example.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static String convertNameToCustomFormat(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        String normalized = fullName.trim().toLowerCase();

        //Loai bo dau tieng viet
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");

        // Tách các phần tên
        String[] nameParts = normalized.split("\\s+");

        // Lấy phần tên chính (từ cuối cùng) ghép với họ và tên đệm
        StringBuilder formattedName = new StringBuilder(nameParts[nameParts.length - 1]);
        for (int i = 0; i < nameParts.length - 1; i++) {
            formattedName.append(nameParts[i]);
        }

        return formattedName.toString();
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
    public  List<Long> convertStringToList(String dbValue) {

        if(dbValue != null && !dbValue.isEmpty()){
            String cleanedValue = dbValue.replaceAll("[\\[\\] ]", "");

            List<Long> idList = Arrays.stream(cleanedValue.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            return idList;
        }
        return List.of();
    }
}
