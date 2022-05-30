package com.vip.marrakech.admin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdminItineraryModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("pax")
    @Expose
    private String pax;
    @SerializedName("arrival_date")
    @Expose
    private String arrivalDate;
    @SerializedName("departure_date")
    @Expose
    private String departureDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("encryptedId")
    @Expose
    private String encryptedId;
    @SerializedName("date_string")
    @Expose
    private String date_string;

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(String date_string) {
        this.date_string = date_string;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

}
