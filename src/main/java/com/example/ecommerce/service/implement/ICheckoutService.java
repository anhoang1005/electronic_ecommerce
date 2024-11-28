package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.order.check_out.CheckoutItem;
import com.example.ecommerce.models.order.check_out.CheckoutRequest;
import com.example.ecommerce.models.order.check_out.ShopCheckoutDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.CheckoutService;
import com.example.ecommerce.service.GHNService;
import com.example.ecommerce.service.VoucherService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ICheckoutService implements CheckoutService {
    private final AuthenticationUtils authUtils;
    private final VoucherService voucherService;
    private final GHNService ghnService;
    private final DateTimeMapperUtils timeUtils;
    private final String shopDistrict;
    private final String shopWard;
    private final UsersRepository usersRepository;

    public ICheckoutService(AuthenticationUtils authUtils,
                            GHNService ghnService,
                            @Value("${ghn.shopDistrict}")String shopDistrict,
                            @Value("${ghn.shopWard}")String shopWard,
                            DateTimeMapperUtils timeUtils,
                            VoucherService voucherService,
                            UsersRepository usersRepository){
        this.authUtils = authUtils;
        this.ghnService = ghnService;
        this.shopDistrict = shopDistrict;
        this.shopWard = shopWard;
        this.timeUtils = timeUtils;
        this.voucherService = voucherService;
        this.usersRepository = usersRepository;
    }

    @Override
    public ResponseBody customerGetCheckOut(CheckoutRequest req) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));

            double productTotal = 0;
            for (CheckoutItem item : req.getItems()){
                productTotal += item.getQuantity()*100000.0;
            }

            double discountVoucher = 0;
            if(req.getDiscountVoucher()!=null) {
                voucherService.customerCheckoutVoucher(req.getDiscountVoucher(), productTotal);
            } else {
                discountVoucher = 0;
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new RequestNotFoundException(e.getMessage());
        }
    }
}
