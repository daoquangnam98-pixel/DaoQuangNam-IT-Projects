package com.example.demoappbanhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean isLoggedIn =
                preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {

            Intent intent = new Intent(
                    MainActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        recyclerProducts = findViewById(R.id.recyclerProducts);

        productList = new ArrayList<>();

        productList.add(new Product(
                1,
                "Vịt quay nguyên con",
                "Vịt quay da giòn, thịt mềm mọng nước",
                89000,
                "Vịt quay",
                "vit_quay_nguyen_con",
                4.9,
                128
        ));

        productList.add(new Product(
                2,
                "Vịt nướng mật ong",
                "Vịt nướng thơm lừng với sốt mật ong",
                99000,
                "Vịt nướng",
                "vit_nuong_mat_ong",
                4.8,
                96
        ));

        productList.add(new Product(
                3,
                "Vịt quay nửa con",
                "Phù hợp cho 2 - 3 người ăn",
                49000,
                "Vịt quay",
                "vit_quay_nua_con",
                4.8,
                85
        ));

        productList.add(new Product(
                4,
                "Combo Vịt Gia Đình",
                "Vịt quay + bánh mì + rau + nước",
                159000,
                "Combo",
                "combo_gia_dinh",
                5.0,
                76
        ));

        productList.add(new Product(
                5,
                "Vịt nướng ngũ vị",
                "Công thức ngũ vị đặc biệt của quán",
                109000,
                "Vịt nướng",
                "vit_nuong_ngu_vi",
                4.9,
                64
        ));

        productList.add(new Product(
                6,
                "Combo 2 người",
                "Vịt + bánh mì + đồ ăn kèm",
                129000,
                "Combo",
                "combo_2_nguoi",
                4.7,
                51
        ));

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2);

        recyclerProducts.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(productList);

        recyclerProducts.setAdapter(productAdapter);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            }

            if (id == R.id.nav_orders) {

                Intent intent = new Intent(
                        MainActivity.this,
                        OrderActivity.class
                );

                startActivity(intent);

                return true;
            }

            if (id == R.id.nav_cart) {

                Intent intent = new Intent(
                        MainActivity.this,
                        CartActivity.class
                );

                startActivity(intent);

                return true;
            }

            if (id == R.id.nav_favorite) {
                return true;
            }

            if (id == R.id.nav_profile) {

                Intent intent = new Intent(
                        MainActivity.this,
                        ProfileActivity.class
                );

                startActivity(intent);

                return true;
            }

            return false;
        });
    }
}