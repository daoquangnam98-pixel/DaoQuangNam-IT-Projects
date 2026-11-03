package com.example.demoappbanhang;

public class Order {

    private String orderId;
    private String customerName;
    private String phone;
    private String address;
    private String paymentMethod;
    private long totalPrice;
    private String status;
    private String date;

    public Order(
            String orderId,
            String customerName,
            String phone,
            String address,
            String paymentMethod,
            long totalPrice,
            String status,
            String date) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.status = status;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}