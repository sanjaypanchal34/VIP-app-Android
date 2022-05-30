package com.vip.marrakech.admin.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MasterVenueModel implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("venue_id")
    private String venue_id;

    @SerializedName("vendor_id")
    private String vendor_id;

    @SerializedName("title")
    private String title;

    @SerializedName("venue_type")
    private String venue_type;

    @SerializedName("status")
    private String status;

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public MasterVenueModel(String id) {
        this.id = id;
    }

    public MasterVenueModel() {
    }

    public MasterVenueModel(String id,String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id == null ? venue_id : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue_type() {
        return venue_type;
    }

    public void setVenue_type(String venue_type) {
        this.venue_type = venue_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            MasterVenueModel model = (MasterVenueModel) obj;
            return model.getId().equals(id);
        } catch (Exception e) {
            return false;
        }
    }
}
