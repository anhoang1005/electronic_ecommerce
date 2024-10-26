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

    private Integer productQuantity;

    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void initializeDefaultValues() {
        this.active = true;
        this.productQuantity = 0;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shopOfCategory;

    @ManyToMany(mappedBy = "listCategoryInProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> listProductOfCategory;
}
