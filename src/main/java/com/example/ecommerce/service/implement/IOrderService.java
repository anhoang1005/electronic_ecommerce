package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.order.OrderMapper;
import com.example.ecommerce.models.order.OrderCustomerFilter;
import com.example.ecommerce.models.order.OrderFilter;
import com.example.ecommerce.models.order.OrderShopFilter;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IOrderService implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderShopRepository orderShopRepository;
    private final DateTimeMapperUtils timeUtils;
    private final OrderMapper orderMapper;
    private final AuthenticationUtils authUtils;
    private final OrderHistoryRepository historyRepository;
    private final OrderShippingRepository shippingRepository;
    private final OrderPaymentRepository paymentRepository;

    @Override
    @Transactional
    public ResponseBody shopUpdateOrder(String orderCode, OrdersShop.Status status) {
        try{
            OrdersShop ordersShop = orderShopRepository.findByOrderShopCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order code not existed!"));
            if(status!=ordersShop.getOrderStatus()){
                ordersShop.setOrderStatus(status);
                ordersShop = orderShopRepository.save(ordersShop);

                OrdersHistory history = new OrdersHistory();
                history.setOrdersShopPOfOrderHistory(ordersShop);
                if (status== OrdersShop.Status.CHO_XAC_NHAN){
                    history.setEvent("Chờ xác nhận xác nhận");
                    history.setDescription("Đơn hàng đang chờ được xác nhận xác nhận vào " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                } else if(status== OrdersShop.Status.DANG_LAY_HANG){
                    history.setEvent("Đã xác nhận");
                    history.setDescription("Đơn hàng đã được xác nhận vào " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                } else if(status== OrdersShop.Status.DANG_GIAO_HANG){
                    history.setEvent("Đã lấy hàng");
                    history.setDescription("Đơn hàng đã được được bàn giao cho đơn vị vận chuyển vào. " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                } else if(status== OrdersShop.Status.GH_THANH_CONG){
                    history.setEvent("Đã giao hàng");
                    history.setDescription("Đơn hàng đã được giao đếm người nhận vào " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                } else if(status== OrdersShop.Status.DA_HUY){
                    history.setEvent("Đã lấy hàng");
                    history.setDescription("Đơn hàng đã bị hủy vào " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                }
                historyRepository.save(history);
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Update order shop error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Update error!");
        }
    }

    @Override
    public ResponseBody shopGetDashboard(OrderShopFilter filter) {
        try{
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            LocalDateTime startDate = null, endDate = null;
            if(filter.getStartDate()!=null && filter.getEndDate()!=null){
                startDate = timeUtils.htmlValueToLocalDateTime(filter.getStartDate());
                endDate = timeUtils.htmlValueToLocalDateTime(filter.getEndDate());
            }
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(filter.getPageNumber()-1, filter.getPageSize(), sort);
            Page<OrdersShop> page = orderShopRepository.shopGetDashboard(
                    shopCode,
                    filter.getOrderCode(),
                    filter.getOrderStatus(),
                    filter.getUserFullName(),
                    filter.getPhoneNumber(),
                    filter.getAddress(),
                    startDate,
                    endDate,
                    pageable);
            PageData pageData = PageData.builder()
                    .data(page.stream().map(orderMapper::entityToOrderShopDto).collect(Collectors.toList()))
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .pageNumber(page.getNumber()+1)
                    .pageSize(page.getSize())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Shop get order error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    public ResponseBody shopGetOrderDetail(String orderCode) {
        try{
            String shopCode = authUtils.getUserFromAuthentication().getShopCode();
            OrdersShop ordersShop = orderShopRepository.findByOrderShopCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order not existed!"));
            if(!ordersShop.getShopOfOrdersShop().getShopCode().equals(shopCode)){
                throw new UnauthorizedException("ERROR");
            }
            return new ResponseBody(orderMapper.entityToOrderShopDetailDto(ordersShop),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Shop get order detail error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    public ResponseBody customerGetTheirOrder(OrderCustomerFilter filter) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(filter.getPageNumber()-1, filter.getPageSize(), sort);
            Page<OrdersShop> page = orderShopRepository.customerGetDashBoard(
                    userId,
                    filter.getOrderCode(),
                    filter.getStatus(),
                    pageable
            );
            PageData pageData = PageData.builder()
                    .data(page.stream().map(orderMapper::entityToOrderShopDto).collect(Collectors.toList()))
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Customer get order error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerCancelOrder(String orderCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            OrdersShop ordersShop = orderShopRepository.findByOrderShopCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order not existed!"));
            Orders orders = ordersShop.getOrdersOfOrdersShop();
            if(!orders.getUsersOfOrder().getId().equals(userId)){
                throw new UnauthorizedException("ERROR");
            } else {
                if(ordersShop.getOrderStatus()== OrdersShop.Status.CHO_XAC_NHAN
                        || ordersShop.getOrderStatus()== OrdersShop.Status.DANG_LAY_HANG){
                    ordersShop.setOrderStatus(OrdersShop.Status.DA_HUY);
                    ordersShop = orderShopRepository.save(ordersShop);

                    OrderShipping shipping = ordersShop.getOrderShippingOfShopOrder();
                    shipping.setStatus(OrderShipping.Status.RETURNED);
                    shippingRepository.save(shipping);

                    OrdersHistory history = new OrdersHistory();
                    history.setOrdersShopPOfOrderHistory(ordersShop);
                    history.setEvent("Hủy đơn hàng");
                    history.setDescription("Bạn đã hủy đơn hàng vào lúc " + timeUtils.localDateTimeToString(LocalDateTime.now()));
                    historyRepository.save(history);

                    orders.setEcommerceServiceFee(orders.getEcommerceServiceFee().subtract(ordersShop.getServiceFee()));
                    orders.setTotalAmount(orders.getTotalAmount().subtract(ordersShop.getEcommerceTotalAmount()));
                    orders.setTotalFee(orders.getTotalFee().subtract(ordersShop.getShopShippingFee()));
                    orders = orderRepository.save(orders);

                    OrderPayment payment = orders.getListOrderPayment().get(0);
                    payment.setAmount(payment.getAmount().subtract(ordersShop.getEcommerceTotalAmount()));
                    paymentRepository.save(payment);
                }
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Customer cancel order error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    public ResponseBody customerGetOrderDetail(String orderCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            OrdersShop ordersShop = orderShopRepository.findByOrderShopCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order not existed!"));
            if(!ordersShop.getOrdersOfOrdersShop().getUsersOfOrder().getId().equals(userId)){
                throw new UnauthorizedException("ERROR");
            }
            return new ResponseBody(orderMapper.entityToOrderShopDetailDto(ordersShop),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            e.printStackTrace();
            log.error("Shop get order detail error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    public ResponseBody adminGetDashBoard(OrderFilter filter) {
        try {
            LocalDateTime startDate = null, endDate = null;
            if(filter.getStartDate()!=null && filter.getEndDate()!=null){
                startDate = timeUtils.htmlValueToLocalDateTime(filter.getStartDate());
                endDate = timeUtils.htmlValueToLocalDateTime(filter.getEndDate());
            }
            String paymentMethod = filter.getPaymentMethod()!=null ? filter.getPaymentMethod().name() : null;
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), sort);
            Page<Orders> page = orderRepository.adminGetDashboard(
                    filter.getOrderCode(),
                    filter.getStatus(),
                    paymentMethod,
                    filter.getIsPayment(),
                    startDate,
                    endDate,
                    pageable);
            PageData pageData = PageData.builder()
                    .data(page.stream().map(orderMapper::entityToOrderDto).collect(Collectors.toList()))
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .pageNumber(page.getNumber()+1)
                    .pageSize(page.getSize())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Admin get order error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

    @Override
    @Transactional
    public ResponseBody adminUpdateOrder(String orderCode, Orders.Status status) {
        try{
            Orders orders = orderRepository.findByOrderCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order code not existed!"));
            orders.setStatus(status);
            orderRepository.save(orders);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Admin update order error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Update order error!");
        }
    }

    @Override
    public ResponseBody adminGetOrderDetail(String orderCode) {
        try{
            Orders orders = orderRepository.findByOrderCode(orderCode)
                    .orElseThrow(()-> new RequestNotFoundException("order not existed!"));
            return new ResponseBody(orderMapper.entityToOrderDetailDto(orders),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Shop get order detail error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Get order error!");
        }
    }

}
