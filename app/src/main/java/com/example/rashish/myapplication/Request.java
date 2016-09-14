package com.example.rashish.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rashish on 14/09/16.
 */
public class Request {
    public interface ResponseHandler {
        void onSuccess(JSONObject response);
        void onError(Exception exception);
    }
    private final String TAG = Request.class.getSimpleName();
    private final String mUrl;
    private final RequestQueue requestQueue;
    private final String requestTag;
    private final ResponseHandler mResponseHandler;


    public Request(String url, ResponseHandler responseHandler,Context context) {
        this.mUrl = url;
        this.requestTag = String.format("{uid=%s}", url);
        this.requestQueue = ((MyApplication)context).getRequestQueue();
        this.mResponseHandler = responseHandler;
    }

    public void getResponse(int methodType,JSONObject params) {
        Log.d(TAG, "url : " + mUrl+"params : "+params.toString());
        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(methodType,mUrl,params,getResponseListener(),getErrorListener());
            jsonRequest.setTag(requestTag);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);
        } catch (Exception e) {
            mResponseHandler.onError(e);
        }
    }
    public void cancelRequest() {
        requestQueue.cancelAll(requestTag);
    }
    private Response.ErrorListener getErrorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mResponseHandler.onError(error);
            }
        };
    }
    private Response.Listener<JSONObject> getResponseListener(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response",response.toString());
                    mResponseHandler.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    mResponseHandler.onError(e);
                }
            }
        };
    }
}