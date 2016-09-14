package com.example.rashish.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.rashish.myapplication.Config.Server;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameEditText = (EditText)findViewById(R.id.name);
        emailEditText = (EditText)findViewById(R.id.email);
        phoneEditText = (EditText)findViewById(R.id.phone);
        phoneEditText = (EditText)findViewById(R.id.phone);
        addressEditText = (EditText)findViewById(R.id.address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Submit details");
        setSupportActionBar(toolbar);
    }

    public void submit(View view){
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        if(!validateInput(name.toLowerCase(), email.toLowerCase(), phone.toLowerCase())){
            return;
        }
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setMessage("Submitting data...");
        dialog.show();
        JSONObject data = new JSONObject();
        try{
            data.put("name", name);
            data.put("email", email);
            data.put("phone", phone);
            data.put("address", address);
        }catch (Exception e){
            e.printStackTrace();
        }
        PostRequest loginRequest = new PostRequest(Server.API_URL_SAVE_DETAILS, getResponseHandler(dialog), getApplicationContext());
        loginRequest.getResponse(data);
        closeKeyBoard();
    }

    private void closeKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean validateInput(String name, String email, String phone) {
        boolean isValid = true;
        View focusView = null;
        if(!isNameValid(name)) {
            isValid = false;
            focusView = nameEditText;
        }
        if(!isEmailValid(email)) {
            isValid = false;
            focusView = emailEditText;
        }
        if(!isPhoneValid(phone)) {
            isValid = false;
            focusView = phoneEditText;
        }

        if(!isValid) {
            focusView.requestFocus();
        }
        return isValid;
    }

    private boolean isNameValid(String name) {
        if(TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return false;
        }
        if(!name.matches("^[a-zA-Z\\s]+")) {
            nameEditText.setError("Enter a valid name");
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        if(TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }
        if(!email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")) {
            emailEditText.setError("Invalid email");
            return false;
        }
        return true;
    }

    private boolean isPhoneValid(String phone) {
        if(TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone no. is required");
            return false;
        }
        if(!phone.matches("^[0-9]*$")) {
            phoneEditText.setError("Invalid phone number");
            return false;
        }
        return true;
    }

    private Request.ResponseHandler getResponseHandler(final ProgressDialog dialog){
        return new Request.ResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                dialog.dismiss();
                try{
                    if(response.getString("message").equals("detailsSaved")) {
                        Utils.showSnackBar(findViewById(android.R.id.content),"Details saved successfully!", Snackbar.LENGTH_SHORT);
                    }else if(response.getString("message").equals("phoneAlreadyRegistered")){
                        Utils.showSnackBar(findViewById(android.R.id.content),"The phone number you've entered is already registered!", Snackbar.LENGTH_SHORT);
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

}
