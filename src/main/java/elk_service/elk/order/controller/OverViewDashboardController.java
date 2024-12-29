package elk_service.elk.order.controller;

import elk_service.elk.order.dtos.response.*;
import elk_service.elk.order.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/owner/overview")
public class OverViewDashboardController {
    @Autowired
    private OverviewService overviewService;

    @GetMapping("/by-shop/{id}")
    public ResponseEntity<ProductAndOrderOverview> overview(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getProductAndProductOverview(id));
    }

    @GetMapping("/by-shop/{id}/monthly-report")
    public ResponseEntity<MonthlyReportOrderOverview> monthlyReport(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getMonthlyReportOrderOverview(id));
    }

    @GetMapping("/by-shop/{id}/monthly-revenue-overview")
    public ResponseEntity<MonthlyRevenueOverview> monthlyOverview(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getMonthlyRevenueOverview(id));
    }

    @GetMapping("/by-shop/{id}/monthly-popular-product")
    public ResponseEntity<List<MonthlyProductOverview>> getMonthlyPopularProduct(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getMonthlyProductOverview(id));
    }

    @GetMapping("/by-shop/{id}/payment-overview")
    public ResponseEntity<List<PaymentMethodOverview>> getPaymentOverview(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getPaymentMethodOverviews(id));
    }

    @GetMapping("/by-shop/{id}/monthly-report-product")
    public ResponseEntity<List<MonthlyReportProductOverview>> getMonthlyReportProduct(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getMonthlyReportProductOverviews(id));
    }

    @GetMapping("/by-shop/{id}/order-overview")
    public ResponseEntity<List<OrderShopStatusOverview>> getOrderShopStatusOverview(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getOrderShopStatusOverviews(id));
    }

    @GetMapping("/by-shop/{id}/ordershop-fee-overview")
    public ResponseEntity<OrderShopFeeOverview> getOrderShopFeeOverview(@PathVariable Long id) {
        return ResponseEntity.ok(overviewService.getOrderShopFeeOverview(id));
    }
}
