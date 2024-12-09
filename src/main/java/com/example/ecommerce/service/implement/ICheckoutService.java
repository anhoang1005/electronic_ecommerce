package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.external.ghn.FeeRequest;
import com.example.ecommerce.models.external.ghn.ServiceResponse;
import com.example.ecommerce.models.external.ghn.TimeRequest;
import com.example.ecommerce.models.external.ghn.TimeResponse;
import com.example.ecommerce.models.order.check_out.CheckoutDto;
import com.example.ecommerce.models.order.check_out.CheckoutItem;
import com.example.ecommerce.models.order.check_out.CheckoutRequest;
import com.example.ecommerce.models.order.check_out.ShopCheckoutDto;
import com.example.ecommerce.models.voucher.VoucherCheckout;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.repository.VariantRepository;
import com.example.ecommerce.service.CheckoutService;
import com.example.ecommerce.service.GHNService;
import com.example.ecommerce.service.VoucherService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final VariantRepository variantRepository;
    private final AddressRepository addressRepository;

    public ICheckoutService(AuthenticationUtils authUtils,
                            GHNService ghnService,
                            @Value("${ghn.shopDistrict}") String shopDistrict,
                            @Value("${ghn.shopWard}") String shopWard,
                            DateTimeMapperUtils timeUtils,
                            VoucherService voucherService,
                            UsersRepository usersRepository,
                            VariantRepository variantRepository, AddressRepository addressRepository){
        this.authUtils = authUtils;
        this.ghnService = ghnService;
        this.shopDistrict = shopDistrict;
        this.shopWard = shopWard;
        this.timeUtils = timeUtils;
        this.voucherService = voucherService;
        this.usersRepository = usersRepository;
        this.variantRepository = variantRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    public ResponseBody customerGetCheckOut(CheckoutRequest req) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            Address address = addressRepository.findById(req.getAddressId())
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));

            Map<String, ShopCheckoutDto> shopCheckout = new HashMap<>();
            BigDecimal allProductTotal = BigDecimal.ZERO;

            //Tinh toan tung item cua tung shop
            for (CheckoutItem item : req.getItems()){
                ProductVariant variant = variantRepository.findById(item.getVariantId())
                        .orElseThrow(() -> new RequestNotFoundException("ERROR"));
                if(variant.getStockQuantity() - item.getQuantity()<0) {
                    return new ResponseBody("", ResponseBody.Status.SUCCESS,
                            "HET_HANG", ResponseBody.Code.SUCCESS);
                } else{
                    Product product = variant.getProduct();
                    Shop shop = product.getShopOfProduct();
                    String shopCode = shop.getShopCode();
                    ShopCheckoutDto.ShopItem shopItem = ShopCheckoutDto.ShopItem.builder()
                            .productId(product.getId()).variantId(variant.getId())
                            .title(product.getTitle()).imageUrl(product.getImageList())
                            .variant(variant.getProductVariation()!=null ? variant.getProductVariation() : null)
                            .sellPrice(variant.getPrice()).quantity(item.getQuantity())
                            .itemTotal(variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .build();
                    if (!shopCheckout.containsKey(shopCode)) {
                        ShopCheckoutDto newCheckout = new ShopCheckoutDto();
                        newCheckout.setShopCode(shopCode);
                        newCheckout.setShopName(shop.getShopName());
                        newCheckout.setShopLogo(shop.getShopLogo());
                        newCheckout.setProductTotal(variant.getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())));
                        newCheckout.setTotalAmount(
                                newCheckout.getProductTotal().subtract(newCheckout.getDiscount()));
                        newCheckout.getListItem().add(shopItem);
                        newCheckout.setTotalWidth(variant.getWidth());
                        newCheckout.setTotalLength(variant.getLength());
                        newCheckout.setTotalHeight(variant.getHeight());
                        newCheckout.setTotalWeight(variant.getWeight());
                        shopCheckout.put(shopCode, newCheckout);
                        allProductTotal = allProductTotal.add(newCheckout.getProductTotal());
                    } else {
                        ShopCheckoutDto existed = shopCheckout.get(shopCode);
                        existed.setProductTotal(existed.getProductTotal()
                                .add(variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
                        existed.getListItem().add(shopItem);
                        existed.setTotalAmount(existed.getProductTotal().subtract(existed.getDiscount()));
                        existed.setTotalWidth(existed.getTotalWidth() + variant.getWidth());
                        existed.setTotalLength(existed.getTotalLength() + variant.getLength());
                        existed.setTotalHeight(existed.getTotalHeight() + variant.getHeight());
                        existed.setTotalWeight(existed.getTotalWeight() + variant.getWeight());
                        allProductTotal = allProductTotal.add(existed.getProductTotal());
                    }
                }
            }

            //Tinh toan voucher cua tung shop
            BigDecimal discountVoucher = BigDecimal.ZERO;
            if(req.getShopDiscounts()!=null){
                for (CheckoutRequest.ShopDiscount discount : req.getShopDiscounts()){
                    if(!shopCheckout.containsKey(discount.getShopCode())){
                        throw new RequestNotFoundException("ERROR");
                    } else{
                        ShopCheckoutDto existed = shopCheckout.get(discount.getShopCode());
                        VoucherCheckout voucherCheckout = voucherService
                                .customerCheckoutShopVoucher(discount.getShopCode(), existed.getProductTotal().doubleValue());

                        existed.setVoucher(voucherCheckout.getShopVoucherDto());
                        existed.setDiscount(BigDecimal.valueOf(voucherCheckout.getDiscount()));
                        existed.setTotalAmount(existed.getProductTotal().subtract(existed.getDiscount()));
                        discountVoucher = discountVoucher.add(existed.getDiscount());
                    }
                }
            }
            List<ShopCheckoutDto> shopCheckoutList = new ArrayList<>(shopCheckout.values());

            //Chu y dang dat gia tri mac dinh cho shop
            //Ship - time
            BigDecimal totalFee = BigDecimal.ZERO;
            for(ShopCheckoutDto dto : shopCheckoutList){
                ServiceResponse response = ghnService.getService(Integer.valueOf(address.getDistrictId()));
                String service = response.getData().get(0).getService_id();
                int weight = dto.getTotalWeight().intValue();
                FeeRequest feeRequest = FeeRequest.builder()
                        .service_id(service)
                        .insurance_value(String.valueOf(dto.getProductTotal().longValue()))
                        .coupon(null)
                        .from_district_id(shopDistrict)
                        .to_district_id(address.getDistrictId()).to_ward_code(address.getWardId())
                        .width("20").length("20").height("20").weight(Integer.toString(weight))
                        .build();
                Double shopFee = ghnService.getFee(feeRequest);
                dto.setFee(BigDecimal.valueOf(shopFee));
                dto.setTotalAmount(dto.getTotalAmount().add(BigDecimal.valueOf(shopFee)));
                totalFee = totalFee.add(BigDecimal.valueOf(shopFee));

                TimeRequest timeRequest = TimeRequest.builder()
                        .service_id(service)
                        .from_district_id(shopDistrict).from_ward_code(shopWard)
                        .to_district_id(address.getDistrictId()).to_ward_code(address.getWardId())
                        .build();
                TimeResponse timeResponse = ghnService.getTime(timeRequest);
                dto.setLeadDay(timeUtils.timestampToDateDifference(timeResponse.getData().getLeadtime()));
                dto.setLeadTime(timeUtils.timestampToDate(timeResponse.getData().getLeadtime()));
            }

            //Voucher cua san (gia tri)
            BigDecimal ecommerceDiscount = BigDecimal.ZERO;
            VoucherDto sanDiscountVoucher;
            if(req.getDiscountVoucher()!=null){
                VoucherCheckout ecommerceDiscountVoucher = voucherService
                        .customerCheckoutVoucher(req.getDiscountVoucher(), allProductTotal.doubleValue());
                ecommerceDiscount = BigDecimal.valueOf(ecommerceDiscountVoucher.getDiscount());
                sanDiscountVoucher = ecommerceDiscountVoucher.getVoucherDto();
            }else{
                sanDiscountVoucher = null;
            }

            //Vouher cua san (shipping)
            BigDecimal ecommerceShipping = BigDecimal.ZERO;
            VoucherDto sanShippingVoucher;
            if(req.getDiscountVoucher()!=null){
                VoucherCheckout ecommerceDiscountVoucher = voucherService
                        .customerCheckoutShippingVoucher(req.getShippingVoucher(), allProductTotal.doubleValue());
                ecommerceShipping = BigDecimal.valueOf(ecommerceDiscountVoucher.getDiscount());
                sanShippingVoucher = ecommerceDiscountVoucher.getVoucherDto();
            } else {
                sanShippingVoucher = null;
            }

            //Tien ship


            //TInh toan so tien cuoi cung
            BigDecimal totalPrice = allProductTotal //Tong tien hang(chua tinh voucher)
                    .subtract(discountVoucher) //Voucher shop
                    .subtract(ecommerceDiscount).subtract(ecommerceShipping) //Voucher san
                    .add(totalFee); //Tien ship

            CheckoutDto checkoutDto = CheckoutDto.builder()
                    .fullName(users.getFirstName() + " " + users.getLastName())
                    .addressDto(null)
                    .itemsCount(req.getItems().size())
                    .productTotal(allProductTotal)
                    .voucherDiscount(discountVoucher)
                    .shippingDiscount(BigDecimal.ZERO)
                    .discountVoucher(sanDiscountVoucher)
                    .shippingVoucher(sanShippingVoucher)
                    .totalPrice(totalPrice)
                    .shopBill(shopCheckoutList)
                    .build();

            return new ResponseBody(checkoutDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody customerSubmitCheckOut(CheckoutRequest req) {
        return null;
    }
}
