package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedBackForm extends AppCompatActivity {
    Button send;
    AutoCompleteTextView recipient;
    EditText title, message;
    int id = PrefManager.getInstance(this).UserID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);

        send = findViewById(R.id.send);
        recipient = findViewById(R.id.recipients);
        title = findViewById(R.id.messageTitle);
        message = findViewById(R.id.feedBackData);

        ArrayList<String> managers = new ArrayList<>();
        managers.add("Finance Manager");
        managers.add("Dispatch Manager");


        ArrayAdapter<String> admins = new ArrayAdapter<>(this, R.layout.feedback_menu_design, managers);
        recipient.setAdapter(admins);

        send.setOnClickListener(v -> {
            sendMessage();
        });
    }

    private void sendMessage() {
        String user_id, receiver, topic, feedbackMessage;
        user_id = String.valueOf(id);
        receiver = recipient.getText().toString().trim();
        feedbackMessage = message.getText().toString().trim();
        topic = title.getText().toString().trim();

        //validations

        if (TextUtils.isEmpty(topic)) {
            title.setError("Cannot be Blank!");
            title.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(feedbackMessage)) {
            message.setError("Cannot be Blank!");
            message.requestFocus();
            return;
        }

        FeedBackAsync feedBackAsync = new FeedBackAsync(receiver, topic, feedbackMessage, user_id);
        feedBackAsync.execute();
    }

    public class FeedBackAsync extends AsyncTask<Void, Void, String> {
        String receiver, title, message, sender;

        public FeedBackAsync(String receiver, String title, String message, String sender) {
            this.receiver = receiver;
            this.title = title;
            this.message = message;
            this.sender = sender;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("receiver", receiver);
            params.put("title", title);
            params.put("message", message);
            params.put("user_id", sender);

            return requestHandler.sendPostRequest(URLs.SEND_FEEDBACK, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject getResponse = new JSONObject(s);

                if (!getResponse.getBoolean("error")) {
                    Toast.makeText(FeedBackForm.this, getResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), FeedBack.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getResponse.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}