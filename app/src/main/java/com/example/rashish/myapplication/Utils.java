package com.example.rashish.myapplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rashish on 14/09/16.
 */
public class Utils {
    public static void showSnackBar(View view, String message, int len){
        Snackbar snackbar = Snackbar.make(view,message,len);
        snackbar.show();
    }
}