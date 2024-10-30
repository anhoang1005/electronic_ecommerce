package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories_attributes_values")
public class CategoryAttributeValue extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @OneToOne
    @JoinColumn(name = "attribute_value_id")
    private AttributeValue attributeValue;
}
