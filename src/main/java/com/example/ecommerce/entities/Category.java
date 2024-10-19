package com.example.ecommerce.entities;

import com.example.ecommerce.entities.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "category", indexes = {
        @Index(name = "idx_category_parent_id", columnList = "parentId")
})
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long parentId;

    @Column
    private Integer categoryLevel;

    @Column(name = "category_name",nullable = false)
    private String categoryName;

    @Column(name = "category_code",nullable = false)
    private String categoryCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false,name = "category_type")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shopOfCategory;

    @OneToMany(mappedBy = "categoryOfProductCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCategory> listProductCategoryOfCategory;
}
