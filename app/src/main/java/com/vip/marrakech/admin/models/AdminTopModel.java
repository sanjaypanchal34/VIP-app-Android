package com.vip.marrakech.admin.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vip.marrakech.retrofit.ApiClient;

public class AdminTopModel implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("venue_type")
    private String venue_type;

    @SerializedName("venue_title")
    private String venue_title;

    @SerializedName("encrypted_id")
    private String encrypted_id;

    @SerializedName("feature_image")
    private String feature_image;

    @SerializedName("coming_soon")
    private String coming_soon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue_type() {
        return venue_type;
    }

    public void setVenue_type(String venue_type) {
        this.venue_type = venue_type;
    }

    public String getVenue_title() {
        return venue_title;
    }

    public void setVenue_title(String venue_title) {
        this.venue_title = venue_title;
    }

    public String getEncrypted_id() {
        return encrypted_id;
    }

    public void setEncrypted_id(String encrypted_id) {
        this.encrypted_id = encrypted_id;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public String getComing_soon() {
        return coming_soon;
    }

    public void setComing_soon(String coming_soon) {
        this.coming_soon = coming_soon;
    }
}