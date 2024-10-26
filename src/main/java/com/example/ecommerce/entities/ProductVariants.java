package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_variants")
public class ProductVariants extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double sellPrice;

    @Column(nullable = false)
    private Integer soldQuantity;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column
    private Status variantsStatus;

    @Column(columnDefinition = "TEXT")
    private String note;

    private Boolean active;

    @PrePersist
    public void initializeDefaultValues() {
        this.soldQuantity = 0;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product productOfProductVariants;

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
