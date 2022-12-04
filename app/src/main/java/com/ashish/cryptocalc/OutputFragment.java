package com.ashish.cryptocalc;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Vector;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class OutputFragment extends Fragment  {

    private OutpuViewModel mViewModel;

    BarChart barChart;
    ArrayList<String> xAxisLabel=null;
    TextView textView=null;
    TextView tv_amount,textView2,editTextNumber1,tv_TotalCoins,tv_Riskamount,tv_cardMoneyRisk,tv_SellingFee,tv_cardTotalRiskAmount,tv_cardMoneyProfit,tv_ProfitSellingFee;
    LineChart graphchart=null;
    TextView touchValue,touchValue2;
    ProgressBar graphchartProgressBar=null;
    RecyclerView rv_tweets;
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
    Vector<TweetInfo> vecTweets=new Vector<>();
    Context context=null;
    SingleActivityViewModel singleActivityViewModel=null;
    public OutputFragment(SingleActivityViewModel singleActivityViewModel, FragmentInterface fragmentInterface, Context context) {
        this.context=context;
        this.singleActivityViewModel=singleActivityViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.outpu_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeControls(view);
        setObserves();
        setOutputToUI(singleActivityViewModel.getmOutputData().getValue());
        fetchDatafromTwitter();
    }

    private void fetchDatafromTwitter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setApplicationOnlyAuthEnabled(true)
                        .setOAuthConsumerKey("pRs4gHfN2i0mwN1XpkpcWn3N3")
                        .setOAuthConsumerSecret("MSeQpydQL2bb41ox3HcOAyR9KYgsSwMzVfOYKJ5xMvbfcZC7SC");
                OAuth2Token token = null;

                try {
                    token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                cb = new ConfigurationBuilder();
                cb.setApplicationOnlyAuthEnabled(true);
                cb.setOAuthConsumerKey("pRs4gHfN2i0mwN1XpkpcWn3N3");
                cb.setOAuthConsumerSecret("MSeQpydQL2bb41ox3HcOAyR9KYgsSwMzVfOYKJ5xMvbfcZC7SC");
                cb.setOAuth2TokenType(token.getTokenType());
                cb.setOAuth2AccessToken(token.getAccessToken());

                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();
                Query query = new Query("BitCoin");
                query.setCount(10);
                query.setLang("en");
                QueryResult result = null;
                try {
                    result = twitter.search(query);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                List<twitter4j.Status> tweets = result.getTweets();
                for (twitter4j.Status tweet:tweets){
                    TweetInfo tweetInfo=new TweetInfo();
                    tweetInfo.name=tweet.getUserMentionEntities()[0].getName();
                    tweetInfo.userId=tweet.getUserMentionEntities()[0].getScreenName();
                    tweetInfo.description=tweet.getText().replace(tweet.getUserMentionEntities()[0].getScreenName(),"");
                    tweetInfo.description=tweetInfo.description.replace("@","");
                    vecTweets.add(tweetInfo);
                }
            }
        }).start();



    }

    private void setObserves() {
        singleActivityViewModel.getDayDataVector().observe(this, new Observer<Vector<DayData>>() {
            @Override
            public void onChanged(Vector<DayData> dayData) {
                dayDataVector=dayData;
                i1D.callOnClick();
                stopProgressBar();
            }
        });
        singleActivityViewModel.getDayDataWeekVector().observe(this, new Observer<Vector<DayData>>() {
            @Override
            public void onChanged(Vector<DayData> dayData) {
                dayDataWeekVector=dayData;
                stopProgressBar();
            }
        });
        singleActivityViewModel.getDayDataMonthVector().observe(this, new Observer<Vector<DayData>>() {
            @Override
            public void onChanged(Vector<DayData> dayData) {
                dayDataMonthVector=dayData;
                stopProgressBar();
            }
        });
        singleActivityViewModel.getDayDataYearVector().observe(this, new Observer<Vector<DayData>>() {
            @Override
            public void onChanged(Vector<DayData> dayData) {
                dayDataYearVector=dayData;
                stopProgressBar();
            }
        });
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

    private void initializeControls(View view) {
        touchValue=view.findViewById(R.id.touchValue);
        touchValue2=view.findViewById(R.id.touchValue2);
        barChart=view.findViewById(R.id.barchart);
        graphchart=view.findViewById(R.id.graphchart);
        graphchartProgressBar=view.findViewById(R.id.graphchartProgressBar);
        tv_amount=view.findViewById(R.id.tv_amount);
        textView2=view.findViewById(R.id.textView2);
        editTextNumber1=view.findViewById(R.id.editTextNumber1);
        tv_TotalCoins=view.findViewById(R.id.tv_TotalCoins);
        tv_Riskamount=view.findViewById(R.id.tv_Riskamount);
        tv_cardMoneyRisk=view.findViewById(R.id.tv_cardMoneyRisk);
        tv_SellingFee=view.findViewById(R.id.tv_SellingFee);
        tv_cardTotalRiskAmount=view.findViewById(R.id.tv_cardTotalRiskAmount);
        tv_cardMoneyProfit=view.findViewById(R.id.tv_cardMoneyProfit);
        tv_ProfitSellingFee=view.findViewById(R.id.tv_ProfitSellingFee);
        spinner = (Spinner)view.findViewById(R.id.spinner);

        rv_tweets=view.findViewById(R.id.rv_tweets);
        TwitterAdapter twitterAdapter = new TwitterAdapter();
        rv_tweets.setHasFixedSize(true);
        rv_tweets.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_tweets.setAdapter(twitterAdapter);

        i1D=view.findViewById(R.id.i1D);
        i1W=view.findViewById(R.id.i1W);
        i1M=view.findViewById(R.id.i1M);
        i1Y=view.findViewById(R.id.i1Y);
        i1D.setPadding(15,15,15,15);
        i1W.setPadding(15,15,15,15);
        i1M.setPadding(15,15,15,15);
        i1Y.setPadding(15,15,15,15);
        i1D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        i1W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        i1M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        i1Y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        coinSelected=pathsKeys[0];
                        startProgressBar();
                        is1stclick1W=true;
                        is1stclick1M=true;
                        is1stclick1Y=true;
                        singleActivityViewModel.callApi(coinSelected,getContext());
                        break;
                    case 1:
                        coinSelected=pathsKeys[1];
                        startProgressBar();
                        is1stclick1W=true;
                        is1stclick1M=true;
                        is1stclick1Y=true;
                        singleActivityViewModel.callApi(coinSelected,getContext());
                        break;
                    case 2:
                        coinSelected=pathsKeys[2];
                        startProgressBar();
                        is1stclick1W=true;
                        is1stclick1M=true;
                        is1stclick1Y=true;
                        singleActivityViewModel.callApi(coinSelected,getContext());
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        startProgressBar();
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
        textView =view.findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void startProgressBar() {
        graphchart.setVisibility(View.GONE);
        graphchartProgressBar.setVisibility(View.VISIBLE);
    }
    private void stopProgressBar() {
        graphchart.setVisibility(View.VISIBLE);
        graphchartProgressBar.setVisibility(View.GONE);
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
        graphchart.setMarker(new MyMarkerView(context,R.layout.marker));
        graphchart.setData(data);
        graphchart.notifyDataSetChanged();
        graphchart.invalidate();

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


    public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.TweetHolder> {

        public class TweetHolder extends RecyclerView.ViewHolder{
            TextView tv_title,tv_desc;
            public TweetHolder(@NonNull View itemView) {
                super(itemView);
                tv_title=itemView.findViewById(R.id.tw_title);
                tv_desc=itemView.findViewById(R.id.tw_des);
            }
        }

        @NonNull
        @Override
        public TweetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.tweet_cell, parent, false);
            TweetHolder viewHolder = new TweetHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TweetHolder holder, int position) {
//            holder.tv_title.setText(vecTweets.get(position).title+"");
//            holder.tv_desc.setText(vecTweets.get(position).description+"");
        }

        @Override
        public int getItemCount() {
//            if (vecTweets!=null)
//                return vecTweets.size();
//            else
                return 5;

        }
    }





    }




