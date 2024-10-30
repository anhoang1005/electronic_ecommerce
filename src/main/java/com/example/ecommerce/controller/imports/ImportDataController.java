package com.example.ecommerce.controller.imports;

import com.example.ecommerce.payload.ImportResponse;
import com.example.ecommerce.service.imports.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest")
public class ImportDataController {
    private final ImportService importService;
    @PostMapping("/product")
    public ImportResponse importBaseDataProduct(@RequestParam("file")MultipartFile file){
        return importService.importBaseProductExcel(file);
    }
    @PostMapping("/categories/system")
    public ImportResponse importCategorySystem(@RequestParam("file")MultipartFile file){
        return importService.importCategoriesFromExcel(file);
    }
}
