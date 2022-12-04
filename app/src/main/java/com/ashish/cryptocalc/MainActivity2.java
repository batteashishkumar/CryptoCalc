package com.ashish.cryptocalc;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    BarChart barChart;
    ArrayList<String> xAxisLabel=null;
    TextView textView=null;
    TextView tv_amount,textView2,editTextNumber1,tv_TotalCoins,tv_Riskamount,tv_cardMoneyRisk,tv_SellingFee,tv_cardTotalRiskAmount,tv_cardMoneyProfit,tv_ProfitSellingFee;
    LineChart graphchart=null;
    TextView touchValue,touchValue2;
    ProgressBar graphchartProgressBar=null;
    ResponseListener responseListener=null;
    Vector<DayData> dayDataVector=new Vector<>();
    Vector<DayData> dayDataWeekVector=new Vector<>();
    Vector<DayData> dayDataMonthVector=new Vector<>();
    Vector<DayData> dayDataYearVector=new Vector<>();
    TextView i1D,i1W,i1M,i1Y;
    private boolean is1stclick1D=true;
    private boolean is1stclick1W=true;
    private boolean is1stclick1M=true;
    private boolean is1stclick1Y=true;
    Spinner spinner;
    private static final String[] paths = {"Bitcoin","Ethereum", "DogeCoin"};
    private static final String[] pathsKeys = {"BTC","ETH", "DOGE"};
    private static String coinSelected="BTC";
    InputData inputData=new InputData();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        inputData= (InputData) getIntent().getExtras().getSerializable("InputData");
        initializeControls();
//        ApplicationInfo.FLAG_SYSTEM
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        startProgressBar();
        calculateParams(inputData);
        responseListener= (dayDataVector, dayDataWeekVector, dayDataMonthVector, dayDataYearVector) -> {
            try {
                if (dayDataVector.size()==0|| dayDataWeekVector.size()==0||dayDataMonthVector.size()==0||dayDataYearVector.size()==0){
                    Toast.makeText(getApplicationContext(), "Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day. Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.", Toast.LENGTH_LONG).show();//{"Note":"Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day. Please visit https:\/\/www.alphavantage.co\/premium\/ if you would like to target a higher API call frequency."}
                }else {
                    setParsedData(dayDataVector, dayDataWeekVector, dayDataMonthVector, dayDataYearVector);
                    stopProgressBar();
                    i1D.callOnClick();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Some Exception", Toast.LENGTH_LONG).show();
            }
        };
        graphchart.getXAxis().setDrawGridLines(false);
        graphchart.getAxisLeft().setDrawGridLines(false);
        graphchart.getAxisRight().setDrawGridLines(false);
        graphchart.getXAxis().setDrawAxisLine(false);
        graphchart.getXAxis().setDrawLabels(false);
        graphchart.getDescription().setText(" ");
        graphchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof CandleEntry) {
                    try {
                        CandleEntry ce = (CandleEntry) e;
                        SimpleDateFormat spf= new SimpleDateFormat("yyyy-mm-dd");
                        SimpleDateFormat spf1= new SimpleDateFormat("dd MM");
                        Date Date=spf.parse(""+ce.getData());
                        String date1=spf1.format(Date);
                        touchValue.setText("$ " + ce.getHigh());
                        touchValue2.setText(""+date1);
                    } catch (Exception parseException) {
                        parseException.printStackTrace();
                    }

                } else {

                    try {
                        SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat spf1= new SimpleDateFormat("dd MMM yy | hh:mm aa");
                        Date Date=spf.parse(""+e.getData());
                        String date1=spf1.format(Date);
                        touchValue.setText("$ " + e.getY());
                        touchValue2.setText(""+date1);
                    } catch (Exception parseException) {
                        try {
                            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat spf1= new SimpleDateFormat("dd MMM yy");
                            Date Date=spf.parse(""+e.getData());
                            String date1=spf1.format(Date);
                            touchValue.setText("$ " + e.getY());
                            touchValue2.setText(""+date1);
                        }catch (Exception ex){

                        }

                    }

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        textView =findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initializeControls() {
        touchValue=findViewById(R.id.touchValue);
        touchValue2=findViewById(R.id.touchValue2);
        barChart=findViewById(R.id.barchart);
        graphchart=findViewById(R.id.graphchart);
        graphchartProgressBar=findViewById(R.id.graphchartProgressBar);
        tv_amount=findViewById(R.id.tv_amount);
        textView2=findViewById(R.id.textView2);
        editTextNumber1=findViewById(R.id.editTextNumber1);
        tv_TotalCoins=findViewById(R.id.tv_TotalCoins);
        tv_Riskamount=findViewById(R.id.tv_Riskamount);
        tv_cardMoneyRisk=findViewById(R.id.tv_cardMoneyRisk);
        tv_SellingFee=findViewById(R.id.tv_SellingFee);
        tv_cardTotalRiskAmount=findViewById(R.id.tv_cardTotalRiskAmount);
        tv_cardMoneyProfit=findViewById(R.id.tv_cardMoneyProfit);
        tv_ProfitSellingFee=findViewById(R.id.tv_ProfitSellingFee);
        spinner = (Spinner)findViewById(R.id.spinner);
        i1D=findViewById(R.id.i1D);
        i1W=findViewById(R.id.i1W);
        i1M=findViewById(R.id.i1M);
        i1Y=findViewById(R.id.i1Y);
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
    }

    private void calculateParams(InputData inputData) {
        OutputData outputData=new OutputData();
        outputData.Investment=inputData.Investment;
        outputData.CoinsPurchased= (float) Math.floor(inputData.Investment/inputData.RealtimePrice);
        outputData.BuyingFee=inputData.BuyingFee;
        outputData.CoinsPurchasedinclBF=(float) Math.floor(inputData.Investment/(inputData.RealtimePrice+inputData.BuyingFee));
        outputData.RiskAmount=(float) Math.floor((inputData.Investment*(inputData.RiskPercent/100)));
        outputData.WorseRiskAmount=(float) Math.floor(inputData.Investment-((inputData.Investment*(inputData.RiskPercent/100))));
        outputData.SellingFeeForRisk=inputData.SellingFee;
        outputData.TotalAmountwithSF=outputData.WorseRiskAmount+outputData.SellingFeeForRisk;
        outputData.ProfitAmount=inputData.SellRate-inputData.Investment;
        outputData.ProfitAmountwithSF=(inputData.SellRate+inputData.SellingFee)-inputData.Investment;
        setOutputToUI(outputData);
    }

    private void setOutputToUI(OutputData outputData) {
        tv_amount.setText(""+outputData.Investment);
        textView2.setText(""+outputData.CoinsPurchased);
        editTextNumber1.setText(""+outputData.BuyingFee);
        tv_TotalCoins.setText(""+outputData.CoinsPurchasedinclBF);
        tv_Riskamount.setText(""+outputData.RiskAmount);
        tv_cardMoneyRisk.setText(""+outputData.WorseRiskAmount);
        tv_SellingFee.setText(""+outputData.SellingFeeForRisk);
        tv_cardTotalRiskAmount.setText(""+outputData.TotalAmountwithSF);
        tv_cardMoneyProfit.setText(""+outputData.ProfitAmount);
        tv_ProfitSellingFee.setText(""+outputData.ProfitAmountwithSF);
    }

    private void startProgressBar() {
        graphchart.setVisibility(View.GONE);
        graphchartProgressBar.setVisibility(View.VISIBLE);
    }
    private void stopProgressBar() {
        graphchart.setVisibility(View.VISIBLE);
        graphchartProgressBar.setVisibility(View.GONE);
    }

    public void callApi(){
        new Thread(){
            @Override
            public void run() {
                AlphaVantageApi alphaVantageApi=new AlphaVantageApi(getApplicationContext(),
                        "https://www.alphavantage.co/query?function=CRYPTO_INTRADAY&symbol="+coinSelected+"&market=USD&interval=5min&outputsize=full&apikey=5R38LO0QVKSML0P7",
                        "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol="+coinSelected+"&market=USD&apikey=5R38LO0QVKSML0P7",responseListener);
                alphaVantageApi.apiCall();
            }
        }.start();
    }

    public void setParsedData(Vector<DayData> dayDataVector,Vector<DayData> dayDataWeekVector,Vector<DayData> dayDataMonthVector,Vector<DayData> dayDataYearVector) {
        this.dayDataVector=dayDataVector;
        this.dayDataWeekVector=dayDataWeekVector;
        this.dayDataMonthVector=dayDataMonthVector;
        this.dayDataYearVector=dayDataYearVector;
    }
    private void setData(Vector<DayData> dataVector) {
        if(is1stclick1W||is1stclick1M||is1stclick1Y){
            graphchart.animateX(dataVector.size()*15);
        }

        ArrayList<Entry> dailydata=new ArrayList<>();
        for(int i=0;i<dataVector.size();i++){
            float val=Float.parseFloat(dataVector.get(i).closing);
            dailydata.add(new Entry(i,val,dataVector.get(i).date));
        }

        LineDataSet set1;
        set1=new LineDataSet(dailydata,coinSelected);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setColor(getResources().getColor(R.color.purple_200));
        set1.setLineWidth(2);
        LineData data=new LineData(set1);
        set1.setFillColor(getResources().getColor(R.color.purple_200));
        set1.setHighLightColor(getResources().getColor(R.color.purple_200));
        graphchart.setTouchEnabled(true);
        graphchart.setMarker(new MyMarkerView(this,R.layout.marker));
        graphchart.setData(data);
        graphchart.notifyDataSetChanged();
        graphchart.invalidate();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                coinSelected=pathsKeys[0];
                startProgressBar();
                is1stclick1W=true;
                is1stclick1M=true;
                is1stclick1Y=true;
                callApi();
                break;
            case 1:
                coinSelected=pathsKeys[1];
                startProgressBar();
                is1stclick1W=true;
                is1stclick1M=true;
                is1stclick1Y=true;
                callApi();
                break;
            case 2:
                coinSelected=pathsKeys[2];
                startProgressBar();
                is1stclick1W=true;
                is1stclick1M=true;
                is1stclick1Y=true;
                callApi();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class MyMarkerView extends MarkerView {

        private TextView tvContent,tvInterval;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            tvContent = (TextView) findViewById(R.id.tvContent);
            tvInterval = (TextView) findViewById(R.id.tvInterval);
        }
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            if (e instanceof CandleEntry) {
                try {
                    CandleEntry ce = (CandleEntry) e;
                    SimpleDateFormat spf= new SimpleDateFormat("yyyy-mm-dd");
                    SimpleDateFormat spf1= new SimpleDateFormat("dd MM");
                    Date Date=spf.parse(""+ce.getData());
                    String date1=spf1.format(Date);
                    tvContent.setText("$ " + ce.getHigh());
                    tvInterval.setText(date1+"");
                } catch (Exception parseException) {
                    parseException.printStackTrace();
                }

            } else {

                try {
                    SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat spf1= new SimpleDateFormat("dd MMM yy | hh:mm aa");
                    Date Date=spf.parse(""+e.getData());
                    String date1=spf1.format(Date);
                    tvContent.setText("$ " + e.getY());
                    tvInterval.setText(""+date1);
                } catch (Exception parseException) {
                    try {
                        SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat spf1= new SimpleDateFormat("dd MMM yy");
                        Date Date=spf.parse(""+e.getData());
                        String date1=spf1.format(Date);
                        tvContent.setText("$ " + e.getY());
                        tvInterval.setText(""+date1);
                    }catch (Exception ex){

                    }

                }

            }

            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    public void click1D(View view){
        i1D.setBackground(getResources().getDrawable(R.drawable.curveedittext));
        i1W.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1M.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1Y.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
        setData(dayDataVector);
        is1stclick1D=false;
    }
    public void click1W(View view){
        i1D.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1W.setBackground(getResources().getDrawable(R.drawable.curveedittext));
        i1M.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1Y.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
        setData(dayDataWeekVector);
        is1stclick1W=false;
    }
    public void click1M(View view){
        i1D.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1W.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1M.setBackground(getResources().getDrawable(R.drawable.curveedittext));
        i1Y.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
        setData(dayDataMonthVector);
        is1stclick1M=false;
    }
    public void click1Y(View view){
        i1D.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1W.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1M.setBackground(getResources().getDrawable(R.drawable.curveedittextwhite));
        i1Y.setBackground(getResources().getDrawable(R.drawable.curveedittext));
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
        setData(dayDataYearVector);
        is1stclick1Y=false;
    }

}