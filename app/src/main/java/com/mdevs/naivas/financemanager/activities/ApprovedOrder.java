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

public class ApprovedOrder extends AppCompatActivity implements OrdersAdapter.OrderClickListener {
    RecyclerView approvedOrdersRv;
    ArrayList<Orders> pOrders = new ArrayList<>();
    OrdersAdapter ordersAdapter;

    public static final String ORDER_NUMBER = "orderNo";
    public static final String STATUS = "orderStatus";
    public static final String ID = "custId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_order);

        approvedOrdersRv = findViewById(R.id.approvedOrdersRV);
        listOrders();
    }

    private void listOrders() {
        approvedOrdersRv.setHasFixedSize(true);
        approvedOrdersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/orders?dispatched_orders=2", null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject orderJson = response.getJSONObject(i);

                    String orderNum = orderJson.getString("order_num");
                    String mpesa_code = orderJson.getString("mpesa_code");
                    String orderDate = orderJson.getString("order_date");
                    String amountPaid = orderJson.getString("total_price");
                    String orderStatus = orderJson.getString("status");
                    String custId = orderJson.getString("user_id");

                    pOrders.add(new Orders(orderNum, mpesa_code, custId, orderDate, amountPaid, orderStatus));
                }
                ordersAdapter = new OrdersAdapter(ApprovedOrder.this, pOrders);
                approvedOrdersRv.setAdapter(ordersAdapter);
                ordersAdapter.setOnItemClickListener(ApprovedOrder.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(ApprovedOrder.this, ApprovedOrderDetails.class);
        Orders orders = pOrders.get(position);

        intent.putExtra(ORDER_NUMBER, orders.getOrderNo());
        intent.putExtra(STATUS, orders.getOrderStatus());
        intent.putExtra(ID, orders.getCust_id());
        startActivity(intent);
    }
}