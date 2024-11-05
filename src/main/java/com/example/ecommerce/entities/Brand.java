package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "brand")
public class Brand extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer productQuantity;

    @Column(nullable = false)
    private Boolean active;

//    @OneToMany(mappedBy = "brandInProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Product> listProductInBrand;
}
