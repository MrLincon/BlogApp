package com.whitespace.blogapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.whitespace.blogapp.Adapters.Feed;
import com.whitespace.blogapp.Adapters.FeedAdapter;
import com.whitespace.blogapp.Adapters.FeedRecyclerDecoration;
import com.whitespace.blogapp.R;

public class MyPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    RecyclerView recyclerView;
    private FeedAdapter adapter;

    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference post;

    public static final String EXTRA_POST_ID = "com.example.firebaseprofile.EXTRA_POST_ID";
    public static final String EXTRA_TITLE = "com.example.firebaseprofile.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.firebaseprofile.EXTRA_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText("My Posts");

        recyclerView = findViewById(R.id.recyclerview);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.bottomPadding);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        recyclerView.addItemDecoration(new FeedRecyclerDecoration(topPadding, sidePadding, bottomPadding));

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
        post = db.collection("Post");

        Query query = post.whereEqualTo("user_id",userID)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Feed> options = new FirestoreRecyclerOptions.Builder<Feed>()
                .setQuery(query, Feed.class)
                .build();
        adapter = new FeedAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                Feed feed = documentSnapshot.toObject(Feed.class);

                String id = documentSnapshot.getId();
                String title = feed.getTitle();
                String description = feed.getDesc();

                Intent intent = new Intent(MyPostActivity.this, EditPostActivity.class);
                intent.putExtra(EXTRA_POST_ID,id);
                intent.putExtra(EXTRA_TITLE,title);
                intent.putExtra(EXTRA_DESCRIPTION,description);
                startActivity(intent);
            }
        });

    }
}
