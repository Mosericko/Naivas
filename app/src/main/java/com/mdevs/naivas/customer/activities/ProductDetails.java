package com.mdevs.naivas.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.CustomerActivity;
import com.mdevs.naivas.customer.classes.CartDetails;
import com.mdevs.naivas.databases.DatabaseHandler;

import static com.mdevs.naivas.customer.CustomerActivity.CATEGORY;
import static com.mdevs.naivas.customer.CustomerActivity.ID;
import static com.mdevs.naivas.customer.CustomerActivity.IMAGE;
import static com.mdevs.naivas.customer.CustomerActivity.NAME;
import static com.mdevs.naivas.customer.CustomerActivity.PRICE;
import static com.mdevs.naivas.customer.CustomerActivity.QUANTITY;

public class ProductDetails extends AppCompatActivity {

    TextView name, price, category, quantity;
    ImageView image;
    Button addToCart;
    LinearLayout add, subtract;
    String totalPrice;
    int number = 1;
    DatabaseHandler myDb;

    String idIn, nameIn, priceIn, categoryIn, imageIn, quantityIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        name = findViewById(R.id.prod_name);
        price = findViewById(R.id.prod_price);
        category = findViewById(R.id.detail_category);
        image = findViewById(R.id.prod_image);
        addToCart = findViewById(R.id.addToCart);
        add = findViewById(R.id.layoutInc);
        subtract = findViewById(R.id.layoutDec);
        quantity = findViewById(R.id.modalQuantity);

        Intent intent = getIntent();
        idIn = intent.getStringExtra(ID);
        nameIn = intent.getStringExtra(NAME);
        priceIn = intent.getStringExtra(PRICE);
        categoryIn = intent.getStringExtra(CATEGORY);
        imageIn = intent.getStringExtra(IMAGE);
        quantityIn = intent.getStringExtra(QUANTITY);


        myDb = new DatabaseHandler(this);

        Glide.with(this)
                .load(imageIn)
                .into(image);
        name.setText(nameIn);
        price.setText(priceIn);
        category.setText(categoryIn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number < Integer.parseInt(quantityIn)) {
                    number++;
                    quantity.setText(String.valueOf(number));

                } else {
                    Toast.makeText(ProductDetails.this, "Quantity Cannot Exceed Stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == 1) {
                    quantity.setText(String.valueOf(number));
                    Toast.makeText(ProductDetails.this, "Buy at least 1 item", Toast.LENGTH_SHORT).show();

                } else {
                    number -= 1;
                    quantity.setText(String.valueOf(number));
                }
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedQuantity = quantity.getText().toString().trim();
                totalPrice = String.valueOf(Integer.parseInt(selectedQuantity) * Integer.parseInt(priceIn));
                CartDetails cartDetails = new CartDetails(idIn, nameIn, categoryIn, priceIn, imageIn, selectedQuantity, quantityIn, totalPrice);

                if (myDb.checkIfRowExists(cartDetails)) {
                    Toast.makeText(ProductDetails.this, "Item already Added to Cart!", Toast.LENGTH_SHORT).show();
                } else {

                    myDb.addToCart(cartDetails);

                    startActivity(new Intent(ProductDetails.this, CustomerActivity.class));
                    Toast.makeText(ProductDetails.this, "Successfully Added  to the Cart", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
}