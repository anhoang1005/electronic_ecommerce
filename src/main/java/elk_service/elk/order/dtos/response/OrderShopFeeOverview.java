package elk_service.elk.order.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data

public class OrderShopFeeOverview {
    private double totalShippingFree;
    private double totalDiscountFee;
    private double totalServiceFree;
    private double totalAmount;
}
