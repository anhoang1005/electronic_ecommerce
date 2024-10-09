package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Roles extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleCode;

    @Column(nullable = false, unique = true)
    private String roleName;

    @Column
    private String note;

    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();

    public enum Position{
        QUANLI(1, "Quản lí"),
        CHUCUAHANG(2, "Chủ cửa hàng"),
        KHACHHANG(3, "Khách hàng"),
        CONGTACVIEN(4, "Cộng tác viên");
        private final int valueInt;
        private final String valueString;
        Position(int valueInt, String valueString){
            this.valueInt = valueInt;
            this.valueString = valueString;
        }
        public int getValueInt(){ return this.valueInt; }
        public String getValueString(){ return this.valueString; }
    }
}
