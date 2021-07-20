package com.mdevs.naivas.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdevs.naivas.R;
import com.mdevs.naivas.productmanager.GroceryDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsVH> {

    Context context;
    ArrayList<GroceryDetails> products;
    private ClickListener clickListener;


    public ProductsAdapter(Context context, ArrayList<GroceryDetails> products) {
        this.context = context;
        this.products = products;
    }

    //interface for seeing more details

    public interface ClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ProductsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_design, parent, false);
        return new ProductsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsVH holder, int position) {
        GroceryDetails groceryDetails = products.get(position);
        Glide.with(context)
                .load(groceryDetails.getImage())
                .into(holder.image);

        holder.name.setText(groceryDetails.getName());
        holder.price.setText(groceryDetails.getPrice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductsVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price;

        public ProductsVH(@NonNull @NotNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.groceryImage);
            name = itemView.findViewById(R.id.groceryName);
            price = itemView.findViewById(R.id.groceryPrice);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    //get the position of the adapter

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        clickListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
