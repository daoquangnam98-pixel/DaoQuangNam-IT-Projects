package com.example.demoappbanhang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> cartItems;
    private final Runnable onCartChanged;

    public CartAdapter(List<CartItem> cartItems, Runnable onCartChanged) {
        this.cartItems = cartItems;
        this.onCartChanged = onCartChanged;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CartViewHolder holder,
            int position) {

        CartItem item = cartItems.get(position);

        // Tên sản phẩm
        holder.txtCartProductName.setText(item.getName());

        // Giá sản phẩm
        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        holder.txtCartProductPrice.setText(
                formatter.format(item.getPrice()) + "đ"
        );

        // Số lượng
        holder.txtQuantity.setText(
                String.valueOf(item.getQuantity())
        );

        // =========================
        // HIỂN THỊ ẢNH SẢN PHẨM
        // =========================

        String image = item.getImage();

        if (image != null && !image.isEmpty()) {

            int imageResource = holder.itemView
                    .getContext()
                    .getResources()
                    .getIdentifier(
                            image,
                            "drawable",
                            holder.itemView
                                    .getContext()
                                    .getPackageName()
                    );

            if (imageResource != 0) {

                holder.imgCartProduct.setImageResource(
                        imageResource
                );

            } else {

                holder.imgCartProduct.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } else {

            holder.imgCartProduct.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        // =========================
        // NÚT GIẢM
        // =========================

        holder.btnMinus.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {
                return;
            }

            CartItem cartItem =
                    cartItems.get(currentPosition);

            if (cartItem.getQuantity() > 1) {

                cartItem.setQuantity(
                        cartItem.getQuantity() - 1
                );

                notifyItemChanged(currentPosition);

            } else {

                cartItems.remove(currentPosition);

                notifyItemRemoved(currentPosition);
            }

            onCartChanged.run();
        });

        // =========================
        // NÚT TĂNG
        // =========================

        holder.btnPlus.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {
                return;
            }

            CartItem cartItem =
                    cartItems.get(currentPosition);

            cartItem.setQuantity(
                    cartItem.getQuantity() + 1
            );

            notifyItemChanged(currentPosition);

            onCartChanged.run();
        });

        // =========================
        // NÚT XÓA
        // =========================

        holder.btnRemove.setOnClickListener(v -> {

            int currentPosition =
                    holder.getBindingAdapterPosition();

            if (currentPosition ==
                    RecyclerView.NO_POSITION) {
                return;
            }

            cartItems.remove(currentPosition);

            notifyItemRemoved(currentPosition);

            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgCartProduct;

        TextView txtCartProductName;
        TextView txtCartProductPrice;
        TextView txtQuantity;

        TextView btnMinus;
        TextView btnPlus;

        ImageButton btnRemove;

        public CartViewHolder(
                @NonNull View itemView) {

            super(itemView);

            imgCartProduct =
                    itemView.findViewById(
                            R.id.imgCartProduct
                    );

            txtCartProductName =
                    itemView.findViewById(
                            R.id.txtCartProductName
                    );

            txtCartProductPrice =
                    itemView.findViewById(
                            R.id.txtCartProductPrice
                    );

            txtQuantity =
                    itemView.findViewById(
                            R.id.txtQuantity
                    );

            btnMinus =
                    itemView.findViewById(
                            R.id.btnMinus
                    );

            btnPlus =
                    itemView.findViewById(
                            R.id.btnPlus
                    );

            btnRemove =
                    itemView.findViewById(
                            R.id.btnRemove
                    );
        }
    }
}