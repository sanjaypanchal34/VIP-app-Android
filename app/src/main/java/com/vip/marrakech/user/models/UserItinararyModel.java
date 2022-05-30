package com.vip.marrakech.user.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserItinararyModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pax")
    @Expose
    private String pax;
    @SerializedName("group_type")
    @Expose
    private String groupType;
    @SerializedName("venue_title")
    @Expose
    private String venueTitle;
    @SerializedName("start_date_time")
    @Expose
    private String startDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("venue_id")
    @Expose
    private String venueId;
    @SerializedName("venue_type")
    @Expose
    private String venueType;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("table_name")
    @Expose
    private String tableName;
    @SerializedName("table_no")
    @Expose
    private String tableNo;
    @SerializedName("pre_book_spend")
    @Expose
    private String preBookSpend;
    @SerializedName("final_spend")
    @Expose
    private String finalSpend;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("booking_from")
    @Expose
    private String bookingFrom;
    @SerializedName("itinerary_id")
    @Expose
    private String itineraryId;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("scan_code")
    @Expose
    private String scanCode;
    @SerializedName("is_payment")
    @Expose
    private String isPayment;
    @SerializedName("bottle_name")
    @Expose
    private String bottleName;
    @SerializedName("bottle_price")
    @Expose
    private String bottlePrice;
    @SerializedName("holding_deposite")
    @Expose
    private String holdingDeposite;
    @SerializedName("total_spend_MAD")
    @Expose
    private String totalSpend;
    @SerializedName("encrypted_day_id")
    @Expose
    private String encryptedDayId;
    @SerializedName("encrypted_itinerary_id")
    @Expose
    private String encryptedItineraryId;

    @SerializedName("deposite_MAD")
    @Expose
    private String deposite_MAD;
    @SerializedName("balance")
    @Expose
    private String balance;

    @SerializedName("day_no")
    @Expose
    private String day_no;

    @SerializedName("deposit_percentage")
    @Expose
    private String deposit_percentage;

    @SerializedName("deposit_option")
    @Expose
    private String deposit_option;

    public String getDay_no() {
        return day_no;
    }

    public void setDay_no(String day_no) {
        this.day_no = day_no;
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

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getVenueTitle() {
        return venueTitle;
    }

    public void setVenueTitle(String venueTitle) {
        this.venueTitle = venueTitle;
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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getPreBookSpend() {
        return preBookSpend;
    }

    public void setPreBookSpend(String preBookSpend) {
        this.preBookSpend = preBookSpend;
    }

    public String getFinalSpend() {
        return finalSpend;
    }

    public void setFinalSpend(String finalSpend) {
        this.finalSpend = finalSpend;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(String bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public String getBottleName() {
        return bottleName;
    }

    public void setBottleName(String bottleName) {
        this.bottleName = bottleName;
    }

    public String getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(String bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public String getHoldingDeposite() {
        return holdingDeposite;
    }

    public void setHoldingDeposite(String holdingDeposite) {
        this.holdingDeposite = holdingDeposite;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getEncryptedDayId() {
        return encryptedDayId;
    }

    public void setEncryptedDayId(String encryptedDayId) {
        this.encryptedDayId = encryptedDayId;
    }

    public String getEncryptedItineraryId() {
        return encryptedItineraryId;
    }

    public void setEncryptedItineraryId(String encryptedItineraryId) {
        this.encryptedItineraryId = encryptedItineraryId;
    }

    public String getDeposite_MAD() {
        return deposite_MAD;
    }

    public void setDeposite_MAD(String deposite_MAD) {
        this.deposite_MAD = deposite_MAD;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDeposit_percentage() {
        return deposit_percentage;
    }

    public void setDeposit_percentage(String deposit_percentage) {
        this.deposit_percentage = deposit_percentage;
    }

    public String getDeposit_option() {
        return deposit_option;
    }

    public void setDeposit_option(String deposit_option) {
        this.deposit_option = deposit_option;
    }
}