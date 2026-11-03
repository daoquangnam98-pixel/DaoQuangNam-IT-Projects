package com.example.demoappbanhang;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<CartItem> cartItems = new ArrayList<>();

    public static void addToCart(
            String name,
            String description,
            long price,
            int quantity,
            String image) {

        for (CartItem item : cartItems) {

            if (item.getName().equals(name)) {

                item.setQuantity(
                        item.getQuantity() + quantity
                );

                return;
            }
        }

        cartItems.add(
                new CartItem(
                        name,
                        description,
                        price,
                        quantity,
                        image
                )
        );
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void removeItem(int position) {

        if (position >= 0 &&
                position < cartItems.size()) {

            cartItems.remove(position);
        }
    }

    public static long getTotalPrice() {

        long total = 0;

        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }

        return total;
    }

    public static int getTotalQuantity() {

        int total = 0;

        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }

        return total;
    }
}