package com.vip.marrakech.enums;

import androidx.annotation.NonNull;

public enum VenueTypes {
    DAY_PARTIES("Day Party", 0),
    NIGHTCLUB("Nightclub", 1),
    SHISHA_BAR("Shisha Lounge", 2),
    RESTAURANT("Restaurant", 3),
    EXPERIENCES("Experiences", 4),
    ROOF_TOP_BAR("Rooftop Bars", 5);

    private final String stringValue;

    private VenueTypes(String toString, int value) {
        stringValue = toString;
    }

    @NonNull
    @Override
    public String toString() {
        return stringValue;
    }
}