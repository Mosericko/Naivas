package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.CartAdapter;
import com.mdevs.naivas.customer.classes.CartDetails;
import com.mdevs.naivas.databases.DatabaseHandler;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout hiddenLay, hiddenLay2;
    DatabaseHandler myDb;
    TextView totalPrice;
    TextView checkOut;
    TextView clearAll;
    CartAdapter cartAdapter;
    ArrayList<CartDetails> cartArray = new ArrayList<>();
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        myDb = new DatabaseHandler(this);
        hiddenLay = findViewById(R.id.hiddenLayout);
        hiddenLay2 = findViewById(R.id.hiddenLayout2);
        recyclerView = findViewById(R.id.cartRecycler);
        totalPrice = findViewById(R.id.totalPrice);
        checkOut = findViewById(R.id.checkOut);
        clearAll = findViewById(R.id.clearItems);
        swipe = findViewById(R.id.swipeRefreshLayout);

        swipe.setOnRefreshListener(() -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            swipe.setRefreshing(false);
        });

        clearAll.setOnClickListener(v -> {
            myDb.deleteCartItems();
            clearAllItems();
            Toast.makeText(Cart.this, "Items Cleared Successfully!", Toast.LENGTH_SHORT).show();
        });
        /*checkOut.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this,CheckOut.class);
            intent.putExtra("total",)
          startActivity(new Intent(Cart.this,CheckOut.class));
        });*/

        loadCart();
        grandTotal();
    }

    public void loadCart() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cartArray.addAll(myDb.getCartDetails());

        cartAdapter = new CartAdapter(this, cartArray);
        recyclerView.setAdapter(cartAdapter);

        if (cartArray.isEmpty()) {
            hiddenLay.setVisibility(View.GONE);
            hiddenLay2.setVisibility(View.GONE);
        } else {
            hiddenLay.setVisibility(View.VISIBLE);
            hiddenLay2.setVisibility(View.VISIBLE);
        }


    }

    private void grandTotal() {
        int position;
        int priceTotal = 0;
        CartDetails cartDetails;


        for (position = 0; position < cartArray.size(); position++) {
            cartDetails = cartArray.get(position);
            priceTotal = priceTotal + (Integer.parseInt(cartDetails.getPrice()) * Integer.parseInt(cartDetails.getSelectedQuantity()));

            totalPrice.setText(String.valueOf(priceTotal));

            int finalPriceTotal = priceTotal;

            checkOut.setOnClickListener(v -> {
                Intent intent = new Intent(Cart.this,CheckOut.class);
                intent.putExtra("Price",String.valueOf(finalPriceTotal));

                startActivity(intent);
            });

        }
    }

    public void clearAllItems() {
        int size = cartArray.size();
        if (size > 0) {
            cartArray.subList(0, size).clear();

            cartAdapter.notifyItemRangeRemoved(0, size);
            hiddenLay.setVisibility(View.GONE);
            hiddenLay2.setVisibility(View.GONE);


        }
    }
}