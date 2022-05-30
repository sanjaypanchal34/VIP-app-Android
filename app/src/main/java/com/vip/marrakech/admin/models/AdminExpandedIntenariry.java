package com.vip.marrakech.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AdminExpandedIntenariry implements Serializable {
    @SerializedName("date")
    private String date;

    @SerializedName("bookings")
    private List<AdminItineraryModel> list;


    private boolean isVisible =false;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<AdminItineraryModel> getList() {
        return list;
    }

    public void setList(List<AdminItineraryModel> list) {
        this.list = list;
    }
}
