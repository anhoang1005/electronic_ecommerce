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
            "where (:voucherCode is null or v.voucherCode = :voucherCode) " +
            "and v.status = :status " +
            "and v.expiredAt > :current " +
            "and (:type is null or v.type = :type) " +
            "and (:discountType is null or v.discountType = :discountType) " )
    Page<Voucher> customerGetListVoucher(@Param("voucherCode") String voucherCode,
                                         @Param("type") Voucher.Type type,
                                         @Param("discountType") Voucher.DiscountType discountType,
                                         @Param("status") Voucher.Status status,
                                         @Param("current") LocalDateTime currentDateTime,
                                         Pageable pageable);

    @Query(value = "select v from Voucher v " +
            "where (:voucherCode is null or v.voucherCode = :voucherCode) " +
            "and v.status = :status " +
            "and v.expiredAt > :current and v.startedAt < :current " +
            "and (:type is null or v.type = :type) " +
            "and (:discountType is null or v.discountType = :discountType) " +
            "and v.minTotalOrder <= :minTotalOrder " )
    Page<Voucher> customerGetListVoucherCheckout(@Param("voucherCode") String voucherCode,
                                         @Param("type") Voucher.Type type,
                                         @Param("discountType") Voucher.DiscountType discountType,
                                         @Param("status") Voucher.Status status,
                                         @Param("current") LocalDateTime currentDateTime,
                                         @Param("minTotalOrder") Double minTotalOrder,
                                         Pageable pageable);

    @Query(value = "select v from Voucher v " +
            "where (:voucherCode is null or v.voucherCode = :voucherCode) " +
            "and (:type is null or v.type = :type) " +
            "and (:discountType is null or v.discountType = :discountType) " +
            "and ((:startSt is null or :startEd is null) or v.startedAt between :startSt and :startEd) " +
            "and ((:expirySt is null or :expiryEd is null) or v.expiredAt between :expirySt and :expiryEd)" +
            "and (:status is null or v.status = :status)")
    Page<Voucher> adminGetListVoucher(@Param("voucherCode") String voucherCode,
                                      @Param("type") Voucher.Type type,
                                      @Param("discountType") Voucher.DiscountType discountType,
                                      @Param("startSt") LocalDateTime startSt,
                                      @Param("startEd") LocalDateTime startEd,
                                      @Param("expirySt") LocalDateTime expirySt,
                                      @Param("expiryEd") LocalDateTime expiryEd,
                                      @Param("status") Voucher.Status status,
                                      Pageable pageable);

}
