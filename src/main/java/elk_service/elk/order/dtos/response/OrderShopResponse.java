package elk_service.elk.order.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class OrderShopResponse {
    private String id;
    private String orderCode;
    private String orderShopCode;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private Date updatedAt;
}
