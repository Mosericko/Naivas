package com.mdevs.naivas.distributionmanager.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.distributionmanager.adapters.DriverAdapter;
import com.mdevs.naivas.distributionmanager.adapters.DriverIdCard;
import com.mdevs.naivas.helperclasses.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllDrivers extends AppCompatActivity {
    RecyclerView driversRV;

    ArrayList<User> driver = new ArrayList<>();
    DriverIdCard driverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drivers);

        driversRV = findViewById(R.id.availableDrivers);

        listDrivers();
    }

    private void listDrivers() {

        driversRV.setHasFixedSize(true);
        driversRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/naivas/android/drivers?all_drivers=1", null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject driverDetails = response.getJSONObject(i);

                    String firstname = driverDetails.getString("firstName");
                    String lastname = driverDetails.getString("lastName");
                    String phone = driverDetails.getString("phone");
                    String email = driverDetails.getString("email");


                    driver.add(new User(firstname, lastname, email, phone));
                }

                driverAdapter = new DriverIdCard(AllDrivers.this, driver);
                driversRV.setAdapter(driverAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);

    }
}