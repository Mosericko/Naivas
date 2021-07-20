package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.naivas.R;

public class FeedBackReply extends AppCompatActivity {
    String replyDateIn, titleIn, messageIn, dateIn, replyMessageIn;

    TextView title, message, dateTime, reply, replyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_reply);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        dateTime = findViewById(R.id.dateTime);
        reply = findViewById(R.id.replyMessage);
        replyDate = findViewById(R.id.replyDateTime);

        Intent intent = getIntent();
        replyDateIn = intent.getStringExtra("replyDate");
        titleIn = intent.getStringExtra("title");
        messageIn = intent.getStringExtra("message");
        dateIn = intent.getStringExtra("datetime");
        replyMessageIn = intent.getStringExtra("reply");

        title.setText(titleIn);
        message.setText(messageIn);
        dateTime.setText(dateIn);

        reply.setText(replyMessageIn);
        replyDate.setText(replyDateIn);
    }
}