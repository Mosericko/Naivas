package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.helperclasses.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LocationForm extends AppCompatActivity {
    DatabaseHandler myDb;
    EditText streetName, building, phoneNumber;
    Button saveLocation;
    int id = PrefManager.getInstance(this).UserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_form);

        streetName = findViewById(R.id.streetName);
        building = findViewById(R.id.building);
        phoneNumber = findViewById(R.id.phoneNumb);
        saveLocation = findViewById(R.id.saveLocation);

        displayPhoneNumber();
        saveLocation.setOnClickListener(v -> {
            validateDetails();

        });

    }

    private void validateDetails() {
        String street = streetName.getText().toString().trim();
        String build = building.getText().toString().trim();
        String mobile = phoneNumber.getText().toString().trim();

        String mobile_pattern = "^07[0-9]{8}$";

        TextInputLayout sName = findViewById(R.id.streetTil);
        TextInputLayout bName = findViewById(R.id.buildingTil);
        TextInputLayout pNumber = findViewById(R.id.phoneTil);

        if (TextUtils.isEmpty(street)) {
            sName.setError("Please enter Street name!");
            streetName.requestFocus();
            return;
        } else {
            sName.setError(null);
        }

        if (TextUtils.isEmpty(build)) {
            bName.setError("Please enter Building name!");
            building.requestFocus();
            return;
        } else {
            bName.setError(null);
        }

        if (mobile.length() < 10) {
            pNumber.setError("Phone number cannot be less than 10 digits");
            phoneNumber.requestFocus();
            return;
        } else {
            pNumber.setError(null);
        }
        if (!phoneNumber.getText().toString().matches(mobile_pattern)) {
            pNumber.setError("Enter a valid phone number");
            phoneNumber.requestFocus();
            return;
        } else {
            pNumber.setError(null);
        }
        if (!android.util.Patterns.PHONE.matcher(mobile).matches()) {
            pNumber.setError("Enter a valid phone number");
            phoneNumber.requestFocus();
            return;
        } else {
            pNumber.setError(null);
        }

        if (TextUtils.isEmpty(mobile)) {
            pNumber.setError("Please enter your phone number!");
            phoneNumber.requestFocus();
            return;
        } else {
            pNumber.setError(null);
        }


        LocationAsync locationAsync = new LocationAsync(String.valueOf(id), street, build, mobile);
        locationAsync.execute();
    }

    void displayPhoneNumber() {

        myDb = new DatabaseHandler(this);
        User user = myDb.getUser(id);

        phoneNumber.setText(user.getPhonenumber());
    }


    public class LocationAsync extends AsyncTask<Void, Void, String> {
        String id, streetName, building, phoneNumber;

        public LocationAsync(String id, String streetName, String building, String phoneNumber) {
            this.id = id;
            this.streetName = streetName;
            this.building = building;
            this.phoneNumber = phoneNumber;
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", String.valueOf(id));
            params.put("street_name", streetName);
            params.put("building", building);
            params.put("phone", phoneNumber);

            return requestHandler.sendPostRequest(URLs.URL_LOCATION, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(LocationForm.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LocationForm.this, CheckOut.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}