package com.example.ecommerce.entities;

import com.example.ecommerce.entities.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shop_categories")
public class ShopCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private ShopCategory parentCategory;

    @OneToMany(
            mappedBy = "parentCategory",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonManagedReference
    private List<ShopCategory> childrens;


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

    @Column(nullable = false,name ="id_delete")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shopOfCategory;
    @OneToMany(mappedBy = "shopCategory",fetch = FetchType.EAGER)
    @JsonManagedReference("shop-category-shop-category-product")
    private List<ShopCategoryProduct> shopCategoryProducts;
    @PrePersist
    public void onCreate(){
        this.isDelete = false;
        this.active = false;
    }
}
