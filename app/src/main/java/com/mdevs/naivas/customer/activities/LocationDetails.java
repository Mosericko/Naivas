package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.AddressAdapter;
import com.mdevs.naivas.customer.classes.Address;
import com.mdevs.naivas.databases.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationDetails extends AppCompatActivity implements AddressAdapter.CardClickListener {
    RecyclerView address;
    RelativeLayout addressLay;
    int id = PrefManager.getInstance(this).UserID();
    ArrayList<Address> details = new ArrayList<>();
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        address = findViewById(R.id.addressRecyclerView);
        addressLay = findViewById(R.id.addressLay);

        addressLay.setOnClickListener(v -> startActivity(new Intent(LocationDetails.this, LocationForm.class)));

        listAddress();
    }

    private void listAddress() {

        address.setHasFixedSize(true);
        address.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/location?user_id=" + id, null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject locationDetails = response.getJSONObject(i);

                    String streetName = locationDetails.getString("street_name");
                    String buildingName = locationDetails.getString("building");
                    String phone = locationDetails.getString("phone");


                    details.add(new Address(streetName, buildingName, phone));
                }

                addressAdapter = new AddressAdapter(LocationDetails.this, details);
                address.setAdapter(addressAdapter);
                addressAdapter.setCardListener(LocationDetails.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);

    }


    @Override
    public void onCardClick(int cardPosition) {
        Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
    }
}