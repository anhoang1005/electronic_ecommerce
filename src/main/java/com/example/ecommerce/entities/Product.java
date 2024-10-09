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

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double sellPrice;

    @Column(nullable = false)
    private Integer soldQuantity;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "productOfProductImage")
    private List<ProductImage> listImageOfProduct;

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


