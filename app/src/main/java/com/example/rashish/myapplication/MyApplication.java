package com.example.rashish.myapplication;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by rashish on 14/09/16.
 */
public class MyApplication extends Application {
    RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
    }
    public RequestQueue getRequestQueue(){
        return this.requestQueue;
    }
}