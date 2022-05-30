package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConciergeClientModel implements Serializable {
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("client_name")
    @Expose
    private String clientName;

    @SerializedName("client_email")
    @Expose
    private String clientEmail;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comission")
    @Expose
    private String comission;
    @SerializedName("total_spend")
    @Expose
    private String totalSpend;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComission() {
        return comission;
    }

    public void setComission(String comission) {
        this.comission = comission;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

}
