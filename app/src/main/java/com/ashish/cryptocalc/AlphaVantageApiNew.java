package com.ashish.cryptocalc;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AlphaVantageApiNew {
    Context context=null;
    String url="";
    ResponseListenerNew responseListenernew=null;
    Vector<DayData> dayDataVector=new Vector<>();
    Vector<DayData> dayDataWeekVector=new Vector<>();
    Vector<DayData> dayDataMonthVector=new Vector<>();
    Vector<DayData> dayDataYearVector=new Vector<>();
    JSONObject jsonObject=new JSONObject();
    JSONObject jsonObjectDays=new JSONObject();
    RequestQueue requestQueue=null;
    final static int Intraday=100;
    final static int Days=200;

    public AlphaVantageApiNew(Context context){
        this.context=context;
        requestQueue = Volley.newRequestQueue(context);
    }



    public void apiCall(String url,ResponseListenerNew responseListenernew)
    {
        try {
            JSONObject jsonBody = new JSONObject();
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        jsonObject=new JSONObject(response);
                        responseListenernew.onResponseReceived(jsonObject);
                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {


                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    return new HashMap<String, String>();
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new HashMap<String, String>();
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
