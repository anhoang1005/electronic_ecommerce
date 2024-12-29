package elk_service.elk.order.dtos.response;

import lombok.Data;

@Data
public class PaymentMethodOverview {
    private String paymentMethod;
    private Long count;
}
