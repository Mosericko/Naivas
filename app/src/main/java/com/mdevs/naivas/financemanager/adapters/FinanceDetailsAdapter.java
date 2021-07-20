package com.mdevs.naivas.financemanager.adapters;

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

public class FinanceDetailsAdapter extends RecyclerView.Adapter<FinanceDetailsAdapter.FinanceDetailsVH> {
    Context context;
    ArrayList<OrderItems> itemList;

    private OrderClickListener orderClickListener;

    public FinanceDetailsAdapter(Context context, ArrayList<OrderItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    public void setOnItemClickListener(OrderClickListener itemClickListener) {
        orderClickListener = itemClickListener;
    }

    //onclick interface
    public interface OrderClickListener {
        void onClick(int position);
    }

    @NonNull
    @NotNull
    @Override
    public FinanceDetailsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.finance_card, parent, false);
        return new FinanceDetailsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FinanceDetailsAdapter.FinanceDetailsVH holder, int position) {
        OrderItems orderItems = itemList.get(position);
        holder.name.setText(orderItems.getName());
        holder.quantity.setText(orderItems.getQuantity());
        holder.price.setText(orderItems.getPrice());
        holder.totalPrice.setText(orderItems.getTotalPrice());
        holder.stock.setText(orderItems.getStock());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class FinanceDetailsVH extends RecyclerView.ViewHolder {
        TextView name, quantity, price, totalPrice, stock;

        public FinanceDetailsVH(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_prod);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            stock = itemView.findViewById(R.id.stock);

            itemView.setOnClickListener(v -> {
                if (orderClickListener != null) {
                    //get the position of the adapter

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        orderClickListener.onClick(position);
                    }
                }
            });
        }
    }
}
