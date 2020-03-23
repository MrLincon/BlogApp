package com.whitespace.blogapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.whitespace.blogapp.R;

public class FeedAdapter extends FirestoreRecyclerAdapter<Feed, FeedAdapter.FeedHolder> {

    private OnItemClickListener listener;
    private Context mContext;
    private String date;

    public FeedAdapter(@NonNull FirestoreRecyclerOptions<Feed> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FeedHolder holder, int position, @NonNull Feed model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDesc());
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_layout,
                parent, false);
        return new FeedHolder(view);
    }

    class FeedHolder extends RecyclerView.ViewHolder {
        TextView title;
        ExpandableTextView description;

        public FeedHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = (ExpandableTextView) itemView.findViewById(R.id.description);

            mContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
