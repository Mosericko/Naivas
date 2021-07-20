package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.helperclasses.User;
import com.mdevs.naivas.signactivity.IntroPrompt;

public class FinanceProfile extends AppCompatActivity {
    int id;
    TextView firstName, lastName, email, phone, gender, usertype;
    DatabaseHandler myDb;
    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_profile);

        myDb = new DatabaseHandler(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_phone);
        gender = findViewById(R.id.profile_gender);
        usertype = findViewById(R.id.profile_usertype);
        signOut = findViewById(R.id.logOut);

        signOut.setOnClickListener(v -> {
            PrefManager.getInstance(this).logout();
            Intent intent = new Intent(FinanceProfile.this, IntroPrompt.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        displayUserInformation();
    }

    private void displayUserInformation() {
        id = PrefManager.getInstance(this).UserID();

        User user = myDb.getUser(id);
        String userCategory = user.getUsertype();

        firstName.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        email.setText(user.getEmail());
        phone.setText(user.getPhonenumber());
        gender.setText(user.getGender());

        switch (userCategory) {
            case "1":
                usertype.setText(R.string.usertype1);
                break;
            case "2":
                usertype.setText(R.string.usertype2);
                break;
            case "3":
                usertype.setText(R.string.usertype3);
                break;
            case "4":
                usertype.setText(R.string.usertype4);
                break;
            case "5":
                usertype.setText(R.string.usertype5);
                break;
        }


    }
}