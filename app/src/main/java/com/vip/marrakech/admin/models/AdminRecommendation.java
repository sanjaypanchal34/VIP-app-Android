package com.vip.marrakech.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdminRecommendation implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("type_name")
    private String type_name;

    @SerializedName("image_name")
    private String image_name;

    @SerializedName("display_name")
    private String display_name;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
