package com.example.ecommerce.controller.voucher;

import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.models.voucher.CheckoutVoucher;
import com.example.ecommerce.models.voucher.CreateVoucherRequest;
import com.example.ecommerce.models.voucher.CustomerVoucherFilter;
import com.example.ecommerce.models.voucher.VoucherFilter;
import com.example.ecommerce.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping("/api/admin/voucher")
    public ResponseEntity<?> adminCreateVoucher(@RequestBody CreateVoucherRequest req){
        return ResponseEntity.ok(voucherService.adminCreateVoucher(req));
    }

    @PutMapping("/api/admin/voucher")
    public ResponseEntity<?> adminUpdateVoucher(@RequestBody CreateVoucherRequest req){
        return ResponseEntity.ok(voucherService.userUpdateVoucher(req, true));
    }

    @PutMapping("/api/admin/voucher/status")
    public ResponseEntity<?> adminChangeStatusVoucher(@RequestParam("voucher_code") String voucherCode,
                                                @RequestParam("status")Voucher.Status status){
        return ResponseEntity.ok(voucherService.userChangeStatusVoucher(voucherCode, status, true));
    }

    @PostMapping("/api/owner/voucher")
    public ResponseEntity<?> shopCreateVoucher(@RequestBody CreateVoucherRequest req){
        return ResponseEntity.ok(voucherService.shopCreateVoucher(req));
    }

    @PutMapping("/api/owner/voucher")
    public ResponseEntity<?> shopUpdateVoucher(@RequestBody CreateVoucherRequest req){
        return ResponseEntity.ok(voucherService.userUpdateVoucher(req, false));
    }

    @PutMapping("/api/owner/voucher/status")
    public ResponseEntity<?> shopChangeStatusVoucher(@RequestParam("voucher_code") String voucherCode,
                                                     @RequestParam("status")Voucher.Status status){
        return ResponseEntity.ok(voucherService.userChangeStatusVoucher(voucherCode, status, false));
    }

    @GetMapping("/api/customer/voucher")
    public ResponseEntity<?> customerGetListVoucher(@RequestBody CustomerVoucherFilter filter){
        return ResponseEntity.ok(voucherService.customerGetTheirListVoucher(filter));
    }

    @GetMapping("/api/customer/voucher/system")
    public ResponseEntity<?> customerCheckout(@RequestParam("voucher_code") String voucherCode,
                                              @RequestParam("total_amount") Double totalAmount){
        return ResponseEntity.ok(voucherService.customerCheckoutVoucher(voucherCode, totalAmount));
    }

    @GetMapping("/api/customer/voucher/system-shipping")
    public ResponseEntity<?> customerCheckoutShipping(@RequestParam("voucher_code") String voucherCode,
                                                      @RequestParam("total_amount") Double totalAmount){
        return ResponseEntity.ok(voucherService.customerCheckoutShippingVoucher(voucherCode, totalAmount));
    }


    @GetMapping("/api/guest/system-voucher")
    public ResponseEntity<?> guestGetSystemVoucher(@RequestParam("page_number") int pageNumber,
                                                  @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(voucherService.guestGetSystemVoucher(pageNumber, pageSize));
    }

    @GetMapping("/api/guest/shop-voucher")
    public ResponseEntity<?> guestGetShopVoucher(@RequestParam("shop_code") String shopCode,
                                                @RequestParam("page_number") int pageNumber,
                                                @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(voucherService.guestGetShopVoucher(shopCode, pageNumber, pageSize));
    }

    @PostMapping("/api/customer/discount-system-voucher")
    public ResponseEntity<?> customerGetListCheckoutVoucher(@RequestBody CheckoutVoucher checkoutVoucher){
        return ResponseEntity.ok(voucherService.customerGetListCheckoutVoucher(checkoutVoucher));
    }

    @PostMapping("/api/customer/shipping-system-voucher")
    public ResponseEntity<?> customerGetListCheckoutShippingVoucher(@RequestBody CheckoutVoucher checkoutVoucher){
        return ResponseEntity.ok(voucherService.customerGetListCheckoutShippingVoucher(checkoutVoucher));
    }

    @PostMapping("/api/customer/discount-shop-voucher")
    public ResponseEntity<?> customerGetListCheckoutShopVoucher(@RequestBody CheckoutVoucher checkoutVoucher){
        return ResponseEntity.ok(voucherService.customerGetListCheckoutShopVoucher(checkoutVoucher));
    }

    @PostMapping("/api/owner/voucher/dashboard")
    public ResponseEntity<?> shopGetDashBoard(@RequestBody VoucherFilter filter){
        return ResponseEntity.ok(voucherService.shopVoucherDashboard(filter));
    }

    @PostMapping("/api/admin/voucher/dashboard")
    public ResponseEntity<?> adminGetDashBoard(@RequestBody VoucherFilter filter){
        return ResponseEntity.ok(voucherService.adminVoucherDashboard(filter));
    }
}
