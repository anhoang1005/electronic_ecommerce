package elk_service.elk.order.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthlyRevenueOverview {
    private BigDecimal totalRevenue; // totalAmount - shopDiscount
    private BigDecimal totalCost; // serviceFee
    private BigDecimal totalProfit; // revenue - profit

    private String revenuePercentageChange;
    private String costPercentageChange;
    private String profitPercentageChange;
}
