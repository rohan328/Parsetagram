package com.rohan.parsetagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("sWKKZSewWJPoZrFZtgk3Ga0qgdGAOTqLhT5ucfI5")
                .clientKey("UgdD2fiDfxMoCt4pPEXwq5QkHS0SZok2fw8ctJ7n")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
