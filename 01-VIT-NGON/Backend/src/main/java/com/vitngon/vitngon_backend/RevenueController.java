
package com.vitngon.vitngon_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/revenue")
public class RevenueController {

    private final OrderRepository orderRepository;

    public RevenueController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public Map<String, Object> getRevenue() {
        List<Order> orders = orderRepository.findAll();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal todayRevenue = BigDecimal.ZERO;
        long completedOrders = 0;
        long paidOrders = 0;
        LocalDate today = LocalDate.now();

        for (Order order : orders) {
            boolean completed = isCompleted(order.getStatus());
            boolean paid = isPaid(order.getPaymentStatus());

            if (completed) {
                completedOrders++;
            }

            if (paid) {
                paidOrders++;
            }

            // Chi tinh doanh thu khi don da hoan thanh
            // va da thanh toan.
            if (completed && paid) {
                BigDecimal amount = order.getTotalAmount();

                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }

                totalRevenue = totalRevenue.add(amount);

                LocalDateTime orderTime = order.getOrderDate();

                if (orderTime == null && order.getCreatedAt() != null) {
                    orderTime = order.getCreatedAt().toLocalDateTime();
                }

                if (orderTime != null
                        && orderTime.toLocalDate().equals(today)) {
                    todayRevenue = todayRevenue.add(amount);
                }
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalOrders", orders.size());
        result.put("completedOrders", completedOrders);
        result.put("paidOrders", paidOrders);
        result.put("totalRevenue", totalRevenue);
        result.put("todayRevenue", todayRevenue);
        result.put("date", today.toString());

        return result;
    }

    private boolean isCompleted(String status) {
        if (status == null) {
            return false;
        }

        String normalized = normalize(status);

        return normalized.contains("hoan thanh")
                || normalized.contains("da giao")
                || normalized.equals("completed")
                || normalized.equals("delivered");
    }

    private boolean isPaid(String paymentStatus) {
        if (paymentStatus == null) {
            return false;
        }

        String normalized = normalize(paymentStatus);

        return normalized.equals("paid")
                || normalized.equals("da thanh toan")
                || normalized.equals("thanh toan thanh cong");
    }

    private String normalize(String value) {
        String result = Normalizer.normalize(
                value.trim().toLowerCase(),
                Normalizer.Form.NFD
        );

        return result
                .replaceAll("\\p{M}", "")
                .replace('đ', 'd')
                .replaceAll("\\s+", " ");
    }
}
