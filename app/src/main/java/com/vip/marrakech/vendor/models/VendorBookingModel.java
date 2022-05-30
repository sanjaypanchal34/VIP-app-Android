package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorBookingModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pax")
    @Expose
    private String pax;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("start_date_time")
    @Expose
    private String startDateTime;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("itinerary_encryptedId")
    @Expose
    private String itineraryEncryptedId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getItineraryEncryptedId() {
        return itineraryEncryptedId;
    }

    public void setItineraryEncryptedId(String itineraryEncryptedId) {
        this.itineraryEncryptedId = itineraryEncryptedId;
    }

}