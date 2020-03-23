package com.whitespace.blogapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.whitespace.blogapp.Adapters.Feed;
import com.whitespace.blogapp.Adapters.FeedAdapter;
import com.whitespace.blogapp.Adapters.FeedRecyclerDecoration;
import com.whitespace.blogapp.Authentication.LoginActivity;
import com.whitespace.blogapp.Class.ThemeSettings;
import com.whitespace.blogapp.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    private FeedAdapter adapter;

    private FirebaseFirestore db;
    private CollectionReference post;

    ThemeSettings themeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme Settings
        themeSettings = new ThemeSettings(this);
        if (themeSettings.loadNightModeState() == false) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        //...............
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Blog App");

        recyclerView = findViewById(R.id.recyclerview);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.bottomPadding);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        recyclerView.addItemDecoration(new FeedRecyclerDecoration(topPadding, sidePadding, bottomPadding));

        db = FirebaseFirestore.getInstance();
        post = db.collection("Post");

        //Navigation drawer layout

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view_left);

        NavigationItems();


        //........................


        Query query = post.orderBy("timestamp", Query.Direction.DESCENDING);
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

            }
        });

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.dark_mode_toggle); // This is the menu item that contains your switch
        Switch drawerSwitch = (Switch) menuItem.getActionView().findViewById(R.id.darkModeSwitch);
        drawerSwitch.setClickable(false);
        if (themeSettings.loadNightModeState() == false) {
            drawerSwitch.setChecked(false);
        } else {
            drawerSwitch.setChecked(true);
        }


    }

    public void NavigationItems() {

        navigationView.setCheckedItem(R.id.feed);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.feed:

                        break;
                    case R.id.post:
                        Intent post = new Intent(MainActivity.this, PostActivity.class);
                        startActivity(post);
                        break;
                    case R.id.my_post:
                        Intent my_post = new Intent(MainActivity.this, MyPostActivity.class);
                        startActivity(my_post);
                        break;
                    case R.id.dark_mode_toggle:
                        if (themeSettings.loadNightModeState() == false) {
                            themeSettings.setNightModeState(true);
                            restartApp();   //Recreate activity
                        } else {
                            themeSettings.setNightModeState(false);
                            restartApp();   //Recreate activity
                        }
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent signOut = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(signOut);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    //Recreate activity

    private void restartApp() {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //...............

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.feed);
    }
}
