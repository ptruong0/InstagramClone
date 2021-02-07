package com.example.instagramclone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 20;

    //private RecyclerView rvPosts;
    //private List<Post> postList;
    //private PostAdapter adapter;
    private ImageButton btnNewPost;
    private ImageButton btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rvPosts = findViewById(R.id.rvPosts);
        btnNewPost = findViewById(R.id.btnNewPost);
        btnAccount = findViewById(R.id.btnAccount);
        //postList = new ArrayList<>();


        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(i);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AccountActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        queryPosts();
        //adapter = new PostAdapter(MainActivity.this, postList);
        //rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //rvPosts.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar ab = getSupportActionBar();
        ab.setIcon(R.drawable.icon);
        return super.onCreateOptionsMenu(menu);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post. class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts " + e, e);
                }
                else {for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                    //postList.add(post);
                }}

            }
        });
    }




}