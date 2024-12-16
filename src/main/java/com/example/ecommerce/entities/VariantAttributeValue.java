package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "variants_attributes_values")
@Entity
public class VariantAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private Variant variant;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "attribute_value_id")
    private AttributeValue attributeValue;


}
