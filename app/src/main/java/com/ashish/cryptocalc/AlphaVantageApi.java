package com.ashish.cryptocalc;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AlphaVantageApi {

    Context context=null;
    String urlIntraday="";
    String urlDays="";
    ResponseListener responseListener=null;
    Vector<DayData> dayDataVector=new Vector<>();
    Vector<DayData> dayDataWeekVector=new Vector<>();
    Vector<DayData> dayDataMonthVector=new Vector<>();
    Vector<DayData> dayDataYearVector=new Vector<>();
    JSONObject jsonObjectIntraday=new JSONObject();
    JSONObject jsonObjectDays=new JSONObject();
    final static int Intraday=100;
    final static int Days=200;

    public AlphaVantageApi(Context context, String urlIntraday,String urlDays,ResponseListener responseListener){
        this.context=context;
        this.responseListener=responseListener;
        this.urlIntraday=urlIntraday;
        this.urlDays=urlDays;
    }

    public void apiCall()
    {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            final String requestBody = jsonBody.toString();

            StringRequest stringRequestIntraDay = new StringRequest(Request.Method.GET, urlIntraday, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        jsonObjectIntraday=new JSONObject(response);
                        parse(jsonObjectIntraday,Intraday);
                    }catch (Exception e){

                    }
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            StringRequest stringRequestDays = new StringRequest(Request.Method.GET, urlDays, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        jsonObjectDays=new JSONObject(response);
                        parse(jsonObjectDays, Days);
                    }catch (Exception e){

                    }
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequestIntraDay);
            requestQueue.add(stringRequestDays);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(JSONObject jsonObject, int from) {
        if (from==Intraday){
            try {
                dayDataVector=new Vector<>();
                JSONObject jsonObjectAllDay=jsonObject.getJSONObject("Time Series Crypto (5min)");
                JSONArray times = jsonObjectAllDay.names ();
                for (int i = 0; i < times.length () && i<300; i++) {
                    String dateNtime = times.getString(i);
                    String opening = jsonObjectAllDay.getJSONObject(dateNtime).get("1. open").toString();
                    String closing = jsonObjectAllDay.getJSONObject(dateNtime).get("4. close").toString();
                    DayData dayData = new DayData(dateNtime, opening, closing);
                    dayDataVector.add(dayData);
                    Collections.sort(dayDataVector, new Comparator<DayData>() {
                        @Override
                        public int compare(DayData d1, DayData d2) {
                            return (d1.date).compareTo(d2.date);
                        }
                    });
                }
            }catch (Exception e){

            }

        }else {
            try {
                dayDataWeekVector=new Vector<>();
                dayDataMonthVector=new Vector<>();
                dayDataYearVector=new Vector<>();
                JSONObject jsonObjectAllDates=jsonObject.getJSONObject("Time Series (Digital Currency Daily)");
                JSONArray dates = jsonObjectAllDates.names ();
                for (int i = 0; i < dates.length () && i<365; i++) {
                    String date = dates.getString(i);
                    String opening=jsonObjectAllDates.getJSONObject(date).get("1a. open (USD)").toString();
                    String closing=jsonObjectAllDates.getJSONObject(date).get("4a. close (USD)").toString();
                    DayData dayData=new DayData(date,opening,closing);
                    if (i<7){
                        dayDataWeekVector.add(dayData);
                        dayDataMonthVector.add(dayData);
                        dayDataYearVector.add(dayData);
                    }else if (i<30) {
                        dayDataMonthVector.add(dayData);
                        dayDataYearVector.add(dayData);
                    }else {
                        dayDataYearVector.add(dayData);
                    }

                }
                Collections.sort(dayDataWeekVector, new Comparator<DayData>() {
                    @Override
                    public int compare(DayData d1, DayData d2) {
                        return (d1.date).compareTo(d2.date);
                    }
                });
                Collections.sort(dayDataMonthVector, new Comparator<DayData>() {
                    @Override
                    public int compare(DayData d1, DayData d2) {
                        return (d1.date).compareTo(d2.date);
                    }
                });
                Collections.sort(dayDataYearVector, new Comparator<DayData>() {
                    @Override
                    public int compare(DayData d1, DayData d2) {
                        return (d1.date).compareTo(d2.date);
                    }
                });
                responseListener.onResponseReceived(dayDataVector,dayDataWeekVector,dayDataMonthVector,dayDataYearVector);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
