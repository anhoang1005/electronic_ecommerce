package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_name")
    private String attributeName;

    @Lob
    @Column(name = "value_json", columnDefinition = "MEDIUMTEXT")
    private String valueJson;
}
