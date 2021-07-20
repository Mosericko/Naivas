package com.mdevs.naivas.financemanager.activities;

import android.content.Intent;
import android.os.Bundle;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Messages extends AppCompatActivity implements FeedBackAdapter.MessageClickListener {
    RecyclerView feedBackRV;
    ArrayList<FeedBackData> feedBackDataArrayList = new ArrayList<>();
    FeedBackAdapter feedBackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        feedBackRV = findViewById(R.id.feedBackRv);

        getFeedBackData();
    }

    private void getFeedBackData() {

        feedBackRV.setHasFixedSize(true);
        feedBackRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String url = "http://android.officialm-devs.com/naivas/android/feedback?finance=1";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String message = jsonObject.getString("message");
                    String dateTime = jsonObject.getString("sent_date");
                    String id = jsonObject.getString("id");
                    String replyM = jsonObject.getString("reply");

                    feedBackDataArrayList.add(new FeedBackData(title, message, dateTime, replyM, id));
                }
                feedBackAdapter = new FeedBackAdapter(this, feedBackDataArrayList);
                feedBackRV.setAdapter(feedBackAdapter);
                feedBackAdapter.setOnItemClickListener(Messages.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(Messages.this, Reply.class);
        FeedBackData feedBackData = feedBackDataArrayList.get(position);

        intent.putExtra("title", feedBackData.getTitle());
        intent.putExtra("message", feedBackData.getMessage());
        intent.putExtra("datetime", feedBackData.getDateTime());
        intent.putExtra("id", feedBackData.getId());
        intent.putExtra("reply", feedBackData.getReplyMessage());

        startActivity(intent);
    }
}