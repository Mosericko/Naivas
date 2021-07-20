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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.productmanager.GroceryDetails;
import com.mdevs.naivas.productmanager.adapters.GroceryListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    RecyclerView groceryRV;
    Context context;
    ArrayList<GroceryDetails> groceryArray = new ArrayList<>();
    GroceryListAdapter groceryListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getContext();
        groceryRV = view.findViewById(R.id.groceryList);

        listAllProducts();
    }

    private void listAllProducts() {
        groceryRV.setHasFixedSize(true);
        groceryRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        //volley library
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest getJsonArray = new JsonArrayRequest(Request.Method.GET, URLs.URL_FETCH_PRODUCTS, null, new Response.Listener<JSONArray>() {
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


                        groceryArray.add(new GroceryDetails(id, image, name, price, category, quantity));
                    }

                    groceryListAdapter = new GroceryListAdapter(context,groceryArray);
                    groceryRV.setAdapter(groceryListAdapter);
//                    groceryListAdapter.setOnItemClickListener(ProductDashboard.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);

        requestQueue.add(getJsonArray);
    }
}
