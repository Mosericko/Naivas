package com.mdevs.naivas.financemanager.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.OrdersAdapter;
import com.mdevs.naivas.customer.classes.Orders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RejectedOrders extends AppCompatActivity implements OrdersAdapter.OrderClickListener {
    RecyclerView rejectedOrdersRv;
    ArrayList<Orders> pOrders = new ArrayList<>();
    OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_orders);

        rejectedOrdersRv = findViewById(R.id.rejectedOrdersRV);
        listOrders();
    }

    private void listOrders() {
        rejectedOrdersRv.setHasFixedSize(true);
        rejectedOrdersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/orders?status=2", null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject orderJson = response.getJSONObject(i);

                    String orderNum = orderJson.getString("order_num");
                    String mpesa_code = orderJson.getString("mpesa_code");
                    String orderDate = orderJson.getString("order_date");
                    String amountPaid = orderJson.getString("total_price");
                    String orderStatus = orderJson.getString("status");

                    pOrders.add(new Orders(orderNum, mpesa_code, orderDate, amountPaid, orderStatus));
                }
                ordersAdapter = new OrdersAdapter(RejectedOrders.this, pOrders);
                rejectedOrdersRv.setAdapter(ordersAdapter);
                ordersAdapter.setOnItemClickListener(RejectedOrders.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "coming ", Toast.LENGTH_SHORT).show();
    }
}