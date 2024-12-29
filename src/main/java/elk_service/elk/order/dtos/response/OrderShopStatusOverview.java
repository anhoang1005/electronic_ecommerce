package elk_service.elk.order.dtos.response;

import lombok.Data;

@Data
public class OrderShopStatusOverview {
    private String status;
    private double value;
    private double percentage;
}
