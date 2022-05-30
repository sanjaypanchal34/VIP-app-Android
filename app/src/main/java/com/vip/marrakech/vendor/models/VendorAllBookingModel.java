package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorAllBookingModel {

    @SerializedName("client_name")
    @Expose
    private String clientName;

    @SerializedName("pax")
    @Expose
    private String pax;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("itinerary_encryptedId")
    @Expose
    private String itineraryEncryptedId;

    @SerializedName("date_time")
    @Expose
    private String dateTime;

    @SerializedName("date_string")
    @Expose
    private String dateString;

    @SerializedName("total_spend")
    @Expose
    private String totalSpend;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("encrypted_booking_id")
    @Expose
    private String encrypted_booking_id;

    @SerializedName("table_no")
    @Expose
    private String table_no;

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getEncrypted_booking_id() {
        return encrypted_booking_id;
    }

    public void setEncrypted_booking_id(String encrypted_booking_id) {
        this.encrypted_booking_id = encrypted_booking_id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public String getItineraryEncryptedId() {
        return itineraryEncryptedId;
    }

    public void setItineraryEncryptedId(String itineraryEncryptedId) {
        this.itineraryEncryptedId = itineraryEncryptedId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}