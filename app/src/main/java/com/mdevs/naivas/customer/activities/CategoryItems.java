package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.adapters.ProductsAdapter;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.productmanager.GroceryDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mdevs.naivas.customer.CustomerActivity.EXTRA_CATEGORY;

public class CategoryItems extends AppCompatActivity {

    RecyclerView catProducts;
    TextView name;
    String itemType;
    ProductsAdapter catItemsAdapter;
    ArrayList<GroceryDetails> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        name = findViewById(R.id.itemCategory);
        catProducts = findViewById(R.id.cat);

        Intent intent = getIntent();
        itemType = intent.getStringExtra(EXTRA_CATEGORY);
        name.setText(itemType);

        fetchItems();
    }

    public void fetchItems() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        catProducts.setLayoutManager(gridLayoutManager);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL, response -> {
            try {
                JSONArray j = new JSONArray(response);
                for (int i = 0; i < j.length(); i++) {
                    try {
                        JSONObject productDetails = j.getJSONObject(i);

                        String id = productDetails.getString("id");
                        String image = productDetails.getString("image");
                        String name = productDetails.getString("productName");
                        String price = productDetails.getString("price");
                        String category = productDetails.getString("category");
                        String quantity = productDetails.getString("quantity");

                        details.add(new GroceryDetails(id, image, name, price,category,quantity));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    catItemsAdapter = new ProductsAdapter(CategoryItems.this, details);
                    catProducts.setAdapter(catItemsAdapter);
                    //catItemsAdapter.setItemClickListener(CategoryItems.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(CategoryItems.this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_category", itemType);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}