package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
    Optional<Voucher> findVoucherByVoucherCode(String voucherCode);

    @Query(value = "select v from Voucher v " +
            "where v.voucherCode = :voucherCode " +
            "and v.status = :status " +
            "and v.expiredAt < :currentDateTime " +
            "and (:type is null or v.type = :type)" )
    Page<Voucher> customerGetListVoucher(@Param("voucherCode") String voucherCode,
                                         @Param("type") Voucher.Type type,
                                         @Param("status") Voucher.Status status,
                                         @Param("current") LocalDateTime currentDateTime,
                                         Pageable pageable);



}
