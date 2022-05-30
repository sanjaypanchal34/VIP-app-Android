package com.vip.marrakech.models;

import java.io.Serializable;
import java.util.Objects;

public class TimeModel implements Serializable {

    private String time, type;

    public TimeModel(String time, String type) {
        this.time = time;
        this.type = type;
    }

    public TimeModel(String time) {
        this.time=time;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        try {
            TimeModel model = (TimeModel) o;
            return model.getTime().equals(getTime());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, type);
    }
}
