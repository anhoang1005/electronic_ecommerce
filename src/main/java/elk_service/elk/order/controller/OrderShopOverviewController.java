package elk_service.elk.order.controller;

import elk_service.elk.order.dtos.params.OrderShopParams;
import elk_service.elk.order.dtos.response.OrderShopResponse;
import elk_service.elk.order.entites.OrderShopDocument;
import elk_service.elk.order.service.OrderShopOverviewService;
import elk_service.elk.utils.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner/order-overview")
public class OrderShopOverviewController {
    @Autowired
    private OrderShopOverviewService orderShopOverviewService;
    @GetMapping("/by-shop")
    public ResponseEntity<ResponsePage<OrderShopDocument,OrderShopResponse>> filter(
           OrderShopParams params,Pageable pageable
    ){
       return new ResponseEntity<>(new ResponsePage<>(orderShopOverviewService.filter(params,pageable), OrderShopResponse.class), HttpStatus.OK);
    }
}
