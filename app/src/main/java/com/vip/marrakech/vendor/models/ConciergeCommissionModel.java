package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConciergeCommissionModel {

    @SerializedName("concierge_id")
    @Expose
    private String conciergeId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("total_spend")
    @Expose
    private String totalSpend;
    @SerializedName("total_clients")
    @Expose
    private String totalClients;
    @SerializedName("concierge_name")
    @Expose
    private String conciergeName;

    public String getConciergeId() {
        return conciergeId;
    }

    public void setConciergeId(String conciergeId) {
        this.conciergeId = conciergeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(String totalClients) {
        this.totalClients = totalClients;
    }

    public String getConciergeName() {
        return conciergeName;
    }

    public void setConciergeName(String conciergeName) {
        this.conciergeName = conciergeName;
    }

}