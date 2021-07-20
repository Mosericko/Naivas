package com.mdevs.naivas.signactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.CustomerActivity;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.distributionmanager.TransportActivity;
import com.mdevs.naivas.driver.DriverActivity;
import com.mdevs.naivas.financemanager.FinanceActivity;
import com.mdevs.naivas.productmanager.ProductActivity;

public class IntroPrompt extends AppCompatActivity {
    Button createAccount;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_prompt);

        createAccount = findViewById(R.id.signUpBtn);
        logIn = findViewById(R.id.signInBtn);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroPrompt.this, SignUp.class));
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroPrompt.this, SignIn.class));
            }
        });

        if (PrefManager.getInstance(this).isLoggedIn()) {
            this.finish();

            switch (PrefManager.getInstance(this).UserType()) {
                case "1":
                    startActivity(new Intent(IntroPrompt.this, CustomerActivity.class));
                    finish();
                    break;
                case "2":
                    startActivity(new Intent(IntroPrompt.this, ProductActivity.class));
                    finish();
                    break;
                case "3":
                    startActivity(new Intent(IntroPrompt.this, FinanceActivity.class));
                    finish();
                    break;
                case "4":
                    startActivity(new Intent(IntroPrompt.this, TransportActivity.class));
                    finish();
                    break;
                case "5":
                    startActivity(new Intent(IntroPrompt.this, DriverActivity.class));
                    finish();
                    break;
            }
        }
    }
}