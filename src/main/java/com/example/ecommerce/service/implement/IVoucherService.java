package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.ShopVoucher;
import com.example.ecommerce.entities.Voucher;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.voucher.VoucherMapper;
import com.example.ecommerce.models.voucher.CreateVoucherRequest;
import com.example.ecommerce.models.voucher.VoucherCheckout;
import com.example.ecommerce.models.voucher.VoucherDto;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.ShopVoucherRepository;
import com.example.ecommerce.repository.VoucherRepository;
import com.example.ecommerce.service.VoucherService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IVoucherService implements VoucherService {
    private final ShopVoucherRepository shopVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final AuthenticationUtils authUtils;
    private final ShopRepository shopRepository;


    @Override
    @Transactional
    public ResponseBody adminCreateVoucher(CreateVoucherRequest req) {
       try{
           Voucher existed = voucherRepository.findVoucherByVoucherCode(req.getVoucherCode()).orElse(null);
           if(existed!=null){
               return new ResponseBody("", ResponseBody.Status.SUCCESS,
                       "VOUCHER_CODE_EXISTED", ResponseBody.Code.SUCCESS);
           }
           Voucher voucher = new Voucher();
           voucher.setVoucherCode(req.getVoucherCode());
           voucher.setTitle(voucher.getTitle());
           voucher.setDescription(voucher.getDescription());
           voucher.setVoucherCount(voucher.getVoucherCount());
           voucher.setRemainingCount(0);
           voucher.setStatus(req.getStatus());
           if(req.getType()== Voucher.Type.DISCOUNT){
               voucher.setType(Voucher.Type.DISCOUNT);
               voucher.setDiscountType(voucher.getDiscountType());
               voucher.setDiscountValue(voucher.getDiscountValue());
               voucher.setMaxDiscountValue(voucher.getMaxDiscountValue());
           } else {
               voucher.setType(Voucher.Type.SHIPPING);
               voucher.setDiscountType(Voucher.DiscountType.AMOUNT);
               voucher.setShippingDiscount(voucher.getShippingDiscount());
               voucher.setMaxShippingDiscount(voucher.getMaxShippingDiscount());
           }
           voucher.setMinTotalOrder(voucher.getMinTotalOrder());
           voucher = voucherRepository.save(voucher);
           return new ResponseBody(voucherMapper.voucherToVoucherDto(voucher), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
       } catch (Exception e){
           log.error("user create voucher failed! Error: " + e.getMessage());
           throw new RequestNotFoundException(e.getMessage());
       }
    }

    @Override
    @Transactional
    public ResponseBody shopCreateVoucher(CreateVoucherRequest req) {
        try{
            ShopVoucher existed = shopVoucherRepository.findShopVoucherByVoucherCode(req.getVoucherCode()).orElse(null);
            if(existed!=null){
                return new ResponseBody("", ResponseBody.Status.SUCCESS,
                        "VOUCHER_CODE_EXISTED", ResponseBody.Code.SUCCESS);
            }
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            Shop shop = shopRepository.findShopByShopCode(shopCode);
            ShopVoucher voucher = new ShopVoucher();
            voucher.setShopOfShopVoucher(shop);
            voucher.setVoucherCode(req.getVoucherCode());
            voucher.setTitle(voucher.getTitle());
            voucher.setDescription(voucher.getDescription());
            voucher.setVoucherCount(voucher.getVoucherCount());
            voucher.setRemainingCount(0);
            voucher.setStatus(req.getStatus());
            voucher.setDiscountType(voucher.getDiscountType());
            voucher.setDiscountValue(voucher.getDiscountValue());
            voucher.setMaxDiscountValue(voucher.getMaxDiscountValue());
            voucher.setMinTotalOrder(voucher.getMinTotalOrder());
            voucher = shopVoucherRepository.save(voucher);
            return new ResponseBody(voucherMapper.shopVoucherToVoucherDto(voucher), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("user create voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody userUpdateVoucher(CreateVoucherRequest req, boolean isAdmin) {
        try{
            if(isAdmin){
                Voucher voucher = voucherRepository.findVoucherByVoucherCode(req.getVoucherCode())
                        .orElseThrow(()-> new RequestNotFoundException("voucher_not_existed"));
                voucher.setTitle(voucher.getTitle());
                voucher.setDescription(voucher.getDescription());
                voucher.setVoucherCount(voucher.getVoucherCount());
                if(req.getType()== Voucher.Type.DISCOUNT){
                    voucher.setType(Voucher.Type.DISCOUNT);
                    voucher.setDiscountType(voucher.getDiscountType());
                    voucher.setDiscountValue(voucher.getDiscountValue());
                    voucher.setMaxDiscountValue(voucher.getMaxDiscountValue());
                } else {
                    voucher.setType(Voucher.Type.SHIPPING);
                    voucher.setDiscountType(Voucher.DiscountType.AMOUNT);
                    voucher.setShippingDiscount(voucher.getShippingDiscount());
                    voucher.setMaxShippingDiscount(voucher.getMaxShippingDiscount());
                }
                voucher.setMinTotalOrder(voucher.getMinTotalOrder());
                voucherRepository.save(voucher);
            } else {
                ShopVoucher voucher = shopVoucherRepository.findShopVoucherByVoucherCode(req.getVoucherCode())
                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
                voucher.setTitle(voucher.getTitle());
                voucher.setDescription(voucher.getDescription());
                voucher.setVoucherCount(voucher.getVoucherCount());
                voucher.setDiscountType(voucher.getDiscountType());
                voucher.setDiscountValue(voucher.getDiscountValue());
                voucher.setMaxDiscountValue(voucher.getMaxDiscountValue());
                voucher.setMinTotalOrder(voucher.getMinTotalOrder());
                shopVoucherRepository.save(voucher);
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("update create voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody userChangeStatusVoucher(String voucherCode, Voucher.Status status, boolean isAdmin) {
        try{
            if(isAdmin){
                Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode)
                        .orElseThrow(()-> new RequestNotFoundException("VOUCHER_NOT_EXIT"));
                voucher.setStatus(status);
            } else {
                ShopVoucher voucher = shopVoucherRepository.findShopVoucherByVoucherCode(voucherCode)
                        .orElseThrow(()-> new RequestNotFoundException("VOUCHER_NOT_EXIT"));
                voucher.setStatus(status);
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("change status voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody customerGetTheirListVoucher(String voucherCode, Voucher.Type type, int pageNumber, int pageSize) {
        try{
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("startDate")));
            LocalDateTime currentDateTime = LocalDateTime.now();
            Page<Voucher> page = voucherRepository.customerGetListVoucher(voucherCode, type, Voucher.Status.ENABLE, currentDateTime, pageable);
            List<VoucherDto> dtoList = page.stream()
                    .map(voucherMapper::voucherToVoucherDto).collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .totalPage(page.getTotalPages())
                    .totalAmount(page.getTotalElements())
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .data(dtoList)
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("get customer voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public VoucherCheckout customerCheckoutVoucher(String voucherCode, Double totalAmount) {
        try{
            Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            LocalDateTime now = LocalDateTime.now();
            if(voucher.getStatus()== Voucher.Status.ENABLE){
                if(voucher.getStartedAt().isAfter(now) || voucher.getExpiredAt().isBefore(now)){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "OUT_OF_TIME");
                }
                if(voucher.getMinTotalOrder() < totalAmount){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "MIN_TOTAL_ORDER");
                }
                if(voucher.getRemainingCount()>voucher.getVoucherCount()){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "VOUCHER_SOLD_OUT");
                }
                double discount;
                if(voucher.getDiscountType()== Voucher.DiscountType.AMOUNT){
                    discount = voucher.getDiscountValue();
                } else {
                    double value = voucher.getDiscountValue()*totalAmount/100;
                    if(voucher.getMaxDiscountValue()!=null){
                        discount = value>voucher.getMaxDiscountValue() ? voucher.getMaxDiscountValue() : value;
                    } else {
                        discount = value;
                    }
                }
                return new VoucherCheckout(null,
                        voucherMapper.voucherToVoucherDto(voucher), discount, "DONE");
            } else {
                return new VoucherCheckout(null,
                        null, 0.0, "VOUCHER_NOT_ENABLE");
            }
        } catch (Exception e){
            log.error("checkout customer voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public VoucherCheckout customerCheckoutShippingVoucher(String voucherCode, Double totalAmount) {
        try{
            Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            LocalDateTime now = LocalDateTime.now();
            if(voucher.getStatus()== Voucher.Status.ENABLE){
                if(voucher.getStartedAt().isAfter(now) || voucher.getExpiredAt().isBefore(now)){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "OUT_OF_TIME");
                }
                if(voucher.getMinTotalOrder() < totalAmount){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "MIN_TOTAL_ORDER");
                }
                if(voucher.getRemainingCount()>voucher.getVoucherCount()){
                    return new VoucherCheckout(null,
                            voucherMapper.voucherToVoucherDto(voucher), 0.0, "VOUCHER_SOLD_OUT");
                }
                return new VoucherCheckout(null,
                        voucherMapper.voucherToVoucherDto(voucher), voucher.getShippingDiscount(), "DONE");
            } else {
                return new VoucherCheckout(null,
                        null, 0.0, "VOUCHER_NOT_ENABLE");
            }
        } catch (Exception e){
            log.error("checkout customer voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public VoucherCheckout customerCheckoutShopVoucher(String voucherCode, Double shopTotal) {
        try{
            ShopVoucher voucher = shopVoucherRepository.findShopVoucherByVoucherCode(voucherCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            LocalDateTime now = LocalDateTime.now();
            if(voucher.getStatus()== Voucher.Status.ENABLE){
                if(voucher.getStartedAt().isAfter(now) || voucher.getExpiredAt().isBefore(now)){
                    return new VoucherCheckout(voucherMapper.shopVoucherToVoucherDto(voucher),
                            null, 0.0, "OUT_OF_TIME");
                }
                if(voucher.getMinTotalOrder() < shopTotal){
                    return new VoucherCheckout(voucherMapper.shopVoucherToVoucherDto(voucher),
                            null, 0.0, "MIN_TOTAL_ORDER");
                }
                if(voucher.getRemainingCount() > voucher.getVoucherCount()){
                    return new VoucherCheckout(voucherMapper.shopVoucherToVoucherDto(voucher),
                            null, 0.0, "VOUCHER_SOLD_OUT");
                }
                double discount;
                if(voucher.getDiscountType()== Voucher.DiscountType.AMOUNT){
                    discount = voucher.getDiscountValue();
                } else {
                    double value = voucher.getDiscountValue()*shopTotal/100;
                    if(voucher.getMaxDiscountValue()!=null){
                        discount = value>voucher.getMaxDiscountValue() ? voucher.getMaxDiscountValue() : value;
                    } else {
                        discount = value;
                    }
                }
                return new VoucherCheckout(voucherMapper.shopVoucherToVoucherDto(voucher),
                        null, discount, "DONE");
            } else {
                return new VoucherCheckout(null,
                        null, 0.0, "VOUCHER_NOT_ENABLE");
            }
        }catch (Exception e){
            log.error("checkout customer voucher failed! Error: " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public VoucherDto customerUseVoucher(String voucherCode) {
        return null;
    }

    @Override
    public ResponseBody shopVoucherDashboard() {
        return null;
    }

    @Override
    public ResponseBody adminVoucherDashboard() {
        return null;
    }
}
