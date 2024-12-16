package com.example.ecommerce.entities;

import com.example.ecommerce.entities.enums.ProductState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@Where(clause = "is_deleted = false and is_hidden = false")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(name = "product_slug")
    private String productSlug;

    private String title;
    private Integer sold;
    private Integer review;

    @Column(name = "min_initial_price")
    private BigDecimal minInitialPrice;

    @Column(name = "max_initital_price")
    private BigDecimal maxInitialPrice;

    private String currency;
    private Long favorite;
    private Long starCount;
    private Long reviewCount;
    private Double rating;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String breadcrumb;

    @Lob
    @Column(name = "product_specifications", columnDefinition = "MEDIUMTEXT")
    private String productSpecifications;

    @Lob
    @Column(name = "product_description",columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String variations;

    @Lob
    @Column(name = "product_ratings", columnDefinition = "MEDIUMTEXT")
    private String productRatings;

    @Column(nullable = false, name = "is_hidden")
    private Boolean isHidden;

    @Column(nullable = false, name = "is_accepted")
    private Boolean isAccepted;

    @Column(nullable = false, name = "is_deleted")
    private Boolean isDeleted;

    @Column(nullable = false, name = "is_rejected")
    private Boolean isRejected  ;

    @OneToMany(mappedBy = "productOfProductImage",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference("product-image")
    private List<ProductImage> listImageOfProduct = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "brand_id")
//    private Brand brand;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageList;
    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("product-attribute-value-product")
    private List<ProductAttributeValue> productAttributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonManagedReference("product-category-product")
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
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

    private String variantIds;
    private String imageIds;

    @PrePersist
    public void onCreate() {
        if (this.sold == null) {
            this.sold = 0;
        }
        if (this.favorite == null) {
            this.favorite = 0L;
        }
        if (this.isAccepted == null) {
            this.isAccepted = false;
        }
        if (this.rating == null) {
            this.rating = 0.0;
        }
        if (this.reviewCount == null) {
            this.reviewCount = 0L;
        }
        if (this.starCount == null) {
            this.starCount = 0L;
        }
        if (this.isHidden == null) {
            this.isHidden = false;
        }
        this.isDeleted = false;
        this.isRejected = false;
    }

}


