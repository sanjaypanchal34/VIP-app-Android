package com.vip.marrakech.helpers;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Validator {



    public static boolean isNotEmail(EditText editText){
         boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
         if (b){
             editText.setError(null);
         }
         return !b;

    }

    public static boolean isNotEmail(TextView editText){
         boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
         if (b){
             editText.setError(null);
         }
         return !b;

    }

    public static boolean isEmpty(EditText editText){
         boolean b = editText.getText().toString().trim().isEmpty();
         if (!b){
             editText.setError(null);
         }
         return b;
    }

    public static boolean isEmpty(TextView editText){
         boolean b = editText.getText().toString().trim().isEmpty();
         if (!b){
             editText.setError(null);
         }
         return b;
    }

    public static boolean isNotMinLength(EditText editText,int min){
      /*   boolean b = editText.getText().toString().trim().length()>= min;
         if (!b){
             editText.setError(null);
         }
         return !b;*/

        boolean b = editText.getText().toString().trim().isEmpty();
        if (!b){
            editText.setError(null);
        }
        return b;
    }

    public static String getText(EditText editText){
        return editText.getText().toString().trim();
    }


    public static String getText(TextView textview){
        return textview.getText().toString().trim();
    }


    public static void setError(EditText editText,String error){
        editText.requestFocus();
        editText.setError(error);
    }

    public static void setError(TextView editText,String error){
        editText.requestFocus();
        editText.setError(error);
    }

    public static Date getDate(Date date){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        calendar.set(Calendar.YEAR,year);
//        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
//        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        Log.e("DATE:::",calendar.getTime().toString());
        return calendar.getTime();
    }


    public static String getAmPM(String hhmm){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date dt = null;
       if (!hhmm.isEmpty()){
           try {
               dt = sdf.parse(hhmm);
               if (dt != null) {
                   return sdfs.format(dt);
               }
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }

        return "";


    }

    public static String getHHMM(String hhmma){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date dt = null;
        try {
            dt = sdfs.parse(hhmma);
            if (dt != null) {
                return sdf.format(dt);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";


    }

    public static String getFormattedNumber(String number){
        if(!number.isEmpty()) {
            double val = Double.parseDouble(number);
            return String.format("%s", NumberFormat.getNumberInstance(Locale.US).format(val));
        }else{
            return "0";
        }
    }
}
