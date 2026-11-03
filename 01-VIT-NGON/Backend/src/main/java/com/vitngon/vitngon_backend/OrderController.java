
package com.vitngon.vitngon_backend;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(
            OrderRepository orderRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Tạo đơn hàng và lưu vào MySQL
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody CreateOrderRequest request) {

        if (request.username == null
                || request.username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Không xác định được tài khoản đặt hàng");
        }

        User user = userRepository.findByUsername(
                request.username.trim()).orElse(null);

        if (user == null || !user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tài khoản không tồn tại hoặc đã bị khóa");
        }

        if (request.orderId == null
                || request.orderId.trim().isEmpty()
                || request.customerName == null
                || request.customerName.trim().isEmpty()
                || request.phone == null
                || request.phone.trim().isEmpty()
                || request.address == null
                || request.address.trim().isEmpty()
                || request.paymentMethod == null
                || request.paymentMethod.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Vui lòng nhập đủ thông tin giao hàng và thanh toán");
        }

        if (orderRepository.existsByOrderId(request.orderId.trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Mã đơn hàng đã tồn tại. Vui lòng thử lại.");
        }

        if (request.totalPrice <= 0) {
            return ResponseEntity.badRequest()
                    .body("Tổng tiền đơn hàng không hợp lệ");
        }

        Order order = new Order();

        order.setUserId(user.getId());
        order.setOrderId(request.orderId.trim());
        order.setCustomerName(request.customerName.trim());

        order.setCustomerPhone(request.phone.trim());
        order.setPhone(request.phone.trim());

        order.setDeliveryAddress(request.address.trim());
        order.setAddress(request.address.trim());

        order.setNote(request.note);
        order.setPaymentMethod(request.paymentMethod.trim());

        order.setTotalAmount(BigDecimal.valueOf(request.totalPrice));
        order.setTotalPrice(request.totalPrice);

        order.setStatus("Chờ xác nhận");
        order.setPaymentStatus("UNPAID");
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedOrder);
    }

    // Danh sách tất cả đơn hàng, dùng cho giao diện quản trị.
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll(
                Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    // Danh sách đơn hàng của một tài khoản khách.
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(
            @RequestParam String username) {

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Thiếu tên tài khoản");
        }

        User user = userRepository.findByUsername(
                username.trim()).orElse(null);

        if (user == null || !user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tài khoản không tồn tại hoặc đã bị khóa");
        }

        List<Order> orders = orderRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "orderDate"))
                .stream()
                .filter(order ->
                        Objects.equals(order.getUserId(), user.getId()))
                .toList();

        return ResponseEntity.ok(orders);
    }

    // Xem chi tiết một đơn hàng theo mã.
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable String orderId) {

        return orderRepository.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật trạng thái đơn hàng.
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody StatusRequest request) {

        if (request.getStatus() == null
                || request.getStatus().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Trạng thái không được để trống");
        }

        return orderRepository.findByOrderId(orderId)
                .map(order -> {
                    order.setStatus(request.getStatus().trim());
                    return ResponseEntity.ok(
                            orderRepository.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public static class CreateOrderRequest {
        public String username;
        public String orderId;
        public String customerName;
        public String phone;
        public String address;
        public String note;
        public String paymentMethod;
        public long totalPrice;

        public CreateOrderRequest() {
        }
    }

    public static class StatusRequest {
        private String status;

        public StatusRequest() {
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
