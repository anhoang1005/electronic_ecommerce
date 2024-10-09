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
    private Integer level;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String categoryType;

    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(mappedBy = "listCategoryInProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> listProductInCategory;
}
