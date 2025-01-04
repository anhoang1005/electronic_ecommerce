package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findProductById(Long id);

    @Query("select p from Product p  where p.isAccepted = true and p.isHidden = false and p.id = :id")
    Product findProductByIdIfAcceptedAndNotHidden(@RequestParam Long id);

    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p " +
            "where p.shopOfProduct.shopCode = :shopCode " +
            "and (:title is null or lower(p.title) like %:title%) " +
            "and (:brandId is null or p.brandOfProduct.id = :brandId) " +
            "and (:category is null or p.breadcrumb like %:category%) " +
            "and (:minPrice is null or p.minInitialPrice >= :minPrice) " +
            "and (:maxPrice is null or p.minInitialPrice <= :maxPrice)")
    Page<Product> shopGetDashboard(@Param("shopCode") String shopCode,
                                   @Param("title") String title,
                                   @Param("brandId") Long brandId,
                                   @Param("category") String category,
                                   @Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   Pageable pageable);

    @Query("select p from Product p " +
            "join p.shopCategoryProducts sc " +
            "where p.shopOfProduct.shopCode = :shopCode " +
            "and (:title is null or lower(p.title) like %:title%) " +
            "and (:brandId is null or p.brandOfProduct.id = :brandId) " +
            "and (:category is null or p.breadcrumb like %:category%) " +
            "and (:shopCategoryId is null or sc.shopCategory.id = :shopCategoryId)")
    Page<Product> guestGetListProduct(@Param("shopCode") String shopCode,
                                      @Param("title") String title,
                                      @Param("brandId") Long brandId,
                                      @Param("category") String category,
                                      @Param("shopCategoryId") String shopCategoryId,
                                      Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.brandOfProduct.id = ?1")
    Long countProductByBrandId(Long brandId);


    @Query("SELECT p FROM Product p JOIN p.productAttributeValues pav " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(pav.attributeValue.value) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Product> searchByTitleOrAttributeValue(@Param("searchText") String searchText);

    @Query("select p from Product p " +
            "where (:shopCode is null or p.shopOfProduct.shopCode = :shopCode) " +
            "and (:title is null or lower(p.title) like %:title%) " +
            "and (:brandId is null or p.brandOfProduct.id = :brandId) " +
            "and (:category is null or p.breadcrumb like %:category%) " +
            "and p.isAccepted = false ")
    Page<Product> findAdminDashboard(@Param("shopCode") String shopCode,
                                     @Param("title") String title,
                                     @Param("brandId") Long brandId,
                                     @Param("category") String category,
                                     Pageable pageable);
}
