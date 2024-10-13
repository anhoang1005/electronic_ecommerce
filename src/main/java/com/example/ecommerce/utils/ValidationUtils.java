package com.example.ecommerce.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ValidationUtils {

    public String titleToSlug(String title) {
        // Chuyen chuoi thanh chu thuong
        String normalized = title.toLowerCase();

        //Loai bo dau tieng viet
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalized).replaceAll("");

        //thay the khoang trang va cac ki tu dac biet bang dau gach ngang
        slug = slug.replaceAll("[^a-z0-9\\s-]", ""); // Loai bo cac ki tu khong hop le
        slug = slug.replaceAll("\\s+", "-"); // Thay the khoang trang bang dau gach ngang
        slug = slug.replaceAll("-+", "-"); //Loai bo cac dau gach ngang thua

        return slug;
    }
}
