package com.mdevs.naivas.customer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.classes.Orders;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersVh> {
    Context context;
    ArrayList<Orders> ordersArrayList;

    private OrderClickListener orderClickListener;


    public OrdersAdapter(Context context, ArrayList<Orders> ordersArrayList) {
        this.context = context;
        this.ordersArrayList = ordersArrayList;
    }

    public void setOnItemClickListener(OrderClickListener itemClickListener) {
        orderClickListener = itemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public OrdersVh onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.orders_card, parent, false);
        return new OrdersVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrdersAdapter.OrdersVh holder, int position) {
        Orders orders = ordersArrayList.get(position);

        holder.orderNo.setText(orders.getOrderNo());
        holder.mpesaCode.setText(orders.getMpesaCode());
        holder.dateTime.setText(orders.getDateTime());
        holder.amountPaid.setText(orders.getAmountPaid());

        if (orders.getOrderStatus().equals("0")) {
            holder.orderStatus.setText(R.string.pending);
        } else if (orders.getOrderStatus().equals("1")) {
            holder.orderStatus.setText(R.string.approved);
            holder.orderStatus.setTextColor(Color.GREEN);
        } else {
            holder.orderStatus.setText(R.string.rejected);
            holder.orderStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    //onclick interface
    public interface OrderClickListener {
        void onClick(int position);
    }


    public class OrdersVh extends RecyclerView.ViewHolder {
        TextView orderNo, mpesaCode, dateTime, amountPaid, orderStatus;

        public OrdersVh(@NonNull @NotNull View itemView) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.orderNo);
            mpesaCode = itemView.findViewById(R.id.mpesaCode);
            dateTime = itemView.findViewById(R.id.dateTime);
            amountPaid = itemView.findViewById(R.id.amountPaid);
            orderStatus = itemView.findViewById(R.id.orderStatus);

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
