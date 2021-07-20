package com.mdevs.naivas.distributionmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DriverIdCard extends RecyclerView.Adapter<DriverIdCard.IdCardVH> {

    Context context;
    ArrayList<User> driverList;

    public DriverIdCard(Context context, ArrayList<User> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @NotNull
    @Override
    public IdCardVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.driver_id_card, parent, false);
        return new IdCardVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DriverIdCard.IdCardVH holder, int position) {
        User user = driverList.get(position);

        holder.firstName.setText(user.getFirstname());
        holder.lastName.setText(user.getLastname());
        holder.phone.setText(user.getPhonenumber());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }


    public class IdCardVH extends RecyclerView.ViewHolder {
        TextView firstName, lastName, phone, email;

        public IdCardVH(@NonNull @NotNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.emailDriver);
        }
    }
}
