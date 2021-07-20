package com.mdevs.naivas.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.activities.Cart;
import com.mdevs.naivas.customer.activities.CategoryItems;
import com.mdevs.naivas.customer.activities.MyProfile;
import com.mdevs.naivas.customer.activities.ProductDetails;
import com.mdevs.naivas.customer.adapters.CategoryAdapter;
import com.mdevs.naivas.customer.adapters.ProductsAdapter;
import com.mdevs.naivas.customer.classes.CategoryDetails;
import com.mdevs.naivas.databases.DatabaseHandler;
import com.mdevs.naivas.databases.PrefManager;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.helperclasses.User;
import com.mdevs.naivas.productmanager.GroceryDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity implements ProductsAdapter.ClickListener, CategoryAdapter.ItemClickListener {

    public static final String EXTRA_CATEGORY = "category";
    RecyclerView categoryRV, products;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryDetails> categList = new ArrayList<>();
    TextView firstName;
    ArrayList<GroceryDetails> productList = new ArrayList<>();
    ProductsAdapter productsAdapter;
    RelativeLayout profile;
    DatabaseHandler myDb;
    int id;
    FloatingActionButton cartFab;

    //Intent constants
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String CATEGORY = "category";
    public static final String IMAGE = "image";
    public static final String QUANTITY = "quantity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        firstName = findViewById(R.id.firstName);
        products = findViewById(R.id.productsList);
        categoryRV = findViewById(R.id.categoryRV);
        profile = findViewById(R.id.profile);
        cartFab = findViewById(R.id.cartFab);

        cartFab.setOnClickListener(v -> {
            startActivity(new Intent(CustomerActivity.this, Cart.class));
        });

        profile.setOnClickListener(v -> {
            startActivity(new Intent(CustomerActivity.this, MyProfile.class));
        });

        listCategories();

        listAllProducts();

        displayProfile();


    }

    private void displayProfile() {
        id = PrefManager.getInstance(this).UserID();
        myDb = new DatabaseHandler(this);
        User user = myDb.getUser(id);

        firstName.setText(user.getFirstname());
    }

    private void listCategories() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        categoryRV.setLayoutManager(gridLayoutManager);

        // add the static files into the arrayList
        categList.add(new CategoryDetails(1, "Fruits", R.drawable.ic_groceries));
        categList.add(new CategoryDetails(2, "Vegetables", R.drawable.ic_veges));
        categList.add(new CategoryDetails(3, "Cereals", R.drawable.ic_cereals));
        categList.add(new CategoryDetails(4, "Spices", R.drawable.ic_spices));


        categoryAdapter = new CategoryAdapter(this, categList);
        categoryRV.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(CustomerActivity.this);
    }

    private void listAllProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        products.setLayoutManager(gridLayoutManager);

        //volley library
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest getJsonArray = new JsonArrayRequest(Request.Method.GET, URLs.URL_FETCH_PRODUCTS, null, response -> {

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


                    productList.add(new GroceryDetails(id, image, name, price, category, quantity));
                }

                productsAdapter = new ProductsAdapter(CustomerActivity.this, productList);
                products.setAdapter(productsAdapter);
                productsAdapter.setOnItemClickListener(CustomerActivity.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(getJsonArray);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, ProductDetails.class);
        GroceryDetails groceryDetails = productList.get(position);

        intent.putExtra(ID,groceryDetails.getId());
        intent.putExtra(NAME,groceryDetails.getName());
        intent.putExtra(PRICE,groceryDetails.getPrice());
        intent.putExtra(CATEGORY,groceryDetails.getCategory());
        intent.putExtra(IMAGE,groceryDetails.getImage());
        intent.putExtra(QUANTITY,groceryDetails.getQuantity());

        startActivity(intent);
    }

    @Override
    public void onItemCLick(int position) {
        Intent intent = new Intent(this, CategoryItems.class);
        CategoryDetails clickedItem = categList.get(position);

        intent.putExtra(EXTRA_CATEGORY, clickedItem.getName());
        startActivity(intent);
    }
}