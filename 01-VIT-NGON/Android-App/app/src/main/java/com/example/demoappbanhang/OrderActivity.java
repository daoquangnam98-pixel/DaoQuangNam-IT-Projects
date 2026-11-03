package com.example.demoappbanhang;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private LinearLayout txtEmptyOrder;
    private ImageButton btnOrderBack;

    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        txtEmptyOrder = findViewById(R.id.txtEmptyOrder);
        btnOrderBack = findViewById(R.id.btnOrderBack);

        recyclerOrders.setLayoutManager(
                new LinearLayoutManager(this)
        );

        orderAdapter = new OrderAdapter(
                OrderManager.getOrders()
        );

        recyclerOrders.setAdapter(orderAdapter);

        btnOrderBack.setOnClickListener(v -> finish());

        updateEmptyState();
    }

    private void updateEmptyState() {

        if (OrderManager.getOrders().isEmpty()) {

            recyclerOrders.setVisibility(View.GONE);
            txtEmptyOrder.setVisibility(View.VISIBLE);

        } else {

            recyclerOrders.setVisibility(View.VISIBLE);
            txtEmptyOrder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (orderAdapter != null) {

            orderAdapter.notifyDataSetChanged();

            updateEmptyState();
        }
    }
}