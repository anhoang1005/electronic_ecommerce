package com.example.ecommerce.utils;

import java.text.Normalizer;

public class StringFormatUtils {
    public static final String convertToSlug(String str){
        str = Normalizer.normalize(str,Normalizer.Form.NFD);
        str = str.replaceAll("\\p{M}","");
        return str.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }
}
