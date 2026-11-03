package com.example.demoappbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {

    private ImageButton btnCheckoutBack;

    private EditText edtCustomerName;
    private EditText edtPhone;
    private EditText edtAddress;
    private EditText edtNote;

    private RadioGroup paymentGroup;
    private RadioButton radioCash;
    private RadioButton radioBank;

    private TextView txtCheckoutSubtotal;
    private TextView txtShippingFee;
    private TextView txtCheckoutTotal;

    private Button btnPlaceOrder;

    private final long SHIPPING_FEE = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);


        btnCheckoutBack = findViewById(R.id.btnCheckoutBack);

        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);

        paymentGroup = findViewById(R.id.paymentGroup);
        radioCash = findViewById(R.id.radioCash);
        radioBank = findViewById(R.id.radioBank);

        txtCheckoutSubtotal =
                findViewById(R.id.txtCheckoutSubtotal);

        txtShippingFee =
                findViewById(R.id.txtShippingFee);

        txtCheckoutTotal =
                findViewById(R.id.txtCheckoutTotal);

        btnPlaceOrder =
                findViewById(R.id.btnPlaceOrder);


        updateTotal();


        btnCheckoutBack.setOnClickListener(v -> finish());


        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void updateTotal() {

        long subtotal = CartManager.getTotalPrice();

        long total;

        if (subtotal > 0) {
            total = subtotal + SHIPPING_FEE;
        } else {
            total = 0;
        }

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        txtCheckoutSubtotal.setText(
                formatter.format(subtotal) + "đ"
        );

        txtShippingFee.setText(
                formatter.format(SHIPPING_FEE) + "đ"
        );

        txtCheckoutTotal.setText(
                formatter.format(total) + "đ"
        );
    }

    private void placeOrder() {


        if (CartManager.getCartItems().isEmpty()) {

            Toast.makeText(
                    this,
                    "🛒 Giỏ hàng đang trống",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String customerName =
                edtCustomerName
                        .getText()
                        .toString()
                        .trim();

        String phone =
                edtPhone
                        .getText()
                        .toString()
                        .trim();

        String address =
                edtAddress
                        .getText()
                        .toString()
                        .trim();


        if (customerName.isEmpty()) {

            edtCustomerName.setError(
                    "Vui lòng nhập họ và tên"
            );

            edtCustomerName.requestFocus();

            return;
        }

        if (phone.isEmpty()) {

            edtPhone.setError(
                    "Vui lòng nhập số điện thoại"
            );

            edtPhone.requestFocus();

            return;
        }

        if (address.isEmpty()) {

            edtAddress.setError(
                    "Vui lòng nhập địa chỉ giao hàng"
            );

            edtAddress.requestFocus();

            return;
        }


        String paymentMethod;

        if (radioCash.isChecked()) {

            paymentMethod =
                    "Thanh toán khi nhận hàng";

        } else {

            paymentMethod =
                    "Chuyển khoản ngân hàng";
        }


        long subtotal =
                CartManager.getTotalPrice();

        long total =
                subtotal + SHIPPING_FEE;


        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 6)
                        .toUpperCase();

        String orderId =
                "DH" + randomPart;


        String date =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(new Date());


        Order order =
                new Order(
                        orderId,
                        customerName,
                        phone,
                        address,
                        paymentMethod,
                        total,
                        "Chờ xác nhận",
                        date
                );


        OrderManager.addOrder(order);


        CartManager.getCartItems().clear();

        Toast.makeText(
                this,
                "🎉 Đặt hàng thành công!\nMã đơn: " + orderId,
                Toast.LENGTH_LONG
        ).show();


        Intent intent =
                new Intent(
                        CheckoutActivity.this,
                        OrderActivity.class
                );

        intent.putExtra(
                "orderId",
                orderId
        );

        startActivity(intent);

        finish();
    }
}