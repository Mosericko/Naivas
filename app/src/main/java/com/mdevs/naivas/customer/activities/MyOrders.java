package com.mdevs.naivas.customer.activities;

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

public class MyOrders extends AppCompatActivity implements OrdersAdapter.OrderClickListener {
    RecyclerView listOfOrders;
    ArrayList<Orders> myOrders = new ArrayList<>();
    OrdersAdapter ordersAdapter;
    int id = PrefManager.getInstance(this).UserID();

    //intent Extras
    public static final String ORDER_NUM = "orderNo";
    public static final String MPESA_CODE = "mpesaCode";
    public static final String TOTAL_PRICE = "total_price";
    public static final String DATE_TIME = "dateTime";
    public static final String ORDER_STATUS = "orderStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        listOfOrders = findViewById(R.id.listOfOrders);

        listOrders();
    }

    private void listOrders() {
        listOfOrders.setHasFixedSize(true);
        listOfOrders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/orders?user_id=" + id, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject orderJson = response.getJSONObject(i);

                    String orderNum = orderJson.getString("order_num");
                    String mpesa_code = orderJson.getString("mpesa_code");
                    String orderDate = orderJson.getString("order_date");
                    String amountPaid = orderJson.getString("total_price");
                    String orderStatus = orderJson.getString("status");

                    myOrders.add(new Orders(orderNum, mpesa_code, orderDate, amountPaid, orderStatus));
                }
                ordersAdapter = new OrdersAdapter(MyOrders.this, myOrders);
                listOfOrders.setAdapter(ordersAdapter);
                ordersAdapter.setOnItemClickListener(MyOrders.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(MyOrders.this, OrderDetails.class);
        Orders orders = myOrders.get(position);

        intent.putExtra(ORDER_NUM, orders.getOrderNo());
        intent.putExtra(ORDER_STATUS, orders.getOrderStatus());

        startActivity(intent);


    }
}