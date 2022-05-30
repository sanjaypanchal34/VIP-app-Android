package com.vip.marrakech.user.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MixesModel implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
