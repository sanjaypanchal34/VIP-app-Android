package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VendorExpandBooking implements Serializable {

    @SerializedName("date")
    private String date;

    @SerializedName("bookings")
    private List<VendorAllBookingModel> list;


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

    public List<VendorAllBookingModel> getList() {
        return list;
    }

    public void setList(List<VendorAllBookingModel> list) {
        this.list = list;
    }
}
