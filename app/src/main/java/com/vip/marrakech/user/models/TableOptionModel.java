package com.vip.marrakech.user.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TableOptionModel implements Serializable {
    @SerializedName("table_name")
    private String table_name;

    @SerializedName("display_name")
    private String display_name;

    @SerializedName("alcohol_name")
    private String alcohol_name;

    @SerializedName("price_in_MAD")
    private String price_in_MAD;

    @SerializedName("number_of_table")
    private String number_of_table;

    @SerializedName("price_in_GBP")
    private String price_in_GBP;

    @SerializedName("price_per_person")
    private String price_per_person;

    @SerializedName("deposit_percentage")
    private String deposit_percentage;

    private boolean isSelect = false;

    public String getDeposit_percentage() {
        return deposit_percentage;
    }

    public void setDeposit_percentage(String deposit_percentage) {
        this.deposit_percentage = deposit_percentage;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getAlcohol_name() {
        return alcohol_name;
    }

    public void setAlcohol_name(String alcohol_name) {
        this.alcohol_name = alcohol_name;
    }

    public String getPrice_in_MAD() {
        return price_in_MAD;
    }

    public void setPrice_in_MAD(String price_in_MAD) {
        this.price_in_MAD = price_in_MAD;
    }

    public String getNumber_of_table() {
        return number_of_table;
    }

    public void setNumber_of_table(String number_of_table) {
        this.number_of_table = number_of_table;
    }

    public String getPrice_in_GBP() {
        return price_in_GBP;
    }

    public void setPrice_in_GBP(String price_in_GBP) {
        this.price_in_GBP = price_in_GBP;
    }

    public String getPrice_per_person() {
        return price_per_person;
    }

    public void setPrice_per_person(String price_per_person) {
        this.price_per_person = price_per_person;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
