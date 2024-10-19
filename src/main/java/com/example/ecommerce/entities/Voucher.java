package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "vourches")
public class Voucher{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long id;
    private String voucherCode;
    private VoucherType voucherType;
    private String voucherTitle;
    private String description;
    private Integer voucherCount;
    private Integer remainingCount;
    private Double voucherValue;
    private Double minTotalOrder;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private Boolean enable;
    private Boolean active;

    public enum VoucherType{
        GIA_TRI(1, "Giá trị"),
        PHAN_TRAM(2, "Phần trăm");
        private final int valueInt;
        private final String valueString;
        VoucherType(int valueInt, String valueString){
            this.valueInt = valueInt;
            this.valueString = valueString;
        }
        public String getValueString() { return this.valueString; }
        public int getValueInt() { return  this.valueInt; }
    }
}
