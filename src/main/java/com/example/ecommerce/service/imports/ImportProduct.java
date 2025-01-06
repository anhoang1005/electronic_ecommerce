package com.example.ecommerce.service.imports;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.attribute.AttributeDTO;
import com.example.ecommerce.models.product.ProductDTO;
import com.example.ecommerce.models.variant.VariantDTO;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.StringFormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ImportProduct {
    private final AuthenticationUtils authenticationUtils;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ShopRepository shopRepository;
    private final ObjectMapper objectMapper;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final VariantAttributeValueRepository variantAttributeValueRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final VariantRepository variantRepository;

    private final JsonConvert jsonConvert;

    public List<ProductDTO> readProductsFromExcel(MultipartFile file) {
        List<ProductDTO> productList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua dòng tiêu đề

                String shopCode = row.getCell(0).getStringCellValue();
                String brand = row.getCell(1).getStringCellValue();
                String category = row.getCell(2).getStringCellValue();
                String title  = row.getCell(3).getStringCellValue();
                System.out.println(shopCode + " " + brand + " " + category + " " + title);
                ProductDTO product = new ProductDTO();
                productList.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public ResponseBody fakeShopCreateProduct(ProductDTO dto){
        try {
//            String shopCode = authenticationUtils.getUserFromAuthentication().getShopCode();
//            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
//                    () -> new RequestNotFoundException("Category not found with id : " + dto.getCategoryId()));
//            Brand brand = brandRepository.findById(dto.getBrandId()).orElseThrow(
//                    () -> new RequestNotFoundException("Brand not found"));
//            Shop shop = shopRepository.findShopByShopCode(shopCode);
//
//            Product product = new Product();
//            product.setTitle(dto.getTitle());
//            product.setDescription(dto.getDescription());
//            product.setProductState(Product.ProductState.PENDING);
//            product.setProductSlug(StringFormatUtils.convertToSlug(dto.getTitle()));
//            product.setCurrency(dto.getCurrency());
//            product.setIsHidden(dto.isHidden());
//            product.setCategory(category);
//            product.setBrandOfProduct(brand);
//            product.setShopOfProduct(shop);
//
//            List<String> image = Arrays.asList(dto.getImageList().trim().split("\\|"));
//            product.setImageList(objectMapper.writeValueAsString(image));
//            Map<String, Set<String>> productAttributeValues = new HashMap<>();
//
//            BigDecimal minPrice = dto.getVariantDTOS().stream().map(VariantDTO::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
//            BigDecimal maxPrice = dto.getVariantDTOS().stream().map(VariantDTO::getPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
//            product.setMaxInitialPrice(minPrice);
//            product.setMinInitialPrice(maxPrice);
//
//            //Tao bien the cho san pham
//            for(VariantDTO variantDTO : dto.getVariantDTOS()){
//                ProductVariant variant = new ProductVariant();
//                variant.setPrice(variantDTO.getPrice());
//                variant.setStockQuantity(variantDTO.getQuantity());
//                variant.setSku(variantDTO.getSku());
//                variant.setStatus(ProductVariant.Status.CON_HANG);
//                variant.setVariantSlug(StringFormatUtils.convertToSlug(variantDTO.getSku()));
//                variant.setHeight(variantDTO.getHeight());
//                variant.setWeight(variantDTO.getWeight());
//                variant.setLength(variantDTO.getLength());
//                variant.setWidth(variantDTO.getWidth());
//                variant.setStockQuantity(variantDTO.getStockQuantity());
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for(AttributeDTO attributeDTO : variantDTO.getAttributeDTOList()) {
//                    Attribute attribute = attributeRepository.
//                            findAttributeByAttributeName(attributeDTO.getName());
//                    if (ObjectUtils.isEmpty(attribute)) {
//                        attribute = new Attribute();
//                        attribute.setAttributeName(attributeDTO.getName());
//                        attribute.setValueJson("[]");
//                        attribute = attributeRepository.save(attribute);
//                    }
//
//                    AttributeValue attributeValue = attributeValueRepository
//                            .findAttributeValueByValueAndAttributeId(attribute.getId(),attributeDTO.getValue());
//                    if(ObjectUtils.isEmpty(attributeValue)){
//                        attributeValue = new AttributeValue();
//                        attributeValue.setAttribute(attribute);
//                        attributeValue.setValue(attributeDTO.getValue());
//                        attributeValue =  attributeValueRepository.save(attributeValue);
//                        try {
//                            List<String> valueJson = jsonConvert.convertValueJsonToList(attribute.getValueJson());
//                            if (valueJson == null) {
//                                valueJson = new ArrayList<>();
//                            }
//                            valueJson.add(attributeValue.getValue());
//                            attribute.setValueJson(jsonConvert.convertListObjectToJson(valueJson));
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
//                    stringBuilder.append(attribute.getAttributeName().toLowerCase())
//                            .append("=")
//                            .append(attributeValue.getValue().toLowerCase())
//                            .append(";");
//                    VariantAttributeValue variantAttributeValue = new VariantAttributeValue();
//                    variantAttributeValue.setAttributeValue(attributeValue);
//                    variantAttributeValue.setAttribute(attribute);
//                    variantAttributeValueRepository.save(variantAttributeValue);
//                    variant.getVariantAttributeValueList().add(variantAttributeValue);
//
//                    ProductAttributeValue productAttributeValue = new ProductAttributeValue();
//                    productAttributeValue.setAttribute(attribute);
//                    productAttributeValue.setAttributeValue(attributeValue);
//                    productAttributeValueRepository.save(productAttributeValue);
//                    product.getProductAttributeValues().add(productAttributeValue);
//                    if (productAttributeValues.containsKey(attribute.getAttributeName())) {
//                        productAttributeValues.get(attribute.getAttributeName()).add(attributeValue.getValue());
//                    } else {
//                        productAttributeValues.put(attribute.getAttributeName(), new HashSet<>(List.of(attributeValue.getValue())));
//                    }
//                }
//
//                if(stringBuilder.length() > 0) {
//                    stringBuilder.substring(0,stringBuilder.length()-1);
//                }
//                variant.setProductVariation(stringBuilder.toString());
//                variantRepository.save(variant);
//
//                //Tao gia tri attribute cua variant
//                for(VariantAttributeValue variantAttributeValue : variant.getVariantAttributeValueList()){
//                    variantAttributeValue.setVariant(variant);
//                    variantAttributeValueRepository.save(variantAttributeValue);
//                }
//                product.getVariants().add(variant);
//            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
