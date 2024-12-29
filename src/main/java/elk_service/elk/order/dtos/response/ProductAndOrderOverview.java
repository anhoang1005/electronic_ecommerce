package elk_service.elk.order.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAndOrderOverview {
    private Long orderPending;
    private Long orderProcessing;
    private Long orderCancelled;
    private Long orderCompleted;
    private Long orderDelivery;

    private Long lockedProduct;
    private Long soldOutProduct;
}
