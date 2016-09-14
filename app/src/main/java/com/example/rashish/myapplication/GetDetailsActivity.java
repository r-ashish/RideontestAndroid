package com.example.rashish.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rashish.myapplication.Config.Server;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetDetailsActivity extends AppCompatActivity {

    EditText userIdEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        userIdEditText = (EditText) findViewById(R.id.userId);
        toolbar.setTitle("Get details");
        setSupportActionBar(toolbar);
    }

    public void getDetails(View view){
        String name = userIdEditText.getText().toString();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setMessage("Getting data...");
        dialog.show();
        JSONObject data = new JSONObject();
        try{
            data.put("userId", name);
        }catch (Exception e){
            e.printStackTrace();
        }
        PostRequest loginRequest = new PostRequest(Server.API_URL_SAVE_DETAILS, getResponseHandler(dialog), getApplicationContext());
        loginRequest.getResponse(data);
    }

    private Request.ResponseHandler getResponseHandler(final ProgressDialog dialog){
        return new Request.ResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                dialog.dismiss();
                try{
                    if(response.getString("message").equals("userFound")) {
                        showDetailsDialog(response);
                    }else if(response.getString("message").equals("userNotFound")){
                        Utils.showSnackBar(findViewById(android.R.id.content),"Invalid userId!", Snackbar.LENGTH_SHORT);
                    }else if(response.getString("message").equals("emailAlreadyRegistered")){
                        Utils.showSnackBar(findViewById(android.R.id.content),"The email you've entered is already registered!", Snackbar.LENGTH_SHORT);
                    }else {
                        Utils.showSnackBar(findViewById(android.R.id.content),"Unable to submit, try again later!", Snackbar.LENGTH_SHORT);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception exception) {
                dialog.dismiss();
                exception.printStackTrace();
            }
        };
    }

    private void showDetailsDialog(JSONObject response){
        try{
            JSONObject userDetails = response.getJSONObject("user");
            String userId = userDetails.getString("userId");
            String name = userDetails.getString("name");
            String phone = userDetails.getString("phone");
            String email = userDetails.getString("email");
            String address = userDetails.getString("address");

            showDialog(userId, name, phone, email, address);
        }catch (Exception e){

        }
    }


    private void showDialog(String userId, String name, String phone, String email, String address){
        final Dialog dialog = new Dialog(GetDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_details);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        TextView userIdTextView = (TextView) findViewById(R.id.userId);
        TextView nameTextView = (TextView) findViewById(R.id.name);
        TextView emailTextView = (TextView) findViewById(R.id.email);
        TextView phoneTextView = (TextView) findViewById(R.id.phone);
        TextView addressTextView = (TextView) findViewById(R.id.address);

        userIdTextView.setText(userIdTextView.getText() + userId);
        nameTextView.setText(nameTextView.getText() + name);
        emailTextView.setText(emailTextView.getText() + email);
        phoneTextView.setText(phoneTextView.getText() + phone);
        addressTextView.setText(addressTextView.getText() + address);

        //lp.gravity = Gravity.TOP;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;



        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
