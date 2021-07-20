package com.mdevs.naivas.productmanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdevs.naivas.R;
import com.mdevs.naivas.helperclasses.RequestHandler;
import com.mdevs.naivas.helperclasses.URLs;
import com.mdevs.naivas.productmanager.GroceryDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {
    Context context;
    ArrayList<GroceryDetails> groceryArrayList;

    public GroceryListAdapter(Context context, ArrayList<GroceryDetails> groceryArrayList) {
        this.context = context;
        this.groceryArrayList = groceryArrayList;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_card, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        GroceryDetails groceryDetails = groceryArrayList.get(position);
        /*Glide.with(context)
                .load(Base64.decode(groceryDetails.getImage(),Base64.DEFAULT))
                .into(holder.image);*/
        Glide.with(context)
                .load(groceryDetails.getImage())
                .into(holder.image);

        holder.name.setText(groceryDetails.getName());
        holder.price.setText(groceryDetails.getPrice());
        holder.category.setText(groceryDetails.getCategory());

    }

    @Override
    public int getItemCount() {

        return groceryArrayList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, category;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.groceryImage);
            name = itemView.findViewById(R.id.groceryName);
            price = itemView.findViewById(R.id.groceryPrice);
            category = itemView.findViewById(R.id.groceryCategory);

            itemView.setOnClickListener(v -> {
                openDialog();
            });
        }

        private void openDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Caution")
                    .setMessage("Are You Sure You Want to Archive?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                          archiveId();
                            //Toast.makeText(itemView.getContext(), "Okay!!", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(itemView.getContext(), "Canceled!!", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.show();
        }

        private void archiveId() {
            int i;
            String id;
            i = getAdapterPosition();
            GroceryDetails groceryDetails = groceryArrayList.get(i);
            id = groceryDetails.getId();

            //execute async task
            ArchiveAsync archiveAsync = new ArchiveAsync(id);
            archiveAsync.execute();

            groceryArrayList.remove(i);
            notifyItemRemoved(i);
//            notifyItemRangeRemoved(i,);


        }

        class ArchiveAsync extends AsyncTask<Void, Void, String> {
            String id;

            public ArchiveAsync(String id) {
                this.id = id;
            }


            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> param = new HashMap<>();
                param.put("archive", id);

                return requestHandler.sendPostRequest(URLs.URL_ARCHIVE_PRODUCT, param);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {

                    JSONObject obj = new JSONObject(s);

                    Toast.makeText(itemView.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
