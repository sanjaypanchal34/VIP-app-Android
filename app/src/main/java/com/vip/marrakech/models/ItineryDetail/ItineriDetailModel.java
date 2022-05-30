
package com.vip.marrakech.models.ItineryDetail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItineriDetailModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("encryptedId")
    @Expose
    private String encryptedId;

    @SerializedName("client_id")
    @Expose
    private String clientId;

    @SerializedName("client_email")
    @Expose
    private String client_email;
    @SerializedName("client_phone")
    @Expose
    private String client_phone;


    @SerializedName("group")
    @Expose
    private String group;
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
    @SerializedName("notification_sent")
    @Expose
    private String notificationSent;
    @SerializedName("notification_date_time")
    @Expose
    private String notificationDateTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("get_itinerary_day_detail")
    @Expose
    private List<GetItineraryDayDetail> getItineraryDayDetail = null;
    @SerializedName("total_spend")
    @Expose
    private String  totalSpend;

    @SerializedName("venue_title")
    @Expose
    private String venue_title;

    @SerializedName("holding_deposit")
    @Expose
    private String holding_deposit;


    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("balance")
    @Expose
    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHolding_deposit() {
        return holding_deposit;
    }

    public void setHolding_deposit(String holding_deposit) {
        this.holding_deposit = holding_deposit;
    }

    private String displayArrival,displayDepature;


    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(String notificationSent) {
        this.notificationSent = notificationSent;
    }

    public String getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(String notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<GetItineraryDayDetail> getGetItineraryDayDetail() {
        return getItineraryDayDetail;
    }

    public void setGetItineraryDayDetail(List<GetItineraryDayDetail> getItineraryDayDetail) {
        this.getItineraryDayDetail = getItineraryDayDetail;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public String getDisplayArrival() {
        return displayArrival;
    }

    public String getDisplayDepature() {
        return displayDepature;
    }

    public void setDisplayDepature(String displayDepature) {
        this.displayDepature = displayDepature;
    }

    public void setDisplayArrival(String displayArrival) {
        this.displayArrival = displayArrival;
    }


    public String getVenue_title() {
        return venue_title;
    }

    public void setVenue_title(String venue_title) {
        this.venue_title = venue_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
