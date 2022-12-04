package com.ashish.cryptocalc;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Vector;

public class SingleActivityViewModel extends ViewModel {
    private static SingleActivityViewModel singleActivityViewModel=null;
    private MutableLiveData<InputData> inputDataMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<OutputData> outputDataMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataWeekVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataMonthVector=new MutableLiveData<>();
    private MutableLiveData<Vector<DayData>> mdayDataYearVector=new MutableLiveData<>();
    private SingleActivityRespository singleActivityRespository=new SingleActivityRespository(mdayDataVector,mdayDataWeekVector,mdayDataMonthVector,mdayDataYearVector);
    public SingleActivityViewModel(){
    }
    public MutableLiveData<OutputData> getmOutputData() { return outputDataMutableLiveData;}
    public MutableLiveData<Vector<DayData>> getDayDataVector() {
        return mdayDataVector;
    }

    public MutableLiveData<Vector<DayData>> getDayDataWeekVector() {
        return mdayDataWeekVector;
    }

    public MutableLiveData<Vector<DayData>> getDayDataMonthVector() {
        return mdayDataMonthVector;
    }

    public MutableLiveData<Vector<DayData>> getDayDataYearVector() {
        return mdayDataYearVector;
    }

    public void setmInputData(InputData inputData) {
        inputDataMutableLiveData.setValue(inputData);
        OutputData outputData=new OutputData();
        outputData.Investment=inputDataMutableLiveData.getValue().Investment;
        outputData.CoinsPurchased= (float) Math.floor(inputDataMutableLiveData.getValue().Investment/inputDataMutableLiveData.getValue().RealtimePrice);
        outputData.BuyingFee=inputDataMutableLiveData.getValue().BuyingFee;
        outputData.CoinsPurchasedinclBF=(float) Math.floor(inputDataMutableLiveData.getValue().Investment/(inputDataMutableLiveData.getValue().RealtimePrice+inputDataMutableLiveData.getValue().BuyingFee));
        outputData.RiskAmount=(float) Math.floor((inputDataMutableLiveData.getValue().Investment*(inputDataMutableLiveData.getValue().RiskPercent/100)));
        outputData.WorseRiskAmount=(float) Math.floor(inputDataMutableLiveData.getValue().Investment-((inputDataMutableLiveData.getValue().Investment*(inputDataMutableLiveData.getValue().RiskPercent/100))));
        outputData.SellingFeeForRisk=inputDataMutableLiveData.getValue().SellingFee;
        outputData.TotalAmountwithSF=outputData.WorseRiskAmount+outputData.SellingFeeForRisk;
        outputData.ProfitAmount=inputDataMutableLiveData.getValue().SellRate-inputDataMutableLiveData.getValue().Investment;
        outputData.ProfitAmountwithSF=(inputDataMutableLiveData.getValue().SellRate+inputDataMutableLiveData.getValue().SellingFee)-inputDataMutableLiveData.getValue().Investment;
        outputDataMutableLiveData.setValue(outputData);
    }

    public void callApi(String coinSelected, Context context) {
        singleActivityRespository.callRApi(coinSelected,context);
    }
}
