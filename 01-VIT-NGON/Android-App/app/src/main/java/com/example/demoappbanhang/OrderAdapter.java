package com.example.demoappbanhang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position) {

        Order order = orderList.get(position);

        holder.txtOrderId.setText("#" + order.getOrderId());
        holder.txtOrderDate.setText(order.getDate());
        holder.txtOrderStatus.setText(order.getStatus());

        holder.txtOrderCustomer.setText(
                "👤 " + order.getCustomerName()
                        + " • " + order.getPhone()
        );

        holder.txtOrderAddress.setText(
                "📍 " + order.getAddress()
        );

        holder.txtOrderPayment.setText(
                "💳 " + order.getPaymentMethod()
        );

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        holder.txtOrderTotal.setText(
                formatter.format(order.getTotalPrice()) + "đ"
        );

        // Bấm vào đơn hàng
        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent =
                    new Intent(context, OrderDetailActivity.class);

            intent.putExtra(
                    "orderId",
                    order.getOrderId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtOrderId;
        TextView txtOrderDate;
        TextView txtOrderStatus;
        TextView txtOrderCustomer;
        TextView txtOrderAddress;
        TextView txtOrderPayment;
        TextView txtOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId =
                    itemView.findViewById(R.id.txtOrderId);

            txtOrderDate =
                    itemView.findViewById(R.id.txtOrderDate);

            txtOrderStatus =
                    itemView.findViewById(R.id.txtOrderStatus);

            txtOrderCustomer =
                    itemView.findViewById(R.id.txtOrderCustomer);

            txtOrderAddress =
                    itemView.findViewById(R.id.txtOrderAddress);

            txtOrderPayment =
                    itemView.findViewById(R.id.txtOrderPayment);

            txtOrderTotal =
                    itemView.findViewById(R.id.txtOrderTotal);
        }
    }
}