package com.mdevs.naivas.driver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.activities.Notifications;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.financemanager.activities.FinanceProfile;
import com.mdevs.naivas.helperclasses.User;

public class DriverActivity extends AppCompatActivity {
    RelativeLayout profile;
    TextView firstName;
    DatabaseHandler myDb;
    SwipeRefreshLayout swipe;
    LinearLayout notifications, assignedOrders;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        firstName = findViewById(R.id.firstName);
        profile = findViewById(R.id.profile);
        notifications = findViewById(R.id.notifications);
        assignedOrders = findViewById(R.id.assignedWork);

        notifications.setOnClickListener(v -> {
            startActivity(new Intent(DriverActivity.this, Notifications.class));
        });

        assignedOrders.setOnClickListener(v -> {
            startActivity(new Intent(DriverActivity.this, AssignedOrders.class));
        });


        profile.setOnClickListener(v -> {
            startActivity(new Intent(DriverActivity.this, FinanceProfile.class));
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