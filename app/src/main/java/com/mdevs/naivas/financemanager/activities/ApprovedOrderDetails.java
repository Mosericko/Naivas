package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.mdevs.naivas.customer.classes.Address;
import com.mdevs.naivas.customer.classes.OrderItems;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.distributionmanager.adapters.DriverAdapter;
import com.mdevs.naivas.distributionmanager.classes.DriverDetails;
import com.mdevs.naivas.financemanager.adapters.FinanceDetailsAdapter;
import com.mdevs.naivas.helperclasses.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mdevs.naivas.financemanager.activities.ApprovedOrder.ID;
import static com.mdevs.naivas.financemanager.activities.PendingOrders.ORDER_NUMBER;
import static com.mdevs.naivas.financemanager.activities.PendingOrders.STATUS;

public class ApprovedOrderDetails extends AppCompatActivity implements DriverAdapter.DriverCardClickListener {
    TextView orderStatus;
    String orderNumber, status, customerId;
    RecyclerView listOfBoughtItems, address, driversRV;
    ArrayList<OrderItems> itemsArrayList = new ArrayList<>();
    FinanceDetailsAdapter orderItemsAdapter;
    int id = PrefManager.getInstance(this).UserID();
    LinearLayout location, avDrivers;

    ArrayList<Address> details = new ArrayList<>();
    AddressAdapter addressAdapter;
    ArrayList<DriverDetails> driverInfo = new ArrayList<>();
    DriverAdapter driverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_order_details);

        orderStatus = findViewById(R.id.orderStatus);
        listOfBoughtItems = findViewById(R.id.listOfItemsBought);
        location = findViewById(R.id.location);
        avDrivers = findViewById(R.id.avDrivers);
        address = findViewById(R.id.locationDetails);
        driversRV = findViewById(R.id.availableDrivers);

        if (String.valueOf(id).equals("3")) {
            location.setVisibility(View.GONE);
            avDrivers.setVisibility(View.GONE);
        }


        Intent intent = getIntent();
        orderNumber = intent.getStringExtra(ORDER_NUMBER);
        status = intent.getStringExtra(STATUS);
        customerId = intent.getStringExtra(ID);

        // orderStatus.setText(status);
        switch (status) {
            case "0":
                orderStatus.setText(R.string.pending);
                break;
            case "1":
                orderStatus.setText(R.string.approved);
                break;
            case "2":
                orderStatus.setText(R.string.rejected);
                break;

        }
        listItems();
        listAddress();
        listDrivers();
    }

    private void listItems() {
        String url = "https://android.officialm-devs.com/naivas/android/orders?order_num=" + orderNumber;
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

                    orderItemsAdapter = new FinanceDetailsAdapter(this, itemsArrayList);
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/location?user_id=" + customerId, null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject locationDetails = response.getJSONObject(i);

                    String streetName = locationDetails.getString("street_name");
                    String buildingName = locationDetails.getString("building");
                    String phone = locationDetails.getString("phone");


                    details.add(new Address(streetName, buildingName, phone));
                }

                addressAdapter = new AddressAdapter(ApprovedOrderDetails.this, details);
                address.setAdapter(addressAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);

    }

    private void listDrivers() {

        driversRV.setHasFixedSize(true);
        driversRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/drivers?drivers=1", null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject driverDetails = response.getJSONObject(i);

                    String firstname = driverDetails.getString("firstName");
                    String lastname = driverDetails.getString("lastName");
                    String status = driverDetails.getString("availability");
                    String id = driverDetails.getString("id");


                    driverInfo.add(new DriverDetails(id, firstname, lastname, status));
                }
                driverAdapter = new DriverAdapter(ApprovedOrderDetails.this, driverInfo);
                driversRV.setAdapter(driverAdapter);
                driverAdapter.setOnItemClickListener(ApprovedOrderDetails.this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);

    }


    @Override
    public void onClick(int position) {
        DriverDetails driverDetails = driverInfo.get(position);
        AssignAsync assignAsync = new AssignAsync(driverDetails.getId(), orderNumber);
        assignAsync.execute();
    }

    public class AssignAsync extends AsyncTask<Void, Void, String> {
        String driver_id, orderNumber;

        public AssignAsync(String driver_id, String orderNumber) {
            this.driver_id = driver_id;
            this.orderNumber = orderNumber;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("driver_id", driver_id);
            params.put("order_num", orderNumber);

            return requestHandler.sendPostRequest("http://android.officialm-devs.com/naivas/android/drivers", params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(ApprovedOrderDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ApprovedOrderDetails.this, ApprovedOrder.class));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}