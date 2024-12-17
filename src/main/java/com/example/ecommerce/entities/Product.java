package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Product")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String productSlug;
    private String title;
    @Column
    private BigDecimal minInitialPrice;

    @Column
    private BigDecimal maxInitialPrice;

    private String currency;
    private Integer soldCount;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String breadcrumb;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String productSpecifications;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String detail;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageList;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String variations;

    @Column(nullable = false)
    private Boolean isHidden;

    @Column(nullable = false)
    private Boolean isAccepted;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Boolean isReject;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @OneToMany(mappedBy = "productOfProductImage",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("product-image")
    private List<ProductImage> listImageOfProduct;

    @JsonIgnore
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("product-attribute-value-product")
    private List<ProductAttributeValue> productAttributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference("product-category-product")
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference("product-shop-category-product")
    private List<ShopCategoryProduct> shopCategoryProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference("product-category")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @JsonBackReference("product-shop")
    private Shop shopOfProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "brand_id")
    private Brand brandOfProduct;

    @PrePersist
    public void onCreate() {
        if (this.isAccepted == null) {
            this.isAccepted = false;
        }
        if(this.soldCount == null){
            this.soldCount = 0;
        }
        this.isDeleted = false;
        this.isReject = true;
    }

    public enum ProductState {
        ACTIVE,
        UN_ACTIVE,
        OUT_OF_STOCK,
        PENDING
    }

    @PostPersist
    public void update(){
        this.productCode = "PD" + String.format("%08d", this.id);
    }

}


