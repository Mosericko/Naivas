package com.mdevs.naivas.distributionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.distributionmanager.activities.AllDrivers;
import com.mdevs.naivas.distributionmanager.activities.DispatchMessages;
import com.mdevs.naivas.distributionmanager.activities.DispatchedOrders;
import com.mdevs.naivas.financemanager.activities.ApprovedOrder;
import com.mdevs.naivas.financemanager.activities.FinanceProfile;
import com.mdevs.naivas.helperclasses.User;

public class TransportActivity extends AppCompatActivity {

    RelativeLayout profile;
    TextView firstName;
    DatabaseHandler myDb;
    SwipeRefreshLayout swipe;
    int id;
    LinearLayout appOrders, allDrivers, messages, dispatchedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        firstName = findViewById(R.id.firstName);
        profile = findViewById(R.id.profile);
        appOrders = findViewById(R.id.appOrders);
        allDrivers = findViewById(R.id.allDrivers);
        messages = findViewById(R.id.messages);
        dispatchedOrders = findViewById(R.id.dispatchedOrders);


        profile.setOnClickListener(v -> {
            startActivity(new Intent(TransportActivity.this, FinanceProfile.class));
        });

        appOrders.setOnClickListener(v -> {
            startActivity(new Intent(TransportActivity.this, ApprovedOrder.class));
        });

        allDrivers.setOnClickListener(v -> {
            startActivity(new Intent(TransportActivity.this, AllDrivers.class));
        });

        messages.setOnClickListener(v -> {
            startActivity(new Intent(TransportActivity.this, DispatchMessages.class));
        });

        dispatchedOrders.setOnClickListener(v -> {
            startActivity(new Intent(TransportActivity.this, DispatchedOrders.class));
        });


        displayProfile();
    }

    private void displayProfile() {
        id = PrefManager.getInstance(this).UserID();
        myDb = new DatabaseHandler(this);
        User user = myDb.getUser(id);

        firstName.setText(user.getFirstname());
    }
}