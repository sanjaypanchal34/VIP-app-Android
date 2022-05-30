package com.vip.marrakech.retrofit.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public interface OnCallBackListener {

    void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException;
    void OnCallBackError(String tag, String error);
}
