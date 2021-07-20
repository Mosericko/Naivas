package com.mdevs.naivas.financemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.financemanager.activities.ApprovedOrder;
import com.mdevs.naivas.financemanager.activities.FinanceProfile;
import com.mdevs.naivas.financemanager.activities.Messages;
import com.mdevs.naivas.financemanager.activities.PendingOrders;
import com.mdevs.naivas.financemanager.activities.RejectedOrders;
import com.mdevs.naivas.helperclasses.User;

import org.json.JSONException;

public class FinanceActivity extends AppCompatActivity {

    RelativeLayout profile;
    LinearLayout pending, approved, rejected, messages;
    TextView totalPending, totalAmount, firstName;
    DatabaseHandler myDb;
    SwipeRefreshLayout swipe;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        firstName = findViewById(R.id.firstName);
        profile = findViewById(R.id.profile);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        rejected = findViewById(R.id.rejected);
        messages = findViewById(R.id.messages);
        swipe = findViewById(R.id.financeSwipe);

        swipe.setOnRefreshListener(() -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            swipe.setRefreshing(false);
        });

        totalPending = findViewById(R.id.totalPending);
        totalAmount = findViewById(R.id.totalAmount);

        profile.setOnClickListener(v -> {
            startActivity(new Intent(FinanceActivity.this, FinanceProfile.class));
        });
        pending.setOnClickListener(v -> {
            startActivity(new Intent(FinanceActivity.this, PendingOrders.class));
        });

        approved.setOnClickListener(v -> {
            startActivity(new Intent(FinanceActivity.this, ApprovedOrder.class));
        });

        rejected.setOnClickListener(v -> {
            startActivity(new Intent(FinanceActivity.this, RejectedOrders.class));
        });

        messages.setOnClickListener(v -> {
            startActivity(new Intent(FinanceActivity.this, Messages.class));
        });

        displayProfile();
        getDashBoardDetails();
    }

    private void getDashBoardDetails() {

        String url = "https://android.officialm-devs.com/naivas/android/orders?stats=1";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                totalPending.setText(response.getString("count"));
                totalAmount.setText(response.getString("sum"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace);

        requestQueue.add(jsonObjectRequest);

    }

    private void displayProfile() {
        id = PrefManager.getInstance(this).UserID();
        myDb = new DatabaseHandler(this);
        User user = myDb.getUser(id);

        firstName.setText(user.getFirstname());
    }
}