package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.Bundle;

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

public class PendingOrders extends AppCompatActivity implements OrdersAdapter.OrderClickListener {
    RecyclerView pendingOrdersRv;
    ArrayList<Orders> pOrders = new ArrayList<>();
    OrdersAdapter ordersAdapter;

    public static final String ORDER_NUMBER = "orderNo";
    public static final String STATUS = "orderStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        pendingOrdersRv = findViewById(R.id.pendingOrdersRv);
        listOrders();
    }

    private void listOrders() {
        pendingOrdersRv.setHasFixedSize(true);
        pendingOrdersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/orders", null, response -> {
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
                ordersAdapter = new OrdersAdapter(PendingOrders.this, pOrders);
                pendingOrdersRv.setAdapter(ordersAdapter);
                ordersAdapter.setOnItemClickListener(PendingOrders.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(PendingOrders.this, FinanceOrderDetails.class);
        Orders orders = pOrders.get(position);

        intent.putExtra(ORDER_NUMBER, orders.getOrderNo());
        intent.putExtra(STATUS, orders.getOrderStatus());
        startActivity(intent);
    }
}