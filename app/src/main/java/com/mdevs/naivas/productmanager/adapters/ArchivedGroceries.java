package com.mdevs.naivas.productmanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

public class ArchivedGroceries extends RecyclerView.Adapter<ArchivedGroceries.ArchivedVH> {

    Context context;
    ArrayList<GroceryDetails> archivedGList;

    public ArchivedGroceries(Context context, ArrayList<GroceryDetails> archivedGList) {
        this.context = context;
        this.archivedGList = archivedGList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ArchivedVH onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_card, parent, false);
        return new ArchivedVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ArchivedGroceries.ArchivedVH holder, int position) {

        GroceryDetails groceryDetails = archivedGList.get(position);

        Glide.with(context)
                .load(groceryDetails.getImage())
                .into(holder.image);

        holder.name.setText(groceryDetails.getName());
        holder.price.setText(groceryDetails.getPrice());
        holder.category.setText(groceryDetails.getCategory());
    }

    @Override
    public int getItemCount() {
        return archivedGList.size();
    }

    public class ArchivedVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, category;

        public ArchivedVH(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
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
                    .setMessage("Proceed to Unarchive?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            archiveId();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(itemView.getContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.show();
        }

        private void archiveId() {
            int i;
            String id;
            i = getAdapterPosition();
            GroceryDetails groceryDetails = archivedGList.get(i);
            id = groceryDetails.getId();

            //execute async task
            ArchiveAsync archiveAsync = new ArchiveAsync(id);
            archiveAsync.execute();

            archivedGList.remove(i);
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
                param.put("unarchive", id);

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
