package com.mdevs.naivas.driver;

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
import com.mdevs.naivas.databases.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignedOrders extends AppCompatActivity implements OrdersAdapter.OrderClickListener {
    int id = PrefManager.getInstance(this).UserID();
    ArrayList<Orders> myOrders = new ArrayList<>();
    OrdersAdapter ordersAdapter;
    RecyclerView listOfOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_orders);

        listOfOrders = findViewById(R.id.listOfOrders);
        listOrders();
    }

    private void listOrders() {
        listOfOrders.setHasFixedSize(true);
        listOfOrders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/drivers?driver_id=" + id, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject orderJson = response.getJSONObject(i);

                    String orderNum = orderJson.getString("order_num");
                    String mpesa_code = orderJson.getString("mpesa_code");
                    String orderDate = orderJson.getString("order_date");
                    String amountPaid = orderJson.getString("total_price");
                    String orderStatus = orderJson.getString("status");
                    String custId = orderJson.getString("user_id");

                    myOrders.add(new Orders(orderNum, mpesa_code, custId, orderDate, amountPaid, orderStatus));
                }
                ordersAdapter = new OrdersAdapter(AssignedOrders.this, myOrders);
                listOfOrders.setAdapter(ordersAdapter);
                ordersAdapter.setOnItemClickListener(AssignedOrders.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(AssignedOrders.this, AssignedOrderDetails.class);
        Orders orders = myOrders.get(position);

        intent.putExtra("orderNum", orders.getOrderNo());
        intent.putExtra("user_id", orders.getCust_id());
        intent.putExtra("status", orders.getOrderStatus());

        startActivity(intent);

    }
}