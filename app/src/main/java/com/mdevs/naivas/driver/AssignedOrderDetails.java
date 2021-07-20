package com.mdevs.naivas.driver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.AddressAdapter;
import com.mdevs.naivas.customer.adapters.OrderItemsAdapter;
import com.mdevs.naivas.customer.classes.Address;
import com.mdevs.naivas.customer.classes.OrderItems;
import com.mdevs.naivas.helperclasses.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignedOrderDetails extends AppCompatActivity {

    String orderN, custId, status;
    RecyclerView listOfBoughtItems, address;
    ArrayList<OrderItems> itemsArrayList = new ArrayList<>();
    OrderItemsAdapter orderItemsAdapter;
    Button confirmOrder;

    ArrayList<Address> details = new ArrayList<>();
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_order_details);

        listOfBoughtItems = findViewById(R.id.listOfItemsBought);
        address = findViewById(R.id.locationDetails);
        confirmOrder = findViewById(R.id.confirmDelivered);

        Intent intent = getIntent();
        orderN = intent.getStringExtra("orderNum");
        custId = intent.getStringExtra("user_id");
        status = intent.getStringExtra("status");

        confirmOrder.setOnClickListener(v -> {
            orderDelivered();
        });


        listAddress();
        listItems();

    }

    private void orderDelivered() {
        SendConfirmationAsync sendConfirmationAsync = new SendConfirmationAsync(orderN, custId);
        sendConfirmationAsync.execute();
    }

    private void listItems() {
        String url = "https://android.officialm-devs.com/naivas/android/orders?order_num=" + orderN;
        listOfBoughtItems.setHasFixedSize(true);
        listOfBoughtItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject orderItems = jsonArray.getJSONObject(i);

                        String name = orderItems.getString("productName");
                        String price = orderItems.getString("price");
                        String quantity = orderItems.getString("selectedQuantity");
                        String stock = orderItems.getString("quantity");
                        String totalPrice = orderItems.getString("selectedPrice");

                        itemsArrayList.add(new OrderItems(name, quantity, totalPrice, price, stock));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    orderItemsAdapter = new OrderItemsAdapter(this, itemsArrayList);
                    listOfBoughtItems.setAdapter(orderItemsAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, "Error 500", Toast.LENGTH_SHORT).show());


        requestQueue.add(stringRequest);
    }

    private void listAddress() {

        address.setHasFixedSize(true);
        address.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/location?user_id=" + custId, null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject locationDetails = response.getJSONObject(i);

                    String streetName = locationDetails.getString("street_name");
                    String buildingName = locationDetails.getString("building");
                    String phone = locationDetails.getString("phone");


                    details.add(new Address(streetName, buildingName, phone));
                }

                addressAdapter = new AddressAdapter(AssignedOrderDetails.this, details);
                address.setAdapter(addressAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);

    }

    public class SendConfirmationAsync extends AsyncTask<Void, Void, String> {

        String order_number, user_id;

        public SendConfirmationAsync(String order_number, String user_id) {
            this.order_number = order_number;
            this.user_id = user_id;
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("order_number", order_number);
            params.put("user_id", user_id);

            return requestHandler.sendPostRequest("http://android.officialm-devs.com/naivas/android/drivers", params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(AssignedOrderDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AssignedOrderDetails.this, DriverActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}