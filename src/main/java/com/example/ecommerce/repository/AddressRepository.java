package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "select a from Address a " +
            "where a.usersEntityInAddress.email = :userEmail")
    List<Address> getListAddressByUserEmail(@Param("userEmail") String userEmail);

    @Query(value = "select a from Address a " +
            "where a.id = :addressId " +
            "and a.usersEntityInAddress.email = :email")
    Address findAddressByIdAndUserEmail(@Param("addressId") Long addressId,
                                        @Param("email") String email);
}
