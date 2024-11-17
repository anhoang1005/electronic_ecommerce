package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "variants")
public class Variant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @Column(name = "count_review")
    private Integer countReview;

    @Column(name = "variant_slug")
    private String variantSlug;

    private String sku;
    private BigDecimal price;

    private Double height;
    private Double weight;
    private Double length;
    private Double width;

    @OneToOne(mappedBy = "variant",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ProductImage productImage;
    @Lob
    @Column(name = "product_variantion", columnDefinition = "MEDIUMTEXT")
    private String productVariation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;


    @OneToMany(mappedBy="variant",cascade = CascadeType.ALL)
    private List<VariantAttributeValue> variantAttributeValueList = new ArrayList<>();
    @PrePersist
    public void onCreate(){
        this.countReview = 0;
        this.soldQuantity = 0;
    }
}
