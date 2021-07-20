package com.mdevs.naivas.signactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MoreDetails extends AppCompatActivity {

    String firstName, lastName, email, password, phoneNumber;
    Button createAccount;
    RadioGroup gender, userType;
    TextView user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        user_login = findViewById(R.id.user_login);
        createAccount = findViewById(R.id.register);
        gender = findViewById(R.id.genderChoice);
        userType = findViewById(R.id.userCategory);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreDetails.this, SignIn.class));
            }
        });

    }

    private void registerUser() {
        String typeUser;
        Intent receiveIntentData = getIntent();
        firstName = receiveIntentData.getStringExtra("firstName");
        lastName = receiveIntentData.getStringExtra("lastName");
        email = receiveIntentData.getStringExtra("email");
        password = receiveIntentData.getStringExtra("password");
        phoneNumber = receiveIntentData.getStringExtra("phoneNumber");

        final String genderRadio = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString().trim();
        final String userCategory = ((RadioButton) findViewById(userType.getCheckedRadioButtonId())).getText().toString().trim();

        typeUser = null;
        switch (userCategory) {

            case "Customer":
                typeUser = "1";
                break;

            case "Products Manager":
                typeUser = "2";
                break;
            case "Finance Manager":
                typeUser = "3";
                break;
            case "Dispatch Manager":
                typeUser = "4";
                break;
            case "Driver":
                typeUser = "5";
                break;


        }

        RegisterUser registerUser = new RegisterUser(firstName, lastName, email, password, phoneNumber, genderRadio, typeUser);
        registerUser.execute();
    }

    private class RegisterUser extends AsyncTask<Void, Void, String> {

        private final String firstName;
        private final String lastName;
        private final String email;
        private final String password;
        private final String phoneNumber;
        private final String gender;
        private final String typeUser;

        public RegisterUser(String firstName, String lastName, String email, String password, String phoneNumber, String gender, String typeUser) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.gender = gender;
            this.typeUser = typeUser;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("gender", gender);
            params.put("email", email);
            params.put("password", password);
            params.put("phone", phoneNumber);
            params.put("userType", typeUser);

            //returning the response
            return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("SignUp", "sign up : " + s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MoreDetails.this, SignIn.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}