package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.distributionmanager.activities.DispatchMessages;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Reply extends AppCompatActivity {

    TextView title, message, dateTime;
    String idIn, titleIn, messageIn, dateIn, replyMessageIn;
    EditText reply;
    Button sendReply;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        dateTime = findViewById(R.id.dateTime);
        myId = PrefManager.getInstance(this).UserType();

        reply = findViewById(R.id.replyData);
        sendReply = findViewById(R.id.sendReply);

        Intent intent = getIntent();
        idIn = intent.getStringExtra("id");
        titleIn = intent.getStringExtra("title");
        messageIn = intent.getStringExtra("message");
        dateIn = intent.getStringExtra("date");
        replyMessageIn = intent.getStringExtra("reply");

        title.setText(titleIn);
        message.setText(messageIn);
        dateTime.setText(dateIn);

        reply.setText(replyMessageIn);

        sendReply.setOnClickListener(v -> {
            sendFeedBackReply();
        });


    }

    private void sendFeedBackReply() {
        String data = reply.getText().toString().trim();

        SendAsync sendAsync = new SendAsync(data, idIn);
        sendAsync.execute();


    }

    public class SendAsync extends AsyncTask<Void, Void, String> {

        String data, id;

        public SendAsync(String data, String id) {
            this.data = data;
            this.id = id;
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("id", id);
            params.put("reply", data);
            return requestHandler.sendPostRequest(URLs.SEND_FEEDBACK, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(Reply.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    switch (myId) {
                        case "3":
                            startActivity(new Intent(Reply.this, Messages.class));
                            finish();
                            break;
                        case "4":
                            startActivity(new Intent(Reply.this, DispatchMessages.class));
                            finish();
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}