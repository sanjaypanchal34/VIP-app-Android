package com.vip.marrakech.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AdminRecommendationDetail implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("venue_id")
    private String venue_id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("venue_type")
    private String venue_type;


    @SerializedName("phone_no")
    private String phone_no;

    @SerializedName("venue_title")
    private String venue_title;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("encrypted_id")
    private String encrypted_id;

    @SerializedName("menu_encrypted_id")
    private String menu_encrypted_id;

    @SerializedName("menu_image")
    private String menu_image;

    @SerializedName("extention")
    private String extention;

    @SerializedName("feature_image")
    private String feature_image;

    @SerializedName("label1")
    private String label1;

    @SerializedName("busiest_days")
    private String busiest_days;

    @SerializedName("label2")
    private String label2;

    @SerializedName("music_type")
    private String music_type;

    @SerializedName("label3")
    private String label3;

    @SerializedName("opening_hours")
    private String opening_hours;

    @SerializedName("label4")
    private String label4;

    @SerializedName("dress_code")
    private String dress_code;

    @SerializedName("coming_soon")
    private String coming_soon;

    @SerializedName("deposit_option")
    private String deposit_option;


    public String getComing_soon() {
        return coming_soon;
    }

    public void setComing_soon(String coming_soon) {
        this.coming_soon = coming_soon;
    }

    @SerializedName("images")
    private List<Image> imageList;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEncrypted_id() {
        return encrypted_id;
    }

    public void setEncrypted_id(String encrypted_id) {
        this.encrypted_id = encrypted_id;
    }

    public String getMenu_encrypted_id() {
        return menu_encrypted_id;
    }

    public void setMenu_encrypted_id(String menu_encrypted_id) {
        this.menu_encrypted_id = menu_encrypted_id;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }


    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getBusiest_days() {
        return busiest_days;
    }

    public void setBusiest_days(String busiest_days) {
        this.busiest_days = busiest_days;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getMusic_type() {
        return music_type;
    }

    public void setMusic_type(String music_type) {
        this.music_type = music_type;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getLabel4() {
        return label4;
    }

    public void setLabel4(String label4) {
        this.label4 = label4;
    }

    public String getDress_code() {
        return dress_code;
    }

    public void setDress_code(String dress_code) {
        this.dress_code = dress_code;
    }


    public String getDeposit_option() {
        return deposit_option;
    }

    public void setDeposit_option(String deposit_option) {
        this.deposit_option = deposit_option;
    }
}
