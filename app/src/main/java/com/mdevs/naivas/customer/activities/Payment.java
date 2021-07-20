package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.CustomerActivity;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.mdevs.naivas.customer.activities.CheckOut.ID;
import static com.mdevs.naivas.customer.activities.CheckOut.JSON;
import static com.mdevs.naivas.customer.activities.CheckOut.ORDER_NUM;
import static com.mdevs.naivas.customer.activities.CheckOut.TOTAL_PRICE;

public class Payment extends AppCompatActivity {
    String jsonIn, idIn, orderNumIn, totalPriceIn;
    EditText mpesaCode;
    Button placeOrder;
    DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mpesaCode = findViewById(R.id.mpesaCode);
        placeOrder = findViewById(R.id.placeOrder);

        myDb = new DatabaseHandler(this);


        Log.d("TAG", "onCreate: " + jsonIn);

        placeOrder.setOnClickListener(v -> {
            sendOrderToDb();
        });

    }

    private void sendOrderToDb() {

        Intent intent = getIntent();
        idIn = intent.getStringExtra(ID);
        orderNumIn = intent.getStringExtra(ORDER_NUM);
        totalPriceIn = intent.getStringExtra(TOTAL_PRICE);
        jsonIn = intent.getStringExtra(JSON);
        final String mpesa_code = mpesaCode.getText().toString().trim();

        //Validations
        if (mpesa_code.isEmpty()) {
            mpesaCode.setError("Cannot be Blank!");
            return;
        }
        if (mpesa_code.length() < 10) {
            mpesaCode.setError("Cannot be less than 10");
            return;
        }

        OrderAsync orderAsync = new OrderAsync(jsonIn, mpesa_code, idIn, orderNumIn, totalPriceIn);
        orderAsync.execute();
    }

    public class OrderAsync extends AsyncTask<Void, Void, String> {
        String json;
        String mpesa_code;
        String user_id;
        String orderNumber;
        String totalPrice;

        public OrderAsync(String json, String mpesa_code, String user_id, String orderNumber, String totalPrice) {
            this.json = json;
            this.mpesa_code = mpesa_code;
            this.user_id = user_id;
            this.orderNumber = orderNumber;
            this.totalPrice = totalPrice;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("json", json);
            params.put("order_num", orderNumber);
            params.put("mpesa_code", mpesa_code);
            params.put("user_id", user_id);
            params.put("total_price", totalPrice);

            return requestHandler.sendPostRequest(URLs.URL_PLACE_ORDER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(Payment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    myDb.deleteCartItems();

                    startActivity(new Intent(Payment.this, CustomerActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}