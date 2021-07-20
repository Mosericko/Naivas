package com.mdevs.naivas.distributionmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.distributionmanager.classes.DriverDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriveVH> {
    Context context;
    ArrayList<DriverDetails> driverDetails;
    private DriverCardClickListener driverCardClickListener;


    public DriverAdapter(Context context, ArrayList<DriverDetails> driverDetails) {
        this.context = context;
        this.driverDetails = driverDetails;
    }

    public void setOnItemClickListener(DriverCardClickListener itemClickListener) {
        driverCardClickListener = itemClickListener;
    }

    //onclick interface
    public interface DriverCardClickListener {
        void onClick(int position);
    }

    @NonNull
    @NotNull
    @Override
    public DriveVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.driver_card, parent, false);
        return new DriveVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DriverAdapter.DriveVH holder, int position) {
        DriverDetails driver = driverDetails.get(position);

        holder.firstName.setText(driver.getFirstName());
        holder.lastName.setText(driver.getLastName());
        holder.status.setText(driver.getStatus());
        if (driver.getStatus().equals("In transit")) {
            holder.assignDriver.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return driverDetails.size();
    }


    public class DriveVH extends RecyclerView.ViewHolder {
        TextView status, firstName, lastName;
        LinearLayout assignDriver;

        public DriveVH(@NonNull @NotNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            status = itemView.findViewById(R.id.status);
            assignDriver = itemView.findViewById(R.id.assign);

            itemView.setOnClickListener(v -> {
                if (driverCardClickListener != null) {
                    //get the position of the adapter

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        driverCardClickListener.onClick(position);
                    }
                }
            });
        }


    }
}
