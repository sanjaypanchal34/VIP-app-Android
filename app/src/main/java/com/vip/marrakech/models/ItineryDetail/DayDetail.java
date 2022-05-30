
package com.vip.marrakech.models.ItineryDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayDetail implements Serializable {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("table_no")
    @Expose
    private String table_no = "";
    @SerializedName("itinerary_id")
    @Expose
    private String itineraryId = "";
    @SerializedName("day_no")
    @Expose
    private String dayNo = "";
    @SerializedName("venue_id")
    @Expose
    private String venueId = "";
    @SerializedName("venue_title")
    @Expose
    private String venue_title = "";
    @SerializedName("venue_type")
    @Expose
    private String venueType = "";
    @SerializedName("table_name")
    @Expose
    private String tableName = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("start_date_time")
    @Expose
    private String startDateTime = "";
    @SerializedName("get_itinerary_day_detail_bottle_price")
    @Expose
    private String getItineraryDayDetailBottlePrice = "";
    @SerializedName("get_itinerary_day_bottle_detail")
    @Expose
    private List<GetItineraryDayBottleDetail> getItineraryDayBottleDetail = null;
    @SerializedName("date")
    @Expose
    private String date = "";
    @SerializedName("time")
    @Expose
    private String time = "00:00";

    @SerializedName("deposite_MAD")
    @Expose
    private String deposite_MAD = "";

    @SerializedName("encrypted_day_id")
    @Expose
    private String encrypted_day_id = "";

    @SerializedName("scan_code")
    @Expose
    private String scan_code = "";

    @SerializedName("service")
    private String service = "";

    @SerializedName("bottle_price")
    private String bottle_price = "";

    @SerializedName("currency")
    private String currency = "";


    public String getBottle_price() {
        return bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        this.bottle_price = bottle_price;
    }

    public String getScan_code() {
        return scan_code;
    }

    public void setScan_code(String scan_code) {
        this.scan_code = scan_code;
    }

    public String getEncrypted_day_id() {
        return encrypted_day_id;
    }

    public void setEncrypted_day_id(String encrypted_day_id) {
        this.encrypted_day_id = encrypted_day_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getDayNo() {
        return dayNo;
    }

    public void setDayNo(String dayNo) {
        this.dayNo = dayNo;
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

    public String getTableName() {
        return tableName;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getGetItineraryDayDetailBottlePrice() {
        return getItineraryDayDetailBottlePrice;
    }

    public void setGetItineraryDayDetailBottlePrice(String getItineraryDayDetailBottlePrice) {
        this.getItineraryDayDetailBottlePrice = getItineraryDayDetailBottlePrice;
    }

    public List<GetItineraryDayBottleDetail> getGetItineraryDayBottleDetail() {
        if (getItineraryDayBottleDetail == null) {
            ArrayList<GetItineraryDayBottleDetail> list = new ArrayList<GetItineraryDayBottleDetail>();
            list.add(new GetItineraryDayBottleDetail());
            return list;
        } else {
            return getItineraryDayBottleDetail;
        }
    }

    public void setGetItineraryDayBottleDetail(List<GetItineraryDayBottleDetail> getItineraryDayBottleDetail) {
        this.getItineraryDayBottleDetail = getItineraryDayBottleDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue_title() {
        return venue_title;
    }

    public void setVenue_title(String venue_title) {
        this.venue_title = venue_title;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public String getDeposite_MAD() {
        return deposite_MAD;
    }

    public void setDeposite_MAD(String deposite_MAD) {
        this.deposite_MAD = deposite_MAD;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
