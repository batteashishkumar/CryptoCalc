package com.ashish.cryptocalc;

import org.json.JSONObject;

public interface ResponseListenerNew {
    void onResponseReceived(JSONObject jsonObject) throws Exception;
}
