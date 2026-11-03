package com.example.demoappbanhang;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final List<Order> orderList =
            new ArrayList<>();

    public static void addOrder(Order order) {
        orderList.add(0, order);
    }

    public static List<Order> getOrders() {
        return orderList;
    }

    public static Order getLatestOrder() {

        if (orderList.isEmpty()) {
            return null;
        }

        return orderList.get(0);
    }
}