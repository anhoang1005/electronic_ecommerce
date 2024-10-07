package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String houseName;

    @Column(nullable = false)
    private String wardName;

    @Column(nullable = false)
    private String districtName;

    @Column(nullable = false)
    private String provinceName;

    @Column
    private String note;

    @Column(nullable = false)
    private String wardId;

    @Column(nullable = false)
    private String districtId;

    @Column(nullable = false)
    private String provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity usersEntityInAddress;

}
