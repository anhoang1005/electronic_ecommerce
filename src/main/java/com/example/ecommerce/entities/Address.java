package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "address")
public class Address extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String houseName;

    @Column(nullable = false, length = 100)
    private String wardName;

    @Column(nullable = false, length = 100)
    private String districtName;

    @Column(nullable = false, length = 100)
    private String provinceName;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column
    private String note;

    @Column(nullable = false, length = 20)
    private String wardId;

    @Column(nullable = false, length = 20)
    private String districtId;

    @Column(nullable = false, length = 20)
    private String provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersEntityInAddress;

    public enum AddressType{
        NHA_RIENG(1, "Nhà Riêng"),
        VAN_PHONG(2, "Văn Phòng");
        private final int valueInt;
        private final String valueString;
        AddressType(int valueInt, String valueString){
            this.valueInt = valueInt;
            this.valueString = valueString;
        }
        public int getValueInt(){ return this.valueInt; }
        public String getValueString(){ return this.valueString; }
    }

}
