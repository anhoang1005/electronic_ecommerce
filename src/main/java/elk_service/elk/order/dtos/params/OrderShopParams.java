package elk_service.elk.order.dtos.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderShopParams {
    @NotNull(message = "shop id not null")
    private Long shopId;
    private String startDate;
    private String endDate;
    private String orderShopCode;
    private String orderCode;
    private String status;
    private String deliveryMethod;

    public void setParams(Long shopId, String startDate, String endDate, String orderShopCode, String orderCode, String status, String deliveryMethod) {
        this.shopId = shopId;
        this.startDate = (startDate == null || startDate.isEmpty()) ? LocalDate.now().withDayOfMonth(1).toString() : startDate;
        this.endDate = ( endDate == null || endDate.isEmpty()) ? LocalDate.now().toString() : endDate;
        this.orderShopCode = orderShopCode;
        this.orderCode = orderCode;
        this.status = status;
        this.deliveryMethod = deliveryMethod;
    }

    public String getStartDate(){
        return (startDate == null || startDate.isEmpty()) ? LocalDate.now().withDayOfMonth(1).toString() : startDate;
    }

    public String getEndDate(){
        return (endDate == null || endDate.isEmpty()) ? LocalDate.now().toString() : endDate;
    }
}
