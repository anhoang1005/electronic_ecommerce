package com.example.ecommerce.entities;

import com.example.ecommerce.entities.enums.ProductState;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Product")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_slug")
    private String productSlug;

    private String title;
    private Integer sold;
    private Double rating;
    private Integer review;

    @Column(name = "min_initial_price")
    private BigDecimal minInitialPrice;

    @Column(name = "max_initital_price")
    private BigDecimal maxInitialPrice;

    private String currency;
    private Long favorite;

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

    @Column(nullable = false,name = "is_available")
    private Boolean isAvailable;

    @Column(nullable = false, name = "is_hidden")
    private Boolean isHidden;

    @OneToMany(mappedBy = "productOfProductImage")
    private List<ProductImage> listImageOfProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductAttributeValue> productAttributeValues = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    public void onCreate(){
        this.sold = 0;
        this.favorite = Long.valueOf(0);
        this.isAvailable = true;
    }
}


