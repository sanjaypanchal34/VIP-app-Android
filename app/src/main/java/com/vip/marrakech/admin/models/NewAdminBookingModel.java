package com.vip.marrakech.admin.models;


import java.io.Serializable;
import java.util.List;

public class NewAdminBookingModel implements Serializable {

    String name;
    List<AdminExpandBooking> bookingList;
    boolean isVisible = false;
    String pax;

    public NewAdminBookingModel(String name, List<AdminExpandBooking> bookingList, boolean isVisible, String pax) {
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

    public List<AdminExpandBooking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<AdminExpandBooking> bookingList) {
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
