package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.address.AddressMapper;
import com.example.ecommerce.models.address.AddressDto;
import com.example.ecommerce.models.external.ghn.FeeRequest;
import com.example.ecommerce.models.external.ghn.ServiceResponse;
import com.example.ecommerce.models.external.ghn.TimeRequest;
import com.example.ecommerce.models.external.ghn.TimeResponse;
import com.example.ecommerce.models.order.check_out.*;
import com.example.ecommerce.models.voucher.VoucherCheckout;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.CheckoutService;
import com.example.ecommerce.service.GHNService;
import com.example.ecommerce.service.VnPayService;
import com.example.ecommerce.service.VoucherService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.example.ecommerce.utils.MoneyUtils;
import com.example.ecommerce.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
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
    private final AddressMapper addressMapper;
    private final ShopRepository shopRepository;
    private final MoneyUtils moneyUtils;
    private final OrderRepository orderRepository;
    private final VnPayService vnPayService;
    private final OrderShopRepository orderShopRepository;

    public ICheckoutService(AuthenticationUtils authUtils,
                            GHNService ghnService,
                            @Value("${ghn.shopDistrict}") String shopDistrict,
                            @Value("${ghn.shopWard}") String shopWard,
                            DateTimeMapperUtils timeUtils,
                            VoucherService voucherService,
                            UsersRepository usersRepository,
                            VariantRepository variantRepository, AddressRepository addressRepository, AddressMapper addressMapper, ShopRepository shopRepository, MoneyUtils moneyUtils, OrderRepository orderRepository, VnPayService vnPayService, OrderShopRepository orderShopRepository) {
        this.authUtils = authUtils;
        this.ghnService = ghnService;
        this.shopDistrict = shopDistrict;
        this.shopWard = shopWard;
        this.timeUtils = timeUtils;
        this.voucherService = voucherService;
        this.usersRepository = usersRepository;
        this.variantRepository = variantRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.shopRepository = shopRepository;
        this.moneyUtils = moneyUtils;
        this.orderRepository = orderRepository;
        this.vnPayService = vnPayService;
        this.orderShopRepository = orderShopRepository;
    }

    @Override
    @Transactional
    public ResponseBody customerGetCheckOut(CheckoutRequest req) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            Address address = addressRepository.findById(req.getAddressId())
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));

            Map<String, ShopCheckoutDto> shopCheckout = new HashMap<>();
            BigDecimal allProductTotal = BigDecimal.ZERO;

            //Tinh toan tung item cua tung shop
            for (CheckoutItem item : req.getItems()) {
                ProductVariant variant = variantRepository.findById(item.getVariantId())
                        .orElseThrow(() -> new RequestNotFoundException("ERROR"));
                if (variant.getStockQuantity() - item.getQuantity() < 0) {
                    return new ResponseBody("", ResponseBody.Status.SUCCESS,
                            "HET_HANG", ResponseBody.Code.SUCCESS);
                } else {
                    Product product = variant.getProduct();
                    Shop shop = product.getShopOfProduct();
                    String shopCode = shop.getShopCode();
                    ShopCheckoutDto.ShopItem shopItem = ShopCheckoutDto.ShopItem.builder()
                            .productId(product.getId()).variantId(variant.getId())
                            .title(product.getTitle()).imageUrl(product.getImageList())
                            .variant(variant.getProductVariation() != null ? variant.getProductVariation() : null)
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
                    }
                    allProductTotal = allProductTotal.add(
                            variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                }
            }

            //Tinh toan voucher cua tung shop
            BigDecimal allShopDiscount = BigDecimal.ZERO;
            if (req.getShopDiscounts() != null) {
                for (CheckoutRequest.ShopDiscount discount : req.getShopDiscounts()) {
                    if (!shopCheckout.containsKey(discount.getShopCode())) {
                        throw new RequestNotFoundException("ERROR");
                    } else {
                        ShopCheckoutDto existed = shopCheckout.get(discount.getShopCode());
                        VoucherCheckout voucherCheckout = voucherService
                                .customerCheckoutShopVoucher(discount.getShopCode(),
                                        discount.getVoucherCode(), existed.getProductTotal().doubleValue());

                        existed.setVoucher(voucherCheckout.getShopVoucherDto());
                        existed.setDiscount(BigDecimal.valueOf(voucherCheckout.getDiscount()));
                        existed.setTotalAmount(existed.getProductTotal().subtract(existed.getDiscount()));
                        allShopDiscount = allShopDiscount.add(existed.getDiscount());
                    }
                }
            }
            List<ShopCheckoutDto> shopCheckoutList = new ArrayList<>(shopCheckout.values());

            //Chu y dang dat gia tri mac dinh cho shop
            //Ship - time
            BigDecimal totalFee = BigDecimal.ZERO;
            for (ShopCheckoutDto dto : shopCheckoutList) {
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
            if (req.getDiscountVoucher() != null) {
                VoucherCheckout ecommerceDiscountVoucher = voucherService
                        .customerCheckoutVoucher(req.getDiscountVoucher(), allProductTotal.doubleValue());
                ecommerceDiscount = BigDecimal.valueOf(ecommerceDiscountVoucher.getDiscount());
                sanDiscountVoucher = ecommerceDiscountVoucher.getVoucherDto();
            } else {
                sanDiscountVoucher = null;
            }

            //Vouher cua san (shipping)
            BigDecimal ecommerceShipping = BigDecimal.ZERO;
            VoucherDto sanShippingVoucher;
            if (req.getShippingVoucher() != null) {
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
                    .subtract(allShopDiscount) //Voucher shop
                    .subtract(ecommerceDiscount).subtract(ecommerceShipping) //Voucher san
                    .add(totalFee); //Tien ship

            CheckoutDto checkoutDto = CheckoutDto.builder()
                    .fullName(users.getFirstName() + " " + users.getLastName())
                    .addressDto(addressMapper.entityToAddressDto(address))
                    .itemsCount(req.getItems().size())
                    .productTotal(allProductTotal)
                    .totalFee(totalFee)
                    .shopDiscount(allShopDiscount)
                    .ecommerceDiscount(ecommerceDiscount)
                    .ecommerceShipping(ecommerceShipping)
                    .discountVoucher(sanDiscountVoucher)
                    .shippingVoucher(sanShippingVoucher)
                    .totalAmount(totalPrice)
                    .shopBill(shopCheckoutList)
                    .build();

            return new ResponseBody(checkoutDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Checkout error! " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody customerSubmitCheckOut(CheckoutRequest req) {
        try {
            Users users = usersRepository.findById(authUtils.getUserFromAuthentication().getId())
                    .orElseThrow(() -> new UnauthorizedException("Error"));
            ResponseBody responseBody = customerGetCheckOut(req);
            CheckoutDto checkoutDto = (CheckoutDto) responseBody.getData();
            AddressDto address = checkoutDto.getAddressDto();
            List<ShopCheckoutDto> shopBill = checkoutDto.getShopBill();

            //Tao order
            Orders orders = new Orders();
            orders.setClientName(address.getFullName());
            orders.setClientTelephone(address.getPhoneNumber());
            orders.setClientAddress(address.getHouseName() + ", " + address.getWardName()
                    + ", " + address.getDistrictName() + ", " + address.getProvinceName());
            orders.setDeliveryMethod(req.getShippingMethod().name());
            orders.setPaymentMethod(req.getPaymentMethod().name());
            orders.setUsersOfOrder(users);

            orders.setTotalDiscount(checkoutDto.getEcommerceDiscount());
            orders.setTotalShipping(checkoutDto.getEcommerceShipping());
            orders.setTotalFee(checkoutDto.getTotalFee());
            orders.setTotalAmount(checkoutDto.getTotalAmount());

            if(req.getPaymentMethod()== OrderPayment.Method.VNPAY ||
                    req.getPaymentMethod()== OrderPayment.Method.MOMO) {
                orders.setIsPayment(false);
                orders.setStatus(Orders.Status.CHO_THANH_TOAN);
            } else if(req.getPaymentMethod()== OrderPayment.Method.THANH_TOAN_KHI_GIAO_HANG){
                orders.setIsPayment(false);
                orders.setStatus(Orders.Status.DANG_XU_LY);
            }

            //Order-payment
            OrderPayment orderPayment = new OrderPayment();
            orderPayment.setMethod(req.getPaymentMethod());
            orderPayment.setStatus(OrderPayment.Status.PENDING);
            orderPayment.setExpiryDate(LocalDateTime.now().plusDays(10));
            orderPayment.setStartDate(LocalDateTime.now());
            orderPayment.setAmount(orders.getTotalAmount());
            orderPayment.setTransactionCode(RandomUtils.generateOrderCode("TRC", 1L));
            orderPayment.setOrderOfOrderPayment(orders);
            orders.setListOrderPayment(List.of(orderPayment)); //Chu y

            orders = orderRepository.save(orders);

            //Tao shop order
//            List<OrdersShop> listOrderShop = new ArrayList<>();
            BigDecimal allServiceFee = BigDecimal.ZERO;
            for(ShopCheckoutDto dto : shopBill) {
                Shop shop = shopRepository.findShopByShopCode(dto.getShopCode());
                OrdersShop ordersShop = new OrdersShop();
                ordersShop.setOrdersOfOrdersShop(orders);
                ordersShop.setShopOfOrdersShop(shop);
                ordersShop.setOrderStatus(OrdersShop.Status.CHO_XAC_NHAN);

                //Phuong thuc thanh toan - giao hang
                ordersShop.setDeliveryMethod(orders.getDeliveryMethod());
                ordersShop.setPaymentMethod(orders.getPaymentMethod());
                ordersShop.setIsPayment(orders.getIsPayment());

                ordersShop.setTotalProduct(dto.getProductTotal());
                ordersShop.setShopShippingFee(dto.getFee());
                ordersShop.setShopDiscount(dto.getDiscount());

                ordersShop.setServiceFee(moneyUtils.calculatorServiceFee(dto.getProductTotal()));

                BigDecimal allEcommerceDiscount = checkoutDto.getEcommerceShipping().add(checkoutDto.getEcommerceDiscount());
                BigDecimal allEcommerceCost = checkoutDto.getTotalAmount().subtract(allEcommerceDiscount);
                BigDecimal result = allEcommerceDiscount.multiply(dto.getTotalAmount().divide(allEcommerceCost, MathContext.DECIMAL128));
                BigDecimal roundedResult = result.add(new BigDecimal("0.5")).setScale(0, BigDecimal.ROUND_DOWN);
                ordersShop.setEcommerceTotalAmount(dto.getTotalAmount().subtract(roundedResult));

                ordersShop.setShopTotalAmount(
                        dto.getProductTotal().subtract(dto.getDiscount()).subtract(ordersShop.getServiceFee()));
                allServiceFee = allServiceFee.add(ordersShop.getServiceFee());

                //Tao ordershipping
                OrderShipping shipping = new OrderShipping();
                shipping.setOrdersShopOfShipping(ordersShop);
                shipping.setCarrier("GHN");
                shipping.setMethod(OrderShipping.Method.GHN);
                shipping.setStatus(OrderShipping.Status.PENDING);
                shipping.setShippingFee(dto.getFee());
                shipping.setTrackingCode(1000000L);
                shipping.setShippingDate(null);
                shipping.setDeliveryDate(LocalDateTime.now().plusDays(dto.getLeadDay()));
                //Thong tin khach hang
                shipping.setClientName(address.getFullName());
                shipping.setClientTelephone(address.getPhoneNumber());
                shipping.setClientAddress(address.getHouseName() + ", " + address.getWardName()
                        + ", " + address.getDistrictName() + ", " + address.getProvinceName());
                ordersShop.setOrderShippingOfShopOrder(shipping);

                //Tao OrderItem
                List<OrdersItems> ordersItemsList = new ArrayList<>();
                for(ShopCheckoutDto.ShopItem item : dto.getListItem()){
                    OrdersItems ordersItems = new OrdersItems();
                    ordersItems.setVariantId(item.getVariantId());
                    ordersItems.setProductId(item.getProductId());
                    ordersItems.setProductTitle(item.getTitle());
                    ordersItems.setProductImage(item.getImageUrl());
                    ordersItems.setVariantName(item.getVariant()!=null ? item.getVariant() : "...");
                    ordersItems.setPrice(item.getSellPrice());
                    ordersItems.setQuantity(item.getQuantity().longValue());
                    ordersItems.setDescription("...");
                    ordersItems.setOrdersShopOfOrdersItems(ordersShop);
                    ordersItemsList.add(ordersItems);
                }
                ordersShop.setListOrdersItemsOfOrdersShop(ordersItemsList);

                OrdersHistory ordersHistory = new OrdersHistory();
                ordersHistory.setEvent("Tạo đơn hàng");
                ordersHistory.setDescription("Đơn hàng được tạo vào lúc " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                ordersHistory.setOrdersShopPOfOrderHistory(ordersShop);
                ordersShop.setListOrderHistoryOfOrderShop(List.of(ordersHistory));

                ordersShop = orderShopRepository.save(ordersShop);
            }

            orderShopRepository.updateEcommerceServiceFee(allServiceFee, orders.getId());
            log.info("Create order success!");
            if(req.getPaymentMethod()== OrderPayment.Method.VNPAY){
                return new ResponseBody(
                        vnPayService.createPayment(orders.getTotalAmount().longValue(), orders.getOrderCode()),
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                return new ResponseBody(null,
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Checkout error! " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }
}
