package com.example.ecommerce.service;

import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.models.voucher.CreateVoucherRequest;
import com.example.ecommerce.models.voucher.VoucherCheckout;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.payload.ResponseBody;

public interface VoucherService {
    ResponseBody shopCreateVoucher(CreateVoucherRequest req);

    ResponseBody adminCreateVoucher(CreateVoucherRequest req);

    ResponseBody userUpdateVoucher(CreateVoucherRequest req, boolean isAdmin);

    ResponseBody userChangeStatusVoucher(String voucherCode, Voucher.Status status, boolean isAdmin);

    ResponseBody customerGetTheirListVoucher(String voucherCode, Voucher.Type type, int pageNumber, int pageSize);

    VoucherCheckout customerCheckoutVoucher(String voucherCode, Double totalAmount);

    VoucherCheckout customerCheckoutShippingVoucher(String voucherCode, Double totalAmount);

    VoucherCheckout customerCheckoutShopVoucher(String voucherCode, Double shopTotal);

    VoucherDto customerUseVoucher(String voucherCode);

    ResponseBody shopVoucherDashboard();

    ResponseBody adminVoucherDashboard();
}
