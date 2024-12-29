package elk_service.elk.order.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MonthlyReportOrderOverview {
    private List<String> months;
    private List<Long> completedOrders;
    private List<Long> returnOrders;
}
