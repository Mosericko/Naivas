package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.classes.OrderItems;
import com.mdevs.naivas.financemanager.FinanceActivity;
import com.mdevs.naivas.financemanager.adapters.FinanceDetailsAdapter;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mdevs.naivas.financemanager.activities.PendingOrders.ORDER_NUMBER;
import static com.mdevs.naivas.financemanager.activities.PendingOrders.STATUS;

public class FinanceOrderDetails extends AppCompatActivity {
    TextView orderStatus;
    String orderNumber, status;
    RecyclerView listOfBoughtItems;
    ArrayList<OrderItems> itemsArrayList = new ArrayList<>();
    FinanceDetailsAdapter orderItemsAdapter;
    Button approve, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_order_details);

        orderStatus = findViewById(R.id.orderStatus);
        listOfBoughtItems = findViewById(R.id.listOfItemsBought);
        approve = findViewById(R.id.approve);
        cancel = findViewById(R.id.cancel);

        Intent intent = getIntent();
        orderNumber = intent.getStringExtra(ORDER_NUMBER);
        status = intent.getStringExtra(STATUS);

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

        approve.setOnClickListener(v -> {
            approveOrder();
        });

        listItems();
    }


    private void approveOrder() {

        ApproveAsync approveAsync = new ApproveAsync(orderNumber);
        approveAsync.execute();


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

                        itemsArrayList.add(new OrderItems(name, quantity, totalPrice, price,stock));
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

    public class ApproveAsync extends AsyncTask<Void, Void, String> {

        String orderNum;

        public ApproveAsync(String orderNum) {
            this.orderNum = orderNum;
        }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("approve", orderNum);

            return requestHandler.sendPostRequest(URLs.URL_PLACE_ORDER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FinanceOrderDetails.this, FinanceActivity.class));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}