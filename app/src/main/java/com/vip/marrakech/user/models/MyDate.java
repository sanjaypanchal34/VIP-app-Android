package com.vip.marrakech.user.models;

import java.io.Serializable;
import java.util.Date;

public class MyDate implements Serializable {
    private Date date;
    private boolean isSelected = false;

    public MyDate(Date date, boolean isSelected) {
        this.date = date;
        this.isSelected = isSelected;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
