package com.whitespace.blogapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.whitespace.blogapp.R;

public class EditPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    EditText title, description;
    FloatingActionButton post;

    private String userID;
    private DocumentReference document_ref;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String POST_ID,TITLE,DESCRIPTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);


        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Edit Post");

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        post = findViewById(R.id.post);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                title.onEditorAction(EditorInfo.IME_ACTION_DONE);
                description.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        final Intent intent = getIntent();
        POST_ID = intent.getStringExtra(MyPostActivity.EXTRA_POST_ID);
        TITLE = intent.getStringExtra(MyPostActivity.EXTRA_TITLE);
        DESCRIPTION = intent.getStringExtra(MyPostActivity.EXTRA_DESCRIPTION);

        title.setText(TITLE);
        description.setText(DESCRIPTION);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        document_ref = db.collection("Post").document(POST_ID);

//        userID = mAuth.getUid();
    }

    private void saveData() {
        final String Title = title.getText().toString();
        final String Description = description.getText().toString();

        if (Title.isEmpty()) {
            title.setError("All field must be filled");
            return;
        }
        if (Description.isEmpty()) {
            description.setError("All field must be filled");
            return;
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(EditPostActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();

            document_ref.update("title", Title,
                    "desc", Description).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            Intent save = new Intent(EditPostActivity.this, MainActivity.class);
            startActivity(save);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_post_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_post:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                builder.setTitle("Are you sure?")
                        .setMessage("If you delete this, this will no longer be shown in the home page!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                document_ref.delete();
                                Intent intent = new Intent(EditPostActivity.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
