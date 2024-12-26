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

    Boolean existsByVoucherCode(String voucherCode);

    @Query(value = "select v from ShopVoucher v " +
            "where v.voucherCode = :voucherCode " +
            "and v.shopOfShopVoucher.shopCode = :shopCode")
    Optional<ShopVoucher> findByShopCodeAndVoucherCode(@Param("shopCode") String shopCode,
                                                       @Param("voucherCode") String voucherCode);

    @Query(value = "select v from ShopVoucher v " +
            "where v.shopOfShopVoucher.shopCode = :shopCode " +
            "and v.status = :status " +
            "and v.expiredAt > :current and v.startedAt < :current ")
    Page<ShopVoucher> guestGetShopVoucher(@Param("shopCode") String shopCode,
                                          @Param("status") Voucher.Status status,
                                          @Param("current") LocalDateTime current,
                                          Pageable pageable);

    @Query(value = "select v from ShopVoucher v " +
            "where v.shopOfShopVoucher.shopCode = :shopCode " +
            "and v.status = :status " +
            "and (:code is null or v.voucherCode = :code) " +
            "and v.expiredAt > :current and v.startedAt < :current " +
            "and v.minTotalOrder <= :minTotalOrder " )
    Page<ShopVoucher> customerGetShopVoucherCheckOut(@Param("shopCode") String shopCode,
                                                     @Param("code") String code,
                                                     @Param("status") Voucher.Status status,
                                                     @Param("current") LocalDateTime current,
                                                     @Param("minTotalOrder") Double minTotalOrder,
                                                     Pageable pageable);

    @Query(value = "select v from ShopVoucher v " +
            "where v.shopOfShopVoucher.shopCode = :shopCode " +
            "and (:voucherCode is null or v.voucherCode = :voucherCode) " +
            "and (:discountType is null or v.discountType = :discountType) " +
            "and ((:startSt is null or :startEd is null) or v.startedAt between :startSt and :startEd) " +
            "and ((:expirySt is null or :expiryEd is null) or v.expiredAt between :expirySt and :expiryEd)" +
            "and (:status is null or v.status = :status)")
    Page<ShopVoucher> shopGetListVoucher(
            @Param("shopCode") String shopCode,
            @Param("voucherCode") String voucherCode,
            @Param("discountType") Voucher.DiscountType discountType,
            @Param("startSt") LocalDateTime startSt,
            @Param("startEd") LocalDateTime startEd,
            @Param("expirySt") LocalDateTime expirySt,
            @Param("expiryEd") LocalDateTime expiryEd,
            @Param("status") Voucher.Status status,
            Pageable pageable);
}
