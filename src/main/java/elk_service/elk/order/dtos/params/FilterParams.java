package elk_service.elk.order.dtos.params;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterParams {
    private String startDate;
    private String endDate;
    private Long shopId;

}
