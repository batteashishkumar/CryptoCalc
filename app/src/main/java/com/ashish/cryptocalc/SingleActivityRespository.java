package com.ashish.cryptocalc;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class SingleActivityRespository {
    private MutableLiveData<Vector<DayData>> mdayDataVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataWeekVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataMonthVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataYearVector=new MutableLiveData<>();
    Vector<DayData> dayDataVector=new Vector<>();
    Vector<DayData> dayDataWeekVector=new Vector<>();
    Vector<DayData> dayDataMonthVector=new Vector<>();
    Vector<DayData> dayDataYearVector=new Vector<>();
    private final int Intraday=100;

    public SingleActivityRespository(MutableLiveData<Vector<DayData>> mdayDataVector, MutableLiveData<Vector<DayData>> mdayDataWeekVector, MutableLiveData<Vector<DayData>> mdayDataMonthVector, MutableLiveData<Vector<DayData>> mdayDataYearVector) {
        this.mdayDataVector=mdayDataVector;
        this.mdayDataWeekVector=mdayDataWeekVector;
        this.mdayDataMonthVector=mdayDataMonthVector;
        this.mdayDataYearVector=mdayDataYearVector;
    }

    public void callRApi(String coinSelected,Context context) {
        new Thread(){
            @Override
            public void run() {
                AlphaVantageApiNew alphaVantageApiNew=new AlphaVantageApiNew(context);
                alphaVantageApiNew.apiCall("https://www.alphavantage.co/query?function=CRYPTO_INTRADAY&symbol=" + coinSelected + "&market=USD&interval=5min&outputsize=full&apikey=5R38LO0QVKSML0P7", new ResponseListenerNew() {
                    @Override
                    public void onResponseReceived(JSONObject jsonObject) throws Exception {
                        parse(jsonObject,Intraday);
                    }
                });
                alphaVantageApiNew.apiCall("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=" + coinSelected + "&market=USD&apikey=5R38LO0QVKSML0P7", new ResponseListenerNew() {
                    @Override
                    public void onResponseReceived(JSONObject jsonObject) throws Exception {
                        parse(jsonObject,200);
                    }
                });
            }
        }.start();
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
                mdayDataVector.postValue(dayDataVector);
                mdayDataWeekVector.postValue(dayDataWeekVector);
                mdayDataMonthVector.postValue(dayDataMonthVector);
                mdayDataYearVector.postValue(dayDataYearVector);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
