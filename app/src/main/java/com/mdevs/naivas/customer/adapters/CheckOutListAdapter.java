package com.mdevs.naivas.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.classes.CartDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckOutListAdapter extends RecyclerView.Adapter<CheckOutListAdapter.CheckOutListVH> {
    Context context;
    ArrayList<CartDetails> checkOutList;

    public CheckOutListAdapter(Context context, ArrayList<CartDetails> checkOutList) {
        this.context = context;
        this.checkOutList = checkOutList;
    }

    @NonNull
    @NotNull
    @Override
    public CheckOutListVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.check_out_card, parent, false);
        return new CheckOutListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CheckOutListAdapter.CheckOutListVH holder, int position) {

        CartDetails cartDetails = checkOutList.get(position);
        holder.name.setText(cartDetails.getName());
        holder.quantity.setText(cartDetails.getSelectedQuantity());
        holder.price.setText(cartDetails.getPrice());
        holder.totalPrice.setText(cartDetails.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return checkOutList.size();
    }


    public class CheckOutListVH extends RecyclerView.ViewHolder {
        TextView name, quantity, price, totalPrice;

        public CheckOutListVH(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_prod);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
