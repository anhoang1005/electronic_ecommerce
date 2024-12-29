package elk_service.elk.order.dtos.response;

import lombok.Data;

@Data
public class MonthlyReportProductOverview {
    private Long productId;
    private String title;
    private Double count;
    private Double percentage;
}
