package com.example.demoappbanhang;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageButton btnOrderDetailBack;
    private Button btnCancelOrder;

    private TextView txtDetailOrderId;
    private TextView txtDetailDate;
    private TextView txtDetailCustomer;
    private TextView txtDetailPhone;
    private TextView txtDetailAddress;
    private TextView txtDetailPayment;
    private TextView txtDetailTotal;

    private TextView txtStatus1;
    private TextView txtStatus2;
    private TextView txtStatus3;
    private TextView txtStatus4;

    private String currentOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);


        btnOrderDetailBack =
                findViewById(R.id.btnOrderDetailBack);

        btnCancelOrder =
                findViewById(R.id.btnCancelOrder);

        txtDetailOrderId =
                findViewById(R.id.txtDetailOrderId);

        txtDetailDate =
                findViewById(R.id.txtDetailDate);

        txtDetailCustomer =
                findViewById(R.id.txtDetailCustomer);

        txtDetailPhone =
                findViewById(R.id.txtDetailPhone);

        txtDetailAddress =
                findViewById(R.id.txtDetailAddress);

        txtDetailPayment =
                findViewById(R.id.txtDetailPayment);

        txtDetailTotal =
                findViewById(R.id.txtDetailTotal);

        txtStatus1 =
                findViewById(R.id.txtStatus1);

        txtStatus2 =
                findViewById(R.id.txtStatus2);

        txtStatus3 =
                findViewById(R.id.txtStatus3);

        txtStatus4 =
                findViewById(R.id.txtStatus4);

        currentOrderId =
                getIntent().getStringExtra("orderId");

        btnOrderDetailBack.setOnClickListener(
                v -> finish()
        );

        loadOrder(currentOrderId);

        btnCancelOrder.setOnClickListener(
                v -> showCancelDialog(currentOrderId)
        );
    }

    private void loadOrder(String orderId) {

        if (orderId == null) {
            return;
        }

        List<Order> orders =
                OrderManager.getOrders();

        Order selectedOrder = null;

        for (Order order : orders) {

            if (order.getOrderId().equals(orderId)) {

                selectedOrder = order;
                break;
            }
        }

        if (selectedOrder == null) {
            return;
        }

        txtDetailOrderId.setText(
                "#" + selectedOrder.getOrderId()
        );

        txtDetailDate.setText(
                selectedOrder.getDate()
        );

        txtDetailCustomer.setText(
                "👤 " + selectedOrder.getCustomerName()
        );

        txtDetailPhone.setText(
                "📞 " + selectedOrder.getPhone()
        );

        txtDetailAddress.setText(
                "📍 " + selectedOrder.getAddress()
        );

        txtDetailPayment.setText(
                "💳 " + selectedOrder.getPaymentMethod()
        );

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        txtDetailTotal.setText(
                formatter.format(
                        selectedOrder.getTotalPrice()
                ) + "đ"
        );

        updateStatus(
                selectedOrder.getStatus()
        );

        updateCancelButton(
                selectedOrder.getStatus()
        );
    }

    private void updateCancelButton(String status) {

        if (status.equals("Chờ xác nhận")) {

            btnCancelOrder.setEnabled(true);
            btnCancelOrder.setVisibility(Button.VISIBLE);

        } else {

            btnCancelOrder.setEnabled(false);
            btnCancelOrder.setVisibility(Button.GONE);
        }
    }

    private void showCancelDialog(String orderId) {

        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn hàng")
                .setMessage(
                        "Bạn có chắc chắn muốn hủy đơn hàng #"
                                + orderId
                                + "?"
                )
                .setNegativeButton(
                        "Không",
                        null
                )
                .setPositiveButton(
                        "Hủy đơn",
                        (dialog, which) -> cancelOrder(orderId)
                )
                .show();
    }

    private void cancelOrder(String orderId) {

        for (Order order : OrderManager.getOrders()) {

            if (order.getOrderId().equals(orderId)) {

                order.setStatus("Đã hủy");

                Toast.makeText(
                        this,
                        "Đã hủy đơn hàng",
                        Toast.LENGTH_SHORT
                ).show();

                loadOrder(orderId);

                return;
            }
        }
    }

    private void updateStatus(String status) {

        txtStatus1.setTextColor(Color.GRAY);
        txtStatus2.setTextColor(Color.GRAY);
        txtStatus3.setTextColor(Color.GRAY);
        txtStatus4.setTextColor(Color.GRAY);

        if (status.equals("Chờ xác nhận")) {

            txtStatus1.setTextColor(
                    Color.rgb(139, 0, 0)
            );

        } else if (status.equals("Đang chuẩn bị")) {

            txtStatus1.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus2.setTextColor(
                    Color.rgb(139, 0, 0)
            );

        } else if (status.equals("Đang giao")) {

            txtStatus1.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus2.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus3.setTextColor(
                    Color.rgb(139, 0, 0)
            );

        } else if (status.equals("Đã giao")) {

            txtStatus1.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus2.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus3.setTextColor(
                    Color.rgb(139, 0, 0)
            );

            txtStatus4.setTextColor(
                    Color.rgb(139, 0, 0)
            );

        } else if (status.equals("Đã hủy")) {

            txtStatus1.setTextColor(Color.RED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentOrderId != null) {
            loadOrder(currentOrderId);
        }
    }
}