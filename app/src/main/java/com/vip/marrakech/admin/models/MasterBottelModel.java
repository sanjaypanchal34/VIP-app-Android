package com.vip.marrakech.admin.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;

import java.io.Serializable;

public class MasterBottelModel implements Serializable {


    @SerializedName("id")
    private String id = "";

    @SerializedName("venue_id")
    private String venue_id;

    @SerializedName("bottle_name")
    private String bottle_name;

    @SerializedName("bottle_type")
    private String bottle_type;

    @SerializedName("bottle_price")
    private String bottle_price;

    public MasterBottelModel() {
    }

    public MasterBottelModel(String bottle_name) {
        this.bottle_name = bottle_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getBottle_name() {
        return bottle_name;
    }

    public void setBottle_name(String bottle_name) {
        this.bottle_name = bottle_name;
    }

    public String getBottle_type() {
        return bottle_type;
    }

    public void setBottle_type(String bottle_type) {
        this.bottle_type = bottle_type;
    }

    public String getBottle_price() {
        return bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        this.bottle_price = bottle_price;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        try{
            MasterBottelModel detail = (MasterBottelModel) obj;
            return bottle_name.equals(detail.getBottle_name());
        }catch (Exception e){
            return false;
        }
    }
}

