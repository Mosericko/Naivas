package com.mdevs.naivas.signactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mdevs.naivas.MainActivity;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.CustomerActivity;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.distributionmanager.TransportActivity;
import com.mdevs.naivas.driver.DriverActivity;
import com.mdevs.naivas.financemanager.FinanceActivity;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.helperclasses.User;
import com.mdevs.naivas.productmanager.ProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    Button login;
    EditText email,password;
    TextView redirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginPass);
        login = findViewById(R.id.login);
        redirect= findViewById(R.id.signUpRedirect);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });
    }

    private void userLogin() {
        final String loginEmail, loginPass;
        loginEmail = email.getText().toString().trim();
        loginPass = password.getText().toString().trim();

        String email_pattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "gmail.com$";

        TextInputLayout eAddress = findViewById(R.id.mailTil);
        TextInputLayout passLayout = findViewById(R.id.loginPassTil);


        if (TextUtils.isEmpty(loginEmail)) {
            eAddress.setError("Please enter your email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            eAddress.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (!email.getText().toString().matches(email_pattern)) {
            eAddress.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (TextUtils.isEmpty(loginPass)) {
            passLayout.setError("Enter your password!");
            password.requestFocus();
            return;
        } else {
            passLayout.setError(null);
        }

        UserSignIn user_login = new UserSignIn(loginEmail, loginPass);
        user_login.execute();


    }


    class UserSignIn extends AsyncTask<Void, Void, String> {

        String email, password;

        UserSignIn(String email, String password) {
            this.email = email;
            this.password = password;
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            //returning the response
            return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
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

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    String typeUser = userJson.getString("userType");

                    //creating a new user object
                    User user = new User(
                            userJson.getInt("id"),
                            userJson.getString("firstName"),
                            userJson.getString("lastName"),
                            userJson.getString("gender"),
                            userJson.getString("email"),
                            userJson.getString("phone"),
                            userJson.getString("userType")

                    );

                    //storing the user in shared preferences
                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);
                    //finish();

                    DatabaseHandler myDb = new DatabaseHandler(SignIn.this);

                    //storing User to SQLite DataBase
                    myDb.addUser(user);

                    switch (typeUser) {
                        case "1":
                            startActivity(new Intent(SignIn.this, CustomerActivity.class));
                            finish();
                            break;
                        case "2":
                            startActivity(new Intent(SignIn.this, ProductActivity.class));
                            finish();
                            break;
                        case "3":
                            startActivity(new Intent(SignIn.this, FinanceActivity.class));
                            finish();
                            break;
                        case "4":
                            startActivity(new Intent(SignIn.this, TransportActivity.class));
                            finish();
                            break;
                        case "5":
                            startActivity(new Intent(SignIn.this, DriverActivity.class));
                            finish();
                            break;
                    }


                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}