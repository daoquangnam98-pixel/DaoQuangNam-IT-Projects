package com.example.demoappbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;

    private CartAdapter cartAdapter;

    private TextView txtCartCount;
    private TextView txtCartGrandTotal;

    private ImageButton btnCartBack;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        txtCartCount = findViewById(R.id.txtCartCount);
        txtCartGrandTotal =
                findViewById(R.id.txtCartGrandTotal);

        btnCartBack =
                findViewById(R.id.btnCartBack);

        btnCheckout =
                findViewById(R.id.btnCheckout);

        recyclerCart.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadCart();

        btnCartBack.setOnClickListener(
                v -> finish()
        );

        btnCheckout.setOnClickListener(v -> {

            if (CartManager.getCartItems().isEmpty()) {

                Toast.makeText(
                        CartActivity.this,
                        "🛒 Giỏ hàng đang trống",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent = new Intent(
                    CartActivity.this,
                    CheckoutActivity.class
            );

            startActivity(intent);
        });
    }

    private void loadCart() {

        cartAdapter = new CartAdapter(
                CartManager.getCartItems(),
                this::updateCartInfo
        );

        recyclerCart.setAdapter(cartAdapter);

        updateCartInfo();
    }

    private void updateCartInfo() {

        int quantity =
                CartManager.getTotalQuantity();

        long total =
                CartManager.getTotalPrice();

        txtCartCount.setText(
                quantity + " món"
        );

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        txtCartGrandTotal.setText(
                formatter.format(total) + "đ"
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cartAdapter != null) {

            cartAdapter.notifyDataSetChanged();

            updateCartInfo();
        }
    }
}