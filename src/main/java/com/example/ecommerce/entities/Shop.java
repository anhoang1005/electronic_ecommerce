package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "shop", indexes = {
        @Index(name = "idx_shop_shopCode", columnList = "shopCode")
})
public class Shop extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(15)", unique = true)
    private String shopCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessType businessType;

    @Column(nullable = false)
    private String shopName;

    @Column(columnDefinition = "TEXT")
    private String shopLogo;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer reviewCount;
    private Long totalReview;
    private Double rating;
    private Integer productQuantity;
    private Integer followCount;
    private Integer waitingOrder;
    private Integer processOrder;
    private Integer deliveringOrder;
    private Integer successOrder;
    private Integer cancelOrder;
    private Integer returnOrder;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 15)
    private String shopHotLine;

    @Column
    private String taxCode;

    @Column(nullable = false, length = 20)
    private String identityCode;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 150)
    private String shopEmail;

    @Column(nullable = true)
    private String frontIdentityCard;

    @Column(nullable = true)
    private String backIdentityCard;

    @Column(nullable = false)
    private String wardId;

    @Column(nullable = false)
    private String districtId;

    @Column(nullable = false)
    private String provinceId;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean enable;

    @Version
    private long version;

    @PrePersist
    public void initializeDefaultValues() {
        this.totalReview = 0L;
        this.reviewCount = 0;
        this.rating = 0.0;
        this.productQuantity = 0;
        this.followCount = 0;
        this.waitingOrder = 0;
        this.processOrder = 0;
        this.deliveringOrder = 0;
        this.successOrder = 0;
        this.cancelOrder = 0;
        this.returnOrder = 0;
        this.active = true;
        this.enable = true;
    }

    @PostPersist
    public void updateShopCode() {
        this.shopCode = "SH" + String.format("%08d", this.id); // cap nhat vơi shopCode thuc te
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usersInShop;

    @OneToMany(mappedBy = "shopOfMessageStory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MessageChat> messageStoryOfShop;

    @OneToMany(mappedBy = "shopOfShopReview", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShopReview> shopReviewOfShop;

    @ManyToMany(mappedBy = "listShopUserFollow", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Users> listUsersFollowShop;

    @OneToMany(mappedBy = "shopOfCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> listCategoryOfShop;

    @OneToMany(mappedBy = "shopOfOrdersShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdersShop> listOrdersShopOfShop;

    public enum BusinessType{
        CA_NHAN(1, "Cá nhân"),
        HO_KINH_DOANH(2, "Hộ kinh doanh"),
        CONG_TY(3, "Công ty");
        private final int valueInt;
        private final String valueString;
        BusinessType(int valueInt,String valueString){
            this.valueInt = valueInt;
            this.valueString = valueString;
        }
        public int getValueInt() { return this.valueInt; }
        public String getValueString() { return  this.valueString; }
    }
}
