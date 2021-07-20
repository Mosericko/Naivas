package com.mdevs.naivas.customer.activities;

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
import com.mdevs.naivas.databases.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    RecyclerView feedBackRV;
    ArrayList<FeedBackData> feedBackDataArrayList = new ArrayList<>();
    FeedBackAdapter feedBackAdapter;
    int id = PrefManager.getInstance(this).UserID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        feedBackRV = findViewById(R.id.feedBackRv);

        getFeedBackData();
    }

    private void getFeedBackData() {

        feedBackRV.setHasFixedSize(true);
        feedBackRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String url = "https://android.officialm-devs.com/naivas/android/notifications?user_id=" + id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String message = jsonObject.getString("message");
                    String dateTime = jsonObject.getString("date_created");


                    feedBackDataArrayList.add(new FeedBackData(title, message, dateTime));
                }
                feedBackAdapter = new FeedBackAdapter(this, feedBackDataArrayList);
                feedBackRV.setAdapter(feedBackAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(jsonArrayRequest);
    }
}