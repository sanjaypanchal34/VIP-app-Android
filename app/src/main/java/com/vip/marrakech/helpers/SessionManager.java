package com.vip.marrakech.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        preferences = context.getSharedPreferences("hjcdxcxvchc", 0);
        editor = preferences.edit();
    }
    public static String getLanguage() {
        return preferences.getString("LANGUAGE", "en");
    }

    public static void setLanguage(String language) {
        editor.putString("LANGUAGE", language);
        editor.apply();
    }

    public static void setRemembered(boolean b) {
        editor.putBoolean("USER_REMEMBER", b);
        editor.apply();
    }

    public static void setLogged(boolean b) {
        editor.putBoolean("USER_LOGGED", b);
        editor.apply();
    }

    public static void setToken(String token) {
        editor.putString("USER_TOKEN", token);
        editor.apply();
    }

    public static void setEncryptedID(String id) {
        editor.putString("USER_E_ID", id);
        editor.apply();
    }

    public static void setName(String name) {
        editor.putString("USER_NAME", name);
        editor.apply();
    }


    public static void setEmail(String email) {
        editor.putString("USER_EMAIL", email);
        editor.apply();
    }

    public static void setMobile(String mobile) {
        editor.putString("USER_MOBILE", mobile);
        editor.apply();
    }


    public static void setRole(String role) {
        editor.putString("USER_ROLE", role);
        editor.apply();
    }


    public static void setVendorExpiryDate(String date) {
        editor.putString("VENDOR_EXPIRY_DATE", date);
        editor.apply();
    }

    public static void setVendorSubscriptionType(String type) {
        editor.putString("VENDOR_SUBSCRIPTION_TYPE", type);
        editor.apply();
    }

    public static void setVendorIsSubscription(int is) {
        editor.putInt("VENDOR_IS_SUBSCRIPTION", is);
        editor.apply();
    }


    public static void setPassword(String password) {
        editor.putString("USER_PASSWORD", password);
        editor.apply();
    }

    public static void setID(String id) {
        editor.putString("USER_ID", id);
        editor.apply();
    }


    public static void setVenueType(String venue_type) {
        editor.putString("USER_VENUE_TYPE", venue_type);
        editor.apply();
    }


    public static boolean isLogged() {
        return preferences.getBoolean("USER_LOGGED", false);
    }

    public static boolean isRemembered() {
        return preferences.getBoolean("USER_REMEMBER", false);
    }

    public static String getToken() {
        return preferences.getString("USER_TOKEN", "");
    }

    public static String getVendorSubscriptionType() {
        return preferences.getString("VENDOR_SUBSCRIPTION_TYPE", "");
    }

    public static int getIsVendorSubscription() {
        return preferences.getInt("VENDOR_IS_SUBSCRIPTION", 0);
    }

    public static String getVendorExpiryDate() {
        return preferences.getString("VENDOR_EXPIRY_DATE", "");
    }

    public static String getName() {
        return preferences.getString("USER_NAME", "");
    }

    public static String getEncryptedID() {
        return preferences.getString("USER_E_ID", "");
    }

    public static String getEmail() {
        return preferences.getString("USER_EMAIL", "");
    }
    public static String getMobile() {
        return preferences.getString("USER_MOBILE", "");
    }

    public static String getRole() {
        return preferences.getString("USER_ROLE", "");
    }

    public static String getID() {
        return preferences.getString("USER_ID", "");
    }

    public static String getVenueID() {
        return preferences.getString("VENDOR_VENUE_ID", "");
    }

    public static String getVenue_Type() {
        return preferences.getString("USER_VENUE_TYPE", "");
    }

    public static String getPassword() {
        return preferences.getString("USER_PASSWORD", "");
    }

    public static String getFCM_TOKEN() {
        return preferences.getString("FCM_TOKEN", "");
    }

    public static String getVendorSubscriptionStatus() {
        return preferences.getString("VENDOR_SUBSCRIPTION_STATUS", "");
    }


    public static void setVendorSubscriptionStatus(String subscription_status) {
        editor.putString("VENDOR_SUBSCRIPTION_STATUS", subscription_status);
        editor.apply();
    }

    public static void setVenueID(String venue_id) {
        editor.putString("VENDOR_VENUE_ID", venue_id);
        editor.apply();
    }

    public static void saveFSMToken(String token) {
        editor.putString("FCM_TOKEN", token);
        editor.apply();
    }

    public static void ProfilePasswordSet(int profile_password_set) {
        editor.putInt("IS_VENDOR_PROFILE_PASSWORD_SET", profile_password_set);
        editor.apply();
    }


    public static int isProfilePasswordSet() {
        return preferences.getInt("IS_VENDOR_PROFILE_PASSWORD_SET", 0);
    }

    public static void setVendorConfirmPassword(boolean is) {
        editor.putBoolean("IS_VENDOR_PROFILE_PASSWORD_CONFIRM", is);
        editor.apply();
    }

    public static boolean isVendorConfirmPassword() {
        return preferences.getBoolean("IS_VENDOR_PROFILE_PASSWORD_CONFIRM", false);
    }

    public static String getUpcommingItinerary() {
        return preferences.getString("USER_UP_COMMING_ITINERARY", "");
    }

    public static String getPastItinerary() {
        return preferences.getString("USER_PAST_ITINERARY", "");
    }

    public static void setUpcommingItinerary(String s) {
        editor.putString("USER_UP_COMMING_ITINERARY", s);
        editor.apply();
    }

    public static void setPastItinerary(String s) {
        editor.putString("USER_PAST_ITINERARY", s);
        editor.apply();
    }

    public static void setSlug(String venue_slug) {
        editor.putString("VENUE_SLUG", venue_slug);
        editor.apply();
    }

    public static void  setDepositOption(String option) {
        editor.putString("DepositOption", option);
        editor.apply();
    }

    public static void setDepositPercentage (String percentage) {
        editor.putString("DepositPercentage", percentage);
        editor.apply();
    }

    public static void setMinPax (int pax) {
        editor.putInt("minPax", pax);
        editor.apply();
    }


    public static String getSlug(){
        return preferences.getString("VENUE_SLUG","");
    }

    public static String getDepositOption(){
        return preferences.getString("DepositOption","");
    }
    public static int getMinPax(){
        return preferences.getInt("minPax",0);
    }
    public static String getDepositPercentage(){
        return preferences.getString("DepositPercentage","");
    }
    public static String getDOB(){
        return preferences.getString("DOB","");
    }
    public static String getCountry(){
        return preferences.getString("COUNTRY","");
    }

    public static void setDOB(String date_of_birth) {
        editor.putString("DOB", date_of_birth);
        editor.apply();
    }

    public static void setCountry(String country) {
        editor.putString("COUNTRY", country);
        editor.apply();
    }

    public static  void clean(){
        editor.clear();
        editor.apply();
    }
}
