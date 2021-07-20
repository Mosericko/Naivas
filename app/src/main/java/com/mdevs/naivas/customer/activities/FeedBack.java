package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.FeedBackAdapter;
import com.mdevs.naivas.customer.classes.FeedBackData;
import com.mdevs.naivas.databases.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedBack extends AppCompatActivity implements FeedBackAdapter.MessageClickListener {
    RelativeLayout sendMessage;
    RecyclerView feedBackRV;
    ArrayList<FeedBackData> feedBackDataArrayList = new ArrayList<>();
    FeedBackAdapter feedBackAdapter;
    int id = PrefManager.getInstance(this).UserID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        sendMessage = findViewById(R.id.sendMessage);
        feedBackRV = findViewById(R.id.feedBackRv);

        sendMessage.setOnClickListener(v -> {
            startActivity(new Intent(FeedBack.this, FeedBackForm.class));
        });


        getFeedBackData();
    }

    private void getFeedBackData() {

        feedBackRV.setHasFixedSize(true);
        feedBackRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String url = "http://android.officialm-devs.com/naivas/android/feedback?user_id=" + id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    String receiver = jsonObject.getString("receiver");
                    String title = jsonObject.getString("title");
                    String message = jsonObject.getString("message");
                    String dateTime = jsonObject.getString("sent_date");
                    String replyM = jsonObject.getString("reply");
                    String replyDate = jsonObject.getString("replied_date");

                    feedBackDataArrayList.add(new FeedBackData(receiver,title, message, dateTime, replyM, replyDate));
                }
                feedBackAdapter = new FeedBackAdapter(this, feedBackDataArrayList);
                feedBackRV.setAdapter(feedBackAdapter);
                feedBackAdapter.setOnItemClickListener(FeedBack.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(FeedBack.this, FeedBackReply.class);
        FeedBackData feedBackData = feedBackDataArrayList.get(position);

        intent.putExtra("title", feedBackData.getTitle());
        intent.putExtra("message", feedBackData.getMessage());
        intent.putExtra("datetime", feedBackData.getDateTime());
        intent.putExtra("replyDate", feedBackData.getReplyDate());
        intent.putExtra("reply", feedBackData.getReplyMessage());

        startActivity(intent);

    }
}