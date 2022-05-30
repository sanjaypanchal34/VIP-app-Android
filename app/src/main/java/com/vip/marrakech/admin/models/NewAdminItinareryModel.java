package com.vip.marrakech.admin.models;


import java.io.Serializable;
import java.util.List;

public class NewAdminItinareryModel implements Serializable {

    String name;
    List<AdminExpandedIntenariry> bookingList;
    boolean isVisible = false;
    String pax;

    public NewAdminItinareryModel(String name, List<AdminExpandedIntenariry> bookingList, boolean isVisible, String pax) {
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

    public List<AdminExpandedIntenariry> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<AdminExpandedIntenariry> bookingList) {
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
