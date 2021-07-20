package com.mdevs.naivas.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.classes.OrderItems;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemsVH> {
    Context context;
    ArrayList<OrderItems> itemList;

    public OrderItemsAdapter(Context context, ArrayList<OrderItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @NotNull
    @Override
    public OrderItemsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.check_out_card, parent, false);
        return new OrderItemsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderItemsAdapter.OrderItemsVH holder, int position) {
        OrderItems orderItems = itemList.get(position);
        holder.name.setText(orderItems.getName());
        holder.quantity.setText(orderItems.getQuantity());
        holder.price.setText(orderItems.getPrice());
        holder.totalPrice.setText(orderItems.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class OrderItemsVH extends RecyclerView.ViewHolder {
        TextView name, quantity, price, totalPrice;

        public OrderItemsVH(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_prod);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
