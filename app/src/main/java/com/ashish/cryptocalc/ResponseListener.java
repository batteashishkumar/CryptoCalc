package com.ashish.cryptocalc;

import java.util.Vector;

public interface ResponseListener {
    void onResponseReceived(Vector<DayData> dayDataVector,Vector<DayData> dayDataWeekVector,Vector<DayData> dayDataMonthVector,Vector<DayData> dayDataYearVector) throws Exception;
}
