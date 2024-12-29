package elk_service.elk.order.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyProductOverview {
    private Long id;
    private String title;
    private String url;
    private BigDecimal price;

    private Long quantity;
    private Double percentage;
}
