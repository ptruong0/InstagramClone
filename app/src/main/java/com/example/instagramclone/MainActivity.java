package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 20;

    private RecyclerView rvPosts;
    private Button btnNewPost;
    private Button btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPosts = findViewById(R.id.rvPosts);
        btnNewPost = findViewById(R.id.btnNewPost);
        btnAccount = findViewById(R.id.btnAccount);

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
                startActivity(i);
            }
        });

        queryPosts();
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data ) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String description = data.getExtras().getString("description");
            String photoFileName = data.getExtras().getString("photoFileName");
            File photoFile = getPhotoFileUri(photoFileName);

            Uri photoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
            Bitmap resizedPhoto = BitmapScaler.scaleToFitWidth(BitmapFactory.decodeFile(photoUri.getPath()), 40);
            // Configure byte output stream
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // Compress the image further
            resizedPhoto.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
            File resizedFile = getPhotoFileUri(photoFileName + "_resized");
            try {
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);
            // Write the bytes of the bitmap to file
            fos.write(bytes.toByteArray());
            fos.close();}
            catch (Exception e) {
                Log.e(TAG, "File resizing error", e);
            }

            Log.i(TAG, "description: " + description);
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(description, currentUser, resizedFile);
        }
    }*/

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post. class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        Log.i(TAG, "here");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving!", e);
                    Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful");
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}