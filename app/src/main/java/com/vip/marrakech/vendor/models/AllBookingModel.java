package com.vip.marrakech.vendor.models;

import java.io.Serializable;
import java.util.List;

public class AllBookingModel implements Serializable {

    String name;
    List<VendorExpandBooking> bookingList;
    boolean isVisible = false;
    String pax;

    public AllBookingModel(String name, List<VendorExpandBooking> bookingList, boolean isVisible,String pax) {
        this.name = name;
        this.bookingList = bookingList;
        this.isVisible = isVisible;
        this.pax = pax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VendorExpandBooking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<VendorExpandBooking> bookingList) {
        this.bookingList = bookingList;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }
}
