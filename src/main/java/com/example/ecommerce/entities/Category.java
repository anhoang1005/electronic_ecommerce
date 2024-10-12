package com.example.ecommerce.entities;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String categoryType;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shopOfCategory;

    @OneToMany(mappedBy = "categoryOfProductCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCategory> listProductCategoryOfCategory;
}
