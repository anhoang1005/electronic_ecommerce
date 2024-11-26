package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopVoucher;
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
public interface ShopVoucherRepository extends JpaRepository<ShopVoucher, Long> {
    Optional<ShopVoucher> findShopVoucherByVoucherCode(String voucherCode);

    @Query(value = "select v from ShopVoucher v " +
            "where (:voucherCode is null or v.voucherCode = :voucherCode) " +
            "and (:discountType is null or v.discountType = :discountType) " +
            "and ((:startSt is null or :startEd is null) or v.startedAt between :startSt and :startEd) " +
            "and ((:expirySt is null or :expiryEd is null) or v.expiredAt between :expirySt and :expiryEd)")
    Page<Voucher> adminGetListVoucher(@Param("voucherCode") String voucherCode,
                                      @Param("discountType") Voucher.DiscountType discountType,
                                      @Param("startSt") LocalDateTime startSt,
                                      @Param("startEd") LocalDateTime startEd,
                                      @Param("expirySt") LocalDateTime expirySt,
                                      @Param("expiryEd") LocalDateTime expiryEd,
                                      Pageable pageable);
}
