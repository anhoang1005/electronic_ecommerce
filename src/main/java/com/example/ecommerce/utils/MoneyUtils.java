package com.example.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyUtils {

    public BigDecimal calculatorServiceFee(BigDecimal productTotal){
        long total = productTotal.longValue();
        long result = (long) total * 3 / 100;
        return BigDecimal.valueOf(result);
    }
}
