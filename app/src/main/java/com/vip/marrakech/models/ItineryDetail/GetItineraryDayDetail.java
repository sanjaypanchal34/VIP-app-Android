
package com.vip.marrakech.models.ItineryDetail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetItineraryDayDetail implements Serializable {

    @SerializedName("day_details")
    @Expose
    private List<DayDetail> dayDetails = null;

    public List<DayDetail> getDayDetails() {
        return dayDetails;
    }

    public void setDayDetails(List<DayDetail> dayDetails) {
        this.dayDetails = dayDetails;
    }

}
