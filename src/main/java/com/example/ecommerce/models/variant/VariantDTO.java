package com.example.ecommerce.models.variant;

import com.example.ecommerce.models.attribute.AttributeDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantDTO {
    private List<AttributeDTO> attributeDTOList = new ArrayList<>();
    private BigDecimal price;
    private Integer quantity;
    private String sku;
    private MultipartFile productImage;
    private Double height;
    private Double weight;
    private Double length;
    private Double width;
    private Integer stockQuantity;
}
