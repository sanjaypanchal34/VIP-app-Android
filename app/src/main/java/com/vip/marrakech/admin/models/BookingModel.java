package com.vip.marrakech.admin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingModel implements Serializable {


    @SerializedName("itinerary_day_id")
    @Expose
    private String itineraryDayId;
    @SerializedName("itinerary_id")
    @Expose
    private String itineraryId;
    @SerializedName("start_date_time")
    @Expose
    private String startDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("itinerary_day_encrypted_id")
    @Expose
    private String itineraryDayEncryptedId;
    @SerializedName("itinerary_encrypted_id")
    @Expose
    private String itineraryEncryptedId;

    @SerializedName("booking_type")
    @Expose
    private String booking_type;

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getItineraryDayId() {
        return itineraryDayId;
    }

    public void setItineraryDayId(String itineraryDayId) {
        this.itineraryDayId = itineraryDayId;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getItineraryDayEncryptedId() {
        return itineraryDayEncryptedId;
    }

    public void setItineraryDayEncryptedId(String itineraryDayEncryptedId) {
        this.itineraryDayEncryptedId = itineraryDayEncryptedId;
    }

    public String getItineraryEncryptedId() {
        return itineraryEncryptedId;
    }

    public void setItineraryEncryptedId(String itineraryEncryptedId) {
        this.itineraryEncryptedId = itineraryEncryptedId;
    }
}
