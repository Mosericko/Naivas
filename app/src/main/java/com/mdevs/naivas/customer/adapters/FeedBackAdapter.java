package com.mdevs.naivas.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.naivas.R;
import com.mdevs.naivas.customer.classes.FeedBackData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedBackVH> {

    Context context;
    ArrayList<FeedBackData> feedBackList;
    private MessageClickListener messageClickListener;

    public FeedBackAdapter(Context context, ArrayList<FeedBackData> feedBackList) {
        this.context = context;
        this.feedBackList = feedBackList;
    }


    public void setOnItemClickListener(MessageClickListener itemClickListener) {
        messageClickListener = itemClickListener;
    }


    public interface MessageClickListener {
        void onClick(int position);
    }

    @NonNull
    @NotNull
    @Override
    public FeedBackVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feedback_card, parent, false);
        return new FeedBackVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FeedBackAdapter.FeedBackVH holder, int position) {
        FeedBackData feedBackData = feedBackList.get(position);

        holder.title.setText(feedBackData.getTitle());
        holder.dateTime.setText(feedBackData.getDateTime());
        holder.feedbackMessage.setText(feedBackData.getMessage());

    }

    @Override
    public int getItemCount() {
        return feedBackList.size();
    }

    public class FeedBackVH extends RecyclerView.ViewHolder {
        TextView title, dateTime, feedbackMessage;

        public FeedBackVH(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.message_title);
            dateTime = itemView.findViewById(R.id.dateTime);
            feedbackMessage = itemView.findViewById(R.id.actual_message);

            itemView.setOnClickListener(v -> {
                if (messageClickListener != null) {
                    //get the position of the adapter

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        messageClickListener.onClick(position);
                    }
                }
            });
        }
    }
}
