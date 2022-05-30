package com.vip.marrakech.user.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserPromotionModel implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("promotionId")
    private String promotionId;

    @SerializedName("venue_type")
    private String venue_type;

    @SerializedName("venue_id")
    private String venue_id;

    @SerializedName("title")
    private String title;

    @SerializedName("venueTitle")
    private String venueTitle;

    @SerializedName("promotion_title")
    private String promotion_title;

    @SerializedName("description")
    private String description;

    @SerializedName("feature_image")
    private String images;

    @SerializedName("encrypted_id")
    private String encrypted_id;

    public String getVenue_type() {
        return venue_type;
    }

    public void setVenue_type(String venue_type) {
        this.venue_type = venue_type;
    }

    public String getPromotion_title() {
        return promotion_title;
    }

    public void setPromotion_title(String promotion_title) {
        this.promotion_title = promotion_title;
    }

    public String getVenueTitle() {
        return venueTitle;
    }

    public void setVenueTitle(String venueTitle) {
        this.venueTitle = venueTitle;
    }

    public String getId() {
        return id;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
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

    public String getEncrypted_id() {
        return encrypted_id;
    }

    public String getImages() {
        return images;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
