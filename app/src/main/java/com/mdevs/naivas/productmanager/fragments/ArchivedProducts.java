package com.mdevs.naivas.productmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.productmanager.GroceryDetails;
import com.mdevs.naivas.productmanager.adapters.ArchivedGroceries;
import com.mdevs.naivas.productmanager.adapters.GroceryListAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArchivedProducts extends Fragment {

    RecyclerView archivedList;
    ArrayList<GroceryDetails> archivedGrocery = new ArrayList<>();
    ArchivedGroceries archivedGroceries;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archived_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getContext();

        archivedList = view.findViewById(R.id.archivedGroceryList);

        listProducts();

    }

    private void listProducts() {

        archivedList.setHasFixedSize(true);
        archivedList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        //volley library
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest getJsonArray = new JsonArrayRequest(Request.Method.GET, URLs.FETCH_ARCHIVED_PRODUCTS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    //loop through the event details array
                    for (int i = 0; i < response.length(); i++) {

                        //get the current Json Object
                        JSONObject productDetails = response.getJSONObject(i);

                        String id = productDetails.getString("id");
                        String image = productDetails.getString("image");
                        String name = productDetails.getString("productName");
                        String price = productDetails.getString("price");
                        String category = productDetails.getString("category");
                        String quantity = productDetails.getString("quantity");


                        archivedGrocery.add(new GroceryDetails(id, image, name, price, category, quantity));
                    }

                    archivedGroceries = new ArchivedGroceries(context, archivedGrocery);
                    archivedList.setAdapter(archivedGroceries);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(getJsonArray);
    }
}
