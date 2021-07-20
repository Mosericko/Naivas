package com.mdevs.naivas.signactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.mdevs.naivas.R;

public class SignUp extends AppCompatActivity {
    Button nextPage;
    EditText firstName,lastName,email,password,confirmPassword,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nextPage=findViewById(R.id.nextDetails);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPass);
        phoneNumber = findViewById(R.id.phoneNumber);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });
    }
    private void validateDetails() {

        final String firstJina = firstName.getText().toString().trim();
        final String lastJina = lastName.getText().toString().trim();
        final String gmail = email.getText().toString().trim();
        final String passCode = password.getText().toString().trim();
        final String confirmPass = confirmPassword.getText().toString().trim();
        final String mobileNum = phoneNumber.getText().toString().trim();


        //email and phoneNumber Validations

        String mobile_pattern = "^07[0-9]{8}$";
        String email_pattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "gmail.com$";

        TextInputLayout fname = findViewById(R.id.fName_til);
        TextInputLayout lname = findViewById(R.id.lName_til);
        TextInputLayout mail = findViewById(R.id.emailTil);
        TextInputLayout pass = findViewById(R.id.passTil);
        TextInputLayout phoneNum = findViewById(R.id.numberTil);
        TextInputLayout confirm = findViewById(R.id.passConfirmTill);

        //Validate inputs

        if (TextUtils.isEmpty(firstJina)) {
            fname.setError("Please enter your first name!");
            firstName.requestFocus();
            return;
        } else {
            fname.setError(null);
        }

        if (TextUtils.isEmpty(lastJina)) {
            lname.setError("Please enter your last name!");
            lastName.requestFocus();
            return;
        } else {
            lname.setError(null);
        }

        if (TextUtils.isEmpty(gmail)) {
            mail.setError("Please enter your email address!");
            email.requestFocus();
            return;
        } else {
            mail.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(gmail).matches()) {
            mail.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            mail.setError(null);
        }

        if (!email.getText().toString().matches(email_pattern)) {
            mail.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            mail.setError(null);
        }

        if (passCode.length() < 5) {
            pass.setError("Password cannot be less than 5 characters");
            password.requestFocus();
            return;
        } else {
            pass.setError(null);
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirm.setError("Passwords Do not Match!!");
            confirmPassword.requestFocus();
            return;
        } else {
            confirm.setError(null);
        }

        if (mobileNum.length() < 10) {
            phoneNum.setError("Phone number cannot be less than 10 digits");
            phoneNumber.requestFocus();
            return;
        } else {
            phoneNum.setError(null);
        }
        if (!phoneNumber.getText().toString().matches(mobile_pattern)) {
            phoneNum.setError("Enter a valid phone number");
            phoneNumber.requestFocus();
            return;
        } else {
            phoneNum.setError(null);
        }
        if (!android.util.Patterns.PHONE.matcher(mobileNum).matches()) {
            phoneNum.setError("Enter a valid phone number");
            phoneNumber.requestFocus();
            return;
        } else {
            phoneNum.setError(null);
        }

        if (TextUtils.isEmpty(mobileNum)) {
            phoneNum.setError("Please enter your phone number!");
            phoneNumber.requestFocus();
            return;
        } else {
            phoneNum.setError(null);
        }

        //if it passes all validations

        Intent intentData = new Intent(SignUp.this, MoreDetails.class);
        intentData.putExtra("firstName", firstJina);
        intentData.putExtra("lastName", lastJina);
        intentData.putExtra("email", gmail);
        intentData.putExtra("password", passCode);
        intentData.putExtra("phoneNumber", mobileNum);


        startActivity(intentData);


    }
}