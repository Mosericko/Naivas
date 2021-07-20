package com.mdevs.naivas.productmanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.helperclasses.User;
import com.mdevs.naivas.signactivity.IntroPrompt;

public class ProfileFragment extends Fragment {
    Context context;
    TextView firstName,secondName,gender,phoneNum,email,userType;
    Button logout;
    DatabaseHandler db;
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getContext();
        firstName = view.findViewById(R.id.nameFirst);
        secondName = view.findViewById(R.id.nameSecond);
        gender = view.findViewById(R.id.gender);
        phoneNum = view.findViewById(R.id.phoneNum);
        email = view.findViewById(R.id.emailAddress);
        userType = view.findViewById(R.id.user);
        logout= view.findViewById(R.id.logout);
        
        displayDetails();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.getInstance(context).logout();
                Intent intent = new Intent(context, IntroPrompt.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void displayDetails() {
        db= new DatabaseHandler(context);
        id= PrefManager.getInstance(context).UserID();
        User user = db.getUser(id);

        firstName.setText(user.getFirstname());
        secondName.setText(user.getLastname());
        gender.setText(user.getGender());
        email.setText(user.getEmail());
        phoneNum.setText(user.getPhonenumber());
    }
}
