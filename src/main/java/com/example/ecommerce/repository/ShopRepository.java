package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findShopByShopCode(String shopCode);
    Shop findShopById(Long id);

    @Query("select case when count(s) > 0 then true else false end " +
            "from Shop s " +
            "where s.shopCode = :shopCode " +
            "and :users member of s.listUsersFollowShop")
    Boolean checkUserFollow(@Param("users") Users users,
                            @Param("shopCode") String shopCode);

    @Query("select u from Shop s join s.listUsersFollowShop u " +
            "where s.shopCode = :shopCode")
    Page<Users> getUserFollowShop(@Param("shopCode") String shopCode,
                                  Pageable pageable);

}
