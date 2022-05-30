package com.vip.marrakech.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdminNotificationModel implements Serializable, Parcelable {

    @SerializedName("is_read")
    private String is_read="";

    @SerializedName("notification_id")
    private String notification_id="";

    @SerializedName("promotion_id")
    private String promotion_id="";

    @SerializedName("notification_time")
    private String notification_time="";

    @SerializedName("notification_content")
    private String notification_content="";

    @SerializedName("itinerary_id")
    private String itinerary_id="";

    @SerializedName("itinerary_day_id")
    private String itinerary_day_id="";

    @SerializedName("view_detail")
    private String view_detail="";

    @SerializedName("venue_id")
    private String venue_id="";

    @SerializedName("notification_type")
    private String notification_type="";

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }


    public String getItinerary_id() {
        return itinerary_id;
    }

    public void setItinerary_id(String itinerary_id) {
        this.itinerary_id = itinerary_id;
    }

    public String getItinerary_day_id() {
        return itinerary_day_id;
    }

    public void setItinerary_day_id(String itinerary_day_id) {
        this.itinerary_day_id = itinerary_day_id;
    }

    public String getView_detail() {
        return view_detail;
    }

    public void setView_detail(String view_detail) {
        this.view_detail = view_detail;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AdminNotificationModel createFromParcel(Parcel in) {
            return new AdminNotificationModel(in);
        }

        public AdminNotificationModel[] newArray(int size) {
            return new AdminNotificationModel[size];
        }
    };

    public AdminNotificationModel(Parcel in) {
        this.is_read = in.readString();
        this.notification_id = in.readString();
        this.notification_time = in.readString();
        this.notification_content = in.readString();
        this.itinerary_id = in.readString();
        this.itinerary_day_id = in.readString();
        this.view_detail = in.readString();
        this.venue_id = in.readString();
        this.notification_type = in.readString();
        this.promotion_id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.is_read);
        dest.writeString(this.notification_id);
        dest.writeString(this.notification_time);
        dest.writeString(this.notification_content);
        dest.writeString(this.itinerary_id);
        dest.writeString(this.itinerary_day_id);
        dest.writeString(this.view_detail);
        dest.writeString(this.venue_id);
        dest.writeString(this.notification_type);
        dest.writeString(this.promotion_id);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + is_read + '\'' +
                ", title='" + notification_id + '\'' +
                ", img='" + notification_time + '\'' +
                "short_description='" + notification_content + '\'' +
                ", body='" + itinerary_id + '\'' +
                ", date='" + itinerary_day_id + '\'' +
                ", view='" + view_detail + '\'' +
                '}';
    }
}
