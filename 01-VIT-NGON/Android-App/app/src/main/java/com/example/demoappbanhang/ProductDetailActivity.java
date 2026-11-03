package com.example.demoappbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnFavorite;

    private ImageView imgProductDetail;

    private TextView txtDetailName;
    private TextView txtDetailRating;
    private TextView txtDetailPrice;
    private TextView txtDetailDescription;
    private TextView txtQuantity;

    private Button btnMinus;
    private Button btnPlus;
    private Button btnAddToCart;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail);

        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);

        imgProductDetail = findViewById(R.id.imgProductDetail);

        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailRating = findViewById(R.id.txtDetailRating);
        txtDetailPrice = findViewById(R.id.txtDetailPrice);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        txtQuantity = findViewById(R.id.txtQuantity);

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Nhận dữ liệu sản phẩm
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");

        long price = getIntent().getLongExtra("price", 0);
        double rating = getIntent().getDoubleExtra("rating", 0);
        int sold = getIntent().getIntExtra("sold", 0);

        // Hiển thị thông tin sản phẩm
        txtDetailName.setText(name);

        txtDetailDescription.setText(description);

        txtDetailRating.setText(
                "⭐ " + rating + "  •  " + sold + " đã bán"
        );

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        txtDetailPrice.setText(
                formatter.format(price) + "đ"
        );

        // Hiển thị ảnh sản phẩm
        if (image != null && !image.isEmpty()) {

            int imageResource = getResources().getIdentifier(
                    image,
                    "drawable",
                    getPackageName()
            );

            if (imageResource != 0) {

                imgProductDetail.setImageResource(imageResource);

            } else {

                imgProductDetail.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } else {

            imgProductDetail.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Giảm số lượng
        btnMinus.setOnClickListener(v -> {

            if (quantity > 1) {

                quantity--;

                txtQuantity.setText(
                        String.valueOf(quantity)
                );
            }
        });

        // Tăng số lượng
        btnPlus.setOnClickListener(v -> {

            if (quantity < 99) {

                quantity++;

                txtQuantity.setText(
                        String.valueOf(quantity)
                );
            }
        });

        // Yêu thích
        btnFavorite.setOnClickListener(v -> {

            Toast.makeText(
                    ProductDetailActivity.this,
                    "❤️ Đã thêm vào yêu thích",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {

            CartManager.addToCart(
                    name,
                    description,
                    price,
                    quantity,
                    image
            );

            Toast.makeText(
                    ProductDetailActivity.this,
                    "🛒 Đã thêm "
                            + quantity
                            + " sản phẩm vào giỏ hàng",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    ProductDetailActivity.this,
                    CartActivity.class
            );

            startActivity(intent);
        });
    }
}