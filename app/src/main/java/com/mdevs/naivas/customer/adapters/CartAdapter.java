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
import com.mdevs.naivas.customer.classes.CartDetails;
import com.mdevs.naivas.databases.DatabaseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {
    Context context;
    ArrayList<CartDetails> cartArrayList;


    public CartAdapter(Context context, ArrayList<CartDetails> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public CartVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_card, parent, false);
        return new CartVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartVH holder, int position) {
        CartDetails cartDetails = cartArrayList.get(position);
        Glide.with(context)
                .load(cartDetails.getImage())
                .into(holder.image);
        holder.title.setText(cartDetails.getName());
        holder.category.setText(cartDetails.getCategory());
        holder.price.setText(cartDetails.getPrice());
        holder.quantity.setText(cartDetails.getSelectedQuantity());

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }


    public class CartVH extends RecyclerView.ViewHolder {
        ImageView image, delete;
        TextView title, category, price, quantity;

        public CartVH(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_image);
            delete = itemView.findViewById(R.id.deleteItem);
            category = itemView.findViewById(R.id.cart_category);
            price = itemView.findViewById(R.id.cart_price);
            quantity = itemView.findViewById(R.id.cart_quantity);
            title = itemView.findViewById(R.id.name_prod);

            delete.setOnClickListener(v -> {

                removeCartItem();
            });
        }

        private void removeCartItem() {
            DatabaseHandler myDb = new DatabaseHandler(context);
            int position = getAdapterPosition();
            CartDetails cart = cartArrayList.get(position);

            int id = Integer.parseInt(cart.getId());

            myDb.deleteOneItem(id);
            cartArrayList.remove(position);
            notifyItemRemoved(position);

        }

    }
}
