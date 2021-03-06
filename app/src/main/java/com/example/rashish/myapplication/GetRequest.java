package com.example.rashish.myapplication;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by rashish on 14/09/16.
 */
public class GetRequest{
    Request request;
    public GetRequest(String url, Request.ResponseHandler responseHandler,Context context) {
        this.request = new Request(url,responseHandler,context);
    }
    public void getResponse(JSONObject params){
        request.getResponse(com.android.volley.Request.Method.GET, params);
    }
    public void cancelRequest(){
        request.cancelRequest();
    }
}