package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;

public class MyProfile extends AppCompatActivity {
    LinearLayout profile, orders, notifications, faqs, feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profile = findViewById(R.id.profile);
        orders = findViewById(R.id.myOrders);
        notifications = findViewById(R.id.myNotifs);
        faqs = findViewById(R.id.faqs);
        feedback = findViewById(R.id.feedback);

        profile.setOnClickListener(v -> {
            startActivity(new Intent(MyProfile.this, UserProfile.class));
        });

        feedback.setOnClickListener(v -> {
            startActivity(new Intent(MyProfile.this, FeedBack.class));
        });

        orders.setOnClickListener(v -> {
            startActivity(new Intent(MyProfile.this, MyOrders.class));
        });

        faqs.setOnClickListener(v -> {
            startActivity(new Intent(MyProfile.this, FAQs.class));
        });

        notifications.setOnClickListener(v -> {
            startActivity(new Intent(MyProfile.this, Notifications.class));
        });

    }

}