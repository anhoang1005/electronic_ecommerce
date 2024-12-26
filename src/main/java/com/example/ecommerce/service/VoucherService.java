package com.example.ecommerce.service;

import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.models.voucher.*;
import com.example.ecommerce.payload.ResponseBody;

public interface VoucherService {
    ResponseBody shopCreateVoucher(CreateVoucherRequest req);

    ResponseBody adminCreateVoucher(CreateVoucherRequest req);

    ResponseBody userUpdateVoucher(CreateVoucherRequest req, boolean isAdmin);

    ResponseBody userChangeStatusVoucher(String voucherCode, Voucher.Status status, boolean isAdmin);

    ResponseBody customerGetTheirListVoucher(CustomerVoucherFilter filter);

    VoucherCheckout customerCheckoutVoucher(String voucherCode, Double totalAmount);

    VoucherCheckout customerCheckoutShippingVoucher(String voucherCode, Double totalAmount);

    VoucherCheckout customerCheckoutShopVoucher(String shopCode, String voucherCode, Double shopTotal);

    ResponseBody guestGetSystemVoucher(int pageNumber, int pageSize);

    ResponseBody guestGetShopVoucher(String shopCode, int pageNumber, int pageSize);

    ResponseBody customerGetListCheckoutVoucher(CheckoutVoucher checkoutVoucher);

    ResponseBody customerGetListCheckoutShippingVoucher(CheckoutVoucher checkoutVoucher);

    ResponseBody customerGetListCheckoutShopVoucher(CheckoutVoucher checkoutVoucher);

    ResponseBody shopVoucherDashboard(VoucherFilter ft);

    ResponseBody adminVoucherDashboard(VoucherFilter ft);
}
