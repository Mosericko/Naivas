package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.CheckOutListAdapter;
import com.mdevs.naivas.customer.classes.CartDetails;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;

import java.util.ArrayList;
import java.util.Random;

public class CheckOut extends AppCompatActivity {
    //Intent Constants
    public static final String ID = "id";
    public static final String ORDER_NUM = "order_num";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String JSON = "json";


    String totalIn, orderNumber, json;
    TextView total, addOrEdit;
    RelativeLayout location;
    RecyclerView listOfItems;
    TextView makePayment;
    DatabaseHandler myDb;
    Gson gson;
    ArrayList<CartDetails> goodsArray = new ArrayList<>();
    ArrayList<CartDetails> totalCheckOut;
    CheckOutListAdapter checkOutListAdapter;
    int id = PrefManager.getInstance(this).UserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        //connect views
        total = findViewById(R.id.total);
        location = findViewById(R.id.location);
        listOfItems = findViewById(R.id.listOfItems);
        makePayment = findViewById(R.id.makePayment);
        addOrEdit = findViewById(R.id.addOrEdit);

        //get intent extras
        Intent intent = getIntent();
        totalIn = intent.getStringExtra("Price");
        total.setText(totalIn);

        //initialize variables
        gson = new Gson();
        myDb = new DatabaseHandler(this);
        totalCheckOut = myDb.getCartDetails();

        json = gson.toJson(totalCheckOut);
        Log.d("TAG", "sendOrderToDb: " + json);
        orderNumber = setOrderNumber();
        Log.d("TAG", "oNumber: " + orderNumber);

        location.setOnClickListener(v -> {
            startActivity(new Intent(CheckOut.this, LocationDetails.class));
        });


        makePayment.setOnClickListener(v -> {

     /*       if (addOrEdit.getText().toString().equals("Add")) {
                Toast.makeText(this, "Please Add Location Details", Toast.LENGTH_SHORT).show();

            } else {*/

            Intent orders = new Intent(CheckOut.this, Payment.class);
            orders.putExtra(ID, String.valueOf(id));
            orders.putExtra(ORDER_NUM, orderNumber);
            orders.putExtra(TOTAL_PRICE, totalIn);
            orders.putExtra(JSON, json);
            startActivity(orders);
            // }

        });


        listGoods();
    }

    private void listGoods() {
        myDb = new DatabaseHandler(this);
        listOfItems.setHasFixedSize(true);
        listOfItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        goodsArray.addAll(myDb.getCartDetails());
        checkOutListAdapter = new CheckOutListAdapter(CheckOut.this, goodsArray);
        listOfItems.setAdapter(checkOutListAdapter);

    }


    private String setOrderNumber() {
        char[] myChars = "1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            char c1 = myChars[random.nextInt(myChars.length)];
            stringBuilder.append(c1);
        }

        String randomString = stringBuilder.toString();

        return ("OrNo" + randomString);
    }

    private void getLocationString(){


    }

}