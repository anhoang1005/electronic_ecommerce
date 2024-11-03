package com.example.ecommerce.service.imports;

import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.enums.CategoryType;
import com.example.ecommerce.payload.ImportResponse;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.utils.JsonConvert;
import com.example.ecommerce.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ProductRepository productRepository;
    private final JsonConvert jsonConvert;
    private final CategoryRepository categoryRepository;
    private final RandomUtils randomUtils;

    public ImportResponse importBaseProductExcel(MultipartFile file){
        int success = 0;
        int failed = 0;
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);

//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            for (int i = 1; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Product product = new Product();
                product.setTitle(getStringValue(row.getCell(2)));
                product.setProductCode(getStringValue(row.getCell(1)));
                product.setRating(getDoubleValue(row.getCell(4)));
                product.setReview(getIntValue(row.getCell(5)));
                product.setMaxInitialPrice(getLongValue(row.getCell(6)));
                product.setCurrency(getStringValue(row.getCell(8)));
//                product.setStock(getIntValue(row.getCell(9)));
                product.setFavorite((long) row.getCell(10).getNumericCellValue());
                product.setBreadcrumb(getStringValue(row.getCell(15)));
                product.setProductSpecifications(getStringValue(row.getCell(16)));
                product.setDescription(getStringValue(row.getCell(17)));
                product.setVariations(getStringValue(row.getCell(24)));
//                product.setBrand(getStringValue(row.getCell(26)));
                product.setProductSlug("demo");
//                product.setIsAvailable(true);
                product.setProductRatings(getStringValue(row.getCell(36)));
//                product.setProductVariation(getStringValue(row.getCell(30)));

                productRepository.save(product);

                if (!ObjectUtils.isEmpty(product)) {
                    success += 1;
                } else {
                    failed += 1;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ImportResponse response = new ImportResponse();
        response.setRecordSuccess(success);
        response.setRecordFailed(failed);
        return response;
    }
    private String getStringValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return "";
    }

    private double getDoubleValue(Cell cell) {
        if (cell == null) {
            System.err.println("null");
            return 0.0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String cellValue = cell.getStringCellValue();
                try {
                    double value = Double.parseDouble(cellValue);
                    return value;
                } catch (NumberFormatException e) {
                    System.err.println("Không thể chuyển đổi sang số: " + cellValue);
                }
                break;
            default:
                System.err.println("Giá trị ô không hợp lệ cho số: " + cell);
        }

        return 0.0;
    }

    private int getIntValue(Cell cell) {
        if (cell == null) {
            System.err.println("null");
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                String cellValue = cell.getStringCellValue();
                try {
                    return (int) Double.parseDouble(cellValue);
                } catch (NumberFormatException e) {
                    System.err.println("Không thể chuyển đổi sang số nguyên: " + cellValue);
                }
                break;
            default:
                System.err.println("Giá trị ô không hợp lệ cho số nguyên: " + cell);
        }

        return 0;
    }

    private BigDecimal getLongValue(Cell cell) {
        if (cell == null) {
            System.err.println("Ô null!");
            return BigDecimal.valueOf(0L);
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf((long) cell.getNumericCellValue());
            case STRING:
                String cellValue = cell.getStringCellValue();
                try {
                    return BigDecimal.valueOf((long) Double.parseDouble(cellValue));
                } catch (NumberFormatException e) {
                    System.err.println("Không thể chuyển đổi sang số dài: " + cellValue);
                }
                break;
            default:
                System.err.println("Giá trị ô không hợp lệ cho số dài: " + cell);
        }

        return BigDecimal.valueOf(0L);
    }
    public ImportResponse importCategoriesFromExcel(MultipartFile file){
        int success = 0;
        int failed = 0;
        try{
            Workbook workbook = new XSSFWorkbook((file.getInputStream()));
            var sheet = workbook.getSheetAt(0);
            for(int i = 1 ; i<sheet.getLastRowNum();i++){
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell cell = row.getCell(15);
                String cellData  = cell.getStringCellValue();
                List<String> categories = jsonConvert.extractCategories(cellData);
                if (!categories.isEmpty()) {
                    Category parent = null;
                    int level = 0;
                    for (String categoryName : categories) {
                        Category category = categoryRepository.findCategoryByCategoryNameAndCategoryLevel(categoryName, level);
                        if (category == null) {
                            category = new Category();
                            category.setCategoryName(categoryName);
                            category.setCategoryLevel(level);
                            category.setParentCategory(parent);
                            category.setActive(true);
                            category.setCategoryType(CategoryType.SYSTEM);
                            category.setCategoryCode("CTGY"+randomUtils.generateRandomString(15));
                            categoryRepository.save(category);
                            if (!ObjectUtils.isEmpty(categories)) {
                                success += 1;
                            } else {
                                failed += 1;
                            }
                        }
                        parent = category;
                        level++;
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        ImportResponse response = new ImportResponse();
        response.setRecordSuccess(success);
        response.setRecordFailed(failed);
        return response;
    }

}
