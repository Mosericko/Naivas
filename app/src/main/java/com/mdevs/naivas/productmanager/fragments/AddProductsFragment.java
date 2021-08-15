package com.mdevs.naivas.productmanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.productmanager.ProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddProductsFragment extends Fragment {
    ImageView selectPhoto;
    EditText prodName,prodQuantity,prodPrice;
    AutoCompleteTextView categGrocery;
    RadioGroup category;
    Button saveProducts;
    Context context;
    Bitmap bitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String imageString;
    private final int galleryReqCode = 1;
    private final int cameraReqCode = 2;
    private Uri fileTrace;
    RadioButton selectedCat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_products,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context= this.getContext();
        selectPhoto= view.findViewById(R.id.choose_product_image);
        prodName= view.findViewById(R.id.product_name);
        prodQuantity= view.findViewById(R.id.prodQuantity);
        prodPrice= view.findViewById(R.id.prodPrice);
        saveProducts= view.findViewById(R.id.addGrocery);
       /* category= view.findViewById(R.id.prodCategory);*/
        categGrocery = view.findViewById(R.id.categGrocery);

        byteArrayOutputStream = new ByteArrayOutputStream();

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        saveProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailsToDb();
            }
        });

        ArrayList<String> groceryList = new ArrayList<>();
        groceryList.add("Vegetables");
        groceryList.add("Fruits");
        groceryList.add("Spices");
        groceryList.add("Cereals");

        ArrayAdapter<String> groceryAdapter = new ArrayAdapter<>(context, R.layout.drop_down_menu_design, groceryList);
        categGrocery.setAdapter(groceryAdapter);
    }

    private void showOptionsDialog() {
        AlertDialog.Builder optionsDialog = new AlertDialog.Builder(context);
        String[] options = {"Capture Image", "Choose Existing Image"};

        optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhoto();
                        break;

                    case 1:
                        choosePhoto();
                        break;
                }
            }
        });

        optionsDialog.show();
    }

    private void choosePhoto() {

        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(gallery, galleryReqCode);
    }

    private void takePhoto() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cameraReqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryReqCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            fileTrace = data.getData();


            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filepath);

                Glide.with(this)
                        .load(bitmap)
                        .into(selectPhoto);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == cameraReqCode && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            Glide.with(this)
                    .load(bitmap)
                    .into(selectPhoto);


        }
    }

    private void sendDetailsToDb() {

        /*int selectedCategory= category.getCheckedRadioButtonId();
        selectedCat = requireView().findViewById(selectedCategory);*/
        final String productName = prodName.getText().toString().trim();
        final String productQuantity = prodQuantity.getText().toString().trim();
        final String productPrice = prodPrice.getText().toString().trim();
        final String catRadio = categGrocery.getText().toString().trim();


        if (fileTrace == null) {
            Toast.makeText(context, "Select a Photo!", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        if (TextUtils.isEmpty(productName)) {
            prodName.setError("Please Enter Product name!");
            prodName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(productQuantity)) {
            prodQuantity.setError("Please Enter Product Color!");
            prodQuantity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(productPrice)) {
            prodPrice.setError("Please Enter Product Price!");
            prodPrice.requestFocus();
            return;
        }

        SendDetailsAsync sendDetails= new SendDetailsAsync(imageString, productName,productQuantity,productPrice,catRadio);
        sendDetails.execute();
    }


    public class SendDetailsAsync extends AsyncTask<Void,Void,String> {

        String imageStr,name,quantity,price,category;

        public SendDetailsAsync(String imageStr,String name, String quantity, String price, String category) {
            this.imageStr = imageStr;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.category = category;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler= new RequestHandler();

            HashMap<String,String> params = new HashMap<>();
            params.put("image",imageStr);
            params.put("productName",name);
            params.put("quantity",quantity);
            params.put("price",price);
            params.put("category",category);

            return requestHandler.sendPostRequest(URLs.URL_ADD_PRODUCTS,params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject obj = new JSONObject(s);

                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, ProductActivity.class));
                requireActivity().finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
