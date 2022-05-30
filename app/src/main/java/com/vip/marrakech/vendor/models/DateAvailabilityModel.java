package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.SerializedName;

public class DateAvailabilityModel {

    @SerializedName("count")
    private String time;
    @SerializedName("date")
    private String date;
    @SerializedName("id")
    private String id;


    public DateAvailabilityModel(String time, String date, String id) {
        this.time = time;
        this.date = date;
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
