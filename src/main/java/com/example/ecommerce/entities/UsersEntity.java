package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UsersEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = true, unique = true,updatable = false)
    private String userCode;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true,updatable = false)
    private String email;

    @Column(nullable = true, unique = true)
    private String identityCode;

    @Column(nullable = true)
    private String frontIdentityImage;

    @Column(nullable = true)
    private String backIdentityImage;

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

    @OneToMany(mappedBy = "usersEntityInAddress")
    private List<AddressEntity> addressEntity;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles = new ArrayList<>();

    @PostPersist
    public void updateUserCode() {
        this.userCode = "AC" + String.format("%08d", this.id); // cap nhat vơi userCode thuc te
    }
}
