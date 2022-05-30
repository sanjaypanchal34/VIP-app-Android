package com.vip.marrakech.user.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UerItinareryModel implements Serializable {

    @SerializedName("arrival_date")
    private String arrival_date;

    @SerializedName("departure_date")
    private String departure_date;

    @SerializedName("id")
    private String id;


    @SerializedName("encryptedId")
    private String encryptedId;


    @SerializedName("pax")
    private String pax;

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }
}
