package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Post.class);


        // set applicationId, client key, and server based on values in Heroku settings
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("OVIfw62EMsA6AoBTUEgbF3c8V5xnYZyC3YUbalym")
                .clientKey("mYRD8aMngI3o4Yq7JipLWm1f2y4NrKKhfSYzjDdj")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
