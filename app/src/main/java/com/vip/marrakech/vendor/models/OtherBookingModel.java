package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtherBookingModel implements Serializable {

    @SerializedName("concierge_name")
    @Expose
    private String conciergeName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pax")
    @Expose
    private String pax;
    @SerializedName("table_spend")
    @Expose
    private String tableSpend;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("total_spend")
    @Expose
    private String totalSpend;
    @SerializedName("encrypted_booking_id")
    @Expose
    private String encrypted_booking_id;

    public String getEncrypted_booking_id() {
        return encrypted_booking_id;
    }

    public void setEncrypted_booking_id(String encrypted_booking_id) {
        this.encrypted_booking_id = encrypted_booking_id;
    }

    public String getConciergeName() {
        return conciergeName;
    }

    public void setConciergeName(String conciergeName) {
        this.conciergeName = conciergeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getTableSpend() {
        return tableSpend;
    }

    public void setTableSpend(String tableSpend) {
        this.tableSpend = tableSpend;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }
}
