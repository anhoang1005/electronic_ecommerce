package elk_service.elk.product.service;

import elk_service.elk.product.entites.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductDocumentService {

    Page<ProductDocument> filterProduct(
            Long shopCategoryId,
            String updateAt,
            Double rating,
            Long sold,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
    Page<ProductDocument> filterAndSortProduct(
            String shopCode,
            Long shopCategoryId,
            String sortField,
            Pageable pageable,
            BigDecimal minPrice, BigDecimal maxPrice
    );

    Page<ProductDocument> searchProductDocument(String searchText, Pageable pageable);
}