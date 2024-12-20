package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer soldQuantity;
    private String variantSlug;
    @Column(nullable = false)
    private Integer stockQuantity;

    private String sku;
    private BigDecimal price;

    private Double height;
    private Double weight;
    private Double length;
    private Double width;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageList;

    @OneToOne(mappedBy = "variant",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ProductImage productImage;

    @Lob
    @Column(name = "product_variantion", columnDefinition = "MEDIUMTEXT")
    private String productVariation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy="variant",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariantAttributeValue> variantAttributeValueList = new ArrayList<>();
    @PrePersist
    public void onCreate(){
        this.soldQuantity = 0;
    }

    public enum Status{
        HET_HANG,
        SAP_HET_HANG,
        CON_HANG,
        GIOI_HAN,
        NGUNG_KINH_DOANH,
        CHO_NHAP_HANG,
        CHI_BAN_TAI_CUA_HANG,
        HANG_SAP_VE
    }
}
