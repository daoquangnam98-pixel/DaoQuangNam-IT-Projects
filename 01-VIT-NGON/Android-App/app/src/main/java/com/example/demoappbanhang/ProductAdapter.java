package com.example.demoappbanhang;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder,
            int position) {

        Product product = productList.get(position);

        // Tên sản phẩm
        holder.txtProductName.setText(product.getName());

        // Đánh giá + số lượng đã bán
        holder.txtRating.setText(
                "⭐ " + product.getRating()
                        + "  •  "
                        + product.getSold()
                        + " đã bán"
        );

        // Giá tiền
        NumberFormat formatter =
                NumberFormat.getInstance(new Locale("vi", "VN"));

        holder.txtPrice.setText(
                formatter.format(product.getPrice()) + "đ"
        );

        // HIỂN THỊ ẢNH SẢN PHẨM
        int imageResId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(
                        product.getImage(),
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );

        if (imageResId != 0) {
            holder.imgProduct.setImageResource(imageResId);
        } else {
            holder.imgProduct.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        // Bấm vào sản phẩm
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    ProductDetailActivity.class
            );

            intent.putExtra("name", product.getName());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("rating", product.getRating());
            intent.putExtra("sold", product.getSold());
            intent.putExtra("image", product.getImage());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtProductName;
        TextView txtRating;
        TextView txtPrice;
        TextView btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}