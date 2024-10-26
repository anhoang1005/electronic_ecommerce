package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Product")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, unique = true, length = 30)
    private String productCode;
    @Column(nullable = false, length = 150)
    private String productSlug;
    @Column(nullable = false, length = 150)
    private String productTitle;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String productDetail;
    private Double rating;
    private Long totalStar;
    private Integer reviewCount;
    private Integer soldCount;
    private Integer stockCount;
    private Integer viewCount;
    private Integer likeCount;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false)
    private Boolean enable;
    @PrePersist
    public void initializeDefaultValues() {
        this.rating = 0.0;
        this.totalStar = 0L;
        this.reviewCount = 0;
        this.reviewCount = 0;
        this.soldCount = 0;
        this.stockCount = 0;
        this.viewCount = 0;
        this.likeCount = 0;
        this.active = false;
        this.enable = false;
    }

    @OneToMany(mappedBy = "productOfProductImage")
    private List<ProductImage> listImageOfProduct;

    @OneToMany(mappedBy = "productOfProductVariants")
    private List<ProductVariants> listVariantsOfProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    private Brand brandInProduct;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> listCategoryInProduct;

}


