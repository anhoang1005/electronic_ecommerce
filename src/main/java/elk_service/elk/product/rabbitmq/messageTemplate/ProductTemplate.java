package elk_service.elk.product.rabbitmq.messageTemplate;

import elk_service.elk.product.entites.ProductDocument;
import elk_service.elk.product.rabbitmq.state.ProductElkState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductTemplate {
    private ProductDocument productDocument;
    private Long productId;
    private ProductElkState productState;
}