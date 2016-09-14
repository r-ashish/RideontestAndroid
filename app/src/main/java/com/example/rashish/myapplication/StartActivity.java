package com.example.rashish.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void submitDetails(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void getDetails(View view){
        startActivity(new Intent(this, GetDetailsActivity.class));
    }
}
