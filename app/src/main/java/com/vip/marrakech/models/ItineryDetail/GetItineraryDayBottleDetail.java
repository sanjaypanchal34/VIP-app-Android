
package com.vip.marrakech.models.ItineryDetail;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetItineraryDayBottleDetail implements Serializable {

    @SerializedName("id")
    @Expose
    private String id = "";

    @SerializedName("booking_id")
    @Expose
    private String booking_id = "";

    @SerializedName("itinerary_day_id")
    @Expose
    private String itineraryDayId = "";
    @SerializedName("bottle_name")
    @Expose
    private String bottleName = "";
    @SerializedName("bottle_type")
    @Expose
    private String bottleType = "";
    @SerializedName("bottle_price")
    @Expose
    private String bottlePrice = "";
    @SerializedName("created_at")
    @Expose
    private String createdAt = "";
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public GetItineraryDayBottleDetail() {
    }

    public GetItineraryDayBottleDetail(String bottleType) {
        this.bottleType = bottleType;
    }

    public GetItineraryDayBottleDetail(String bottleType,String id) {
        this.bottleType = bottleType;
        this.id = id;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItineraryDayId() {
        return itineraryDayId;
    }

    public void setItineraryDayId(String itineraryDayId) {
        this.itineraryDayId = itineraryDayId;
    }

    public String getBottleName() {
        return bottleName;
    }

    public void setBottleName(String bottleName) {
        this.bottleName = bottleName;
    }

    public String getBottleType() {
        return bottleType;
    }

    public void setBottleType(String bottleType) {
        this.bottleType = bottleType;
    }

    public String getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(String bottlePrice) {
        this.bottlePrice = bottlePrice;
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


    @Override
    public boolean equals(@Nullable Object obj) {
        try{
            GetItineraryDayBottleDetail detail = (GetItineraryDayBottleDetail) obj;
            return id.equals(detail.getId());
        }catch (Exception e){
            return false;
        }
    }
}
