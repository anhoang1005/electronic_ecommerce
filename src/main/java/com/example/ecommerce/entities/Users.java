package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_usercode", columnList = "userCode")
})
public class Users extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String userCode;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = true)
    private String verifyCode;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean enable;

    @OneToOne(mappedBy = "usersInShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Shop shopOfUsers;

    @OneToMany(mappedBy = "usersEntityInAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Address> listAddressEntity;

    @OneToMany(mappedBy = "usersOfMessageStory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MessageChat> messageChatOfUser;

    @OneToMany(mappedBy = "usersOfShopReview", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShopReview> listShopReviewOfUsers;

    @OneToMany(mappedBy = "usersOfOrder", fetch = FetchType.LAZY)
    private List<Orders> listOrdersOfUsers;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Roles> listRolesOfUsers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "shop_follow",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Shop> listShopUserFollow;

    @ManyToMany
    @JoinTable(
            name = "review_like",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<ProductReview> listProductReviewUserLike;

    @PostPersist
    public void updateUserCode() {
        this.userCode = "AC" + String.format("%08d", this.id); // cap nhat vơi userCode thuc te
    }

    public enum Gender{
        NAM("Nam"),
        NU("Nữ");
        private final String valueString;
        Gender(String valueString) {this.valueString = valueString; }
        public String getValueString() { return this.valueString; }
    }
}
