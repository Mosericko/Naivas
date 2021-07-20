package com.mdevs.naivas.customer.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.OrderItemsAdapter;
import com.mdevs.naivas.customer.classes.OrderItems;
import com.mdevs.naivas.helperclasses.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mdevs.naivas.customer.activities.MyOrders.ORDER_NUM;
import static com.mdevs.naivas.customer.activities.MyOrders.ORDER_STATUS;

public class OrderDetails extends AppCompatActivity {
    TextView orderStatus;
    String orderNumber, status;
    RecyclerView listOfBoughtItems;
    ArrayList<OrderItems> itemsArrayList = new ArrayList<>();
    OrderItemsAdapter orderItemsAdapter;
    Button download;

    DownloadManager downloadManager;
    private static final int REQUEST_CODE = 1;

    String receiptUrl = " https://android.officialm-devs.com/naivas/android/receipt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderStatus = findViewById(R.id.orderStatus);
        listOfBoughtItems = findViewById(R.id.listOfItemsBought);
        download = findViewById(R.id.downloadReceipt);


        Intent intent = getIntent();
        orderNumber = intent.getStringExtra(ORDER_NUM);
        status = intent.getStringExtra(ORDER_STATUS);

        switch (status) {
            case "0":
                orderStatus.setText(R.string.pending);
                download.setVisibility(View.GONE);
                break;
            case "1":
                orderStatus.setText(R.string.approved);
                break;
            case "2":
                orderStatus.setText(R.string.rejected);
                download.setVisibility(View.GONE);
                break;

        }
        download.setOnClickListener(v -> {
            downloadReceipt();
        });

        listItems();

        ActivityCompat.requestPermissions((Activity) this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
    }

    private void downloadReceipt() {

        ReceiptAsync receiptAsync = new ReceiptAsync(orderNumber);
        receiptAsync.execute();
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
                        String totalPrice = orderItems.getString("selectedPrice");

                        itemsArrayList.add(new OrderItems(name, quantity, totalPrice, price));
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

    public class ReceiptAsync extends AsyncTask<Void, Void, String> {

        String orderNum;

        public ReceiptAsync(String orderNum) {
            this.orderNum = orderNum;
        }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("order_num", orderNum);

            return requestHandler.sendPostRequest(receiptUrl, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                String receiptAddress = obj.getString("url");

                Log.d("TAG", "downloadReceipt: " + receiptAddress);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(receiptAddress));
                String title = URLUtil.guessFileName(receiptAddress, null, null);
                //Set the title of this download, to be displayed in notifications (if enabled).
                request.setTitle(title);
                //Set a description of this download, to be displayed in notifications (if enabled)
                request.setDescription("Receipt download using DownloadManager.");

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                //Set the local destination for the downloaded file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                downloadManager.enqueue(request);
                Toast.makeText(OrderDetails.this, "Download Completed", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}