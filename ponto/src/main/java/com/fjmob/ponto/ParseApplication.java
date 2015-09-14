package com.fjmob.ponto;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(getApplicationContext(), "8H9p0T97aRXHtPSmTfbuhsEuQN7YfWjotPNHvbex", "x0cINJqs9qXDepLjHkJizmpBzuPsh41E9lCK9lBG");
    }
  }