package com.vip.marrakech.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class GoTo {


    private static String EXTRA = "EXTRA_BUNDLE";

    public static void start(@NonNull Context context, @NonNull Class aClass) {
        context.startActivity(new Intent(context, aClass));
    }


    public static void startWithFinish(@NonNull Context context, @NonNull Class aClass) {
        context.startActivity(new Intent(context, aClass));
        ((Activity) context).finish();
    }


    public static void startWithExtraFinish(@NonNull Context context, @NonNull Class aClass, @NonNull Bundle bundle) {
        context.startActivity(new Intent(context, aClass).putExtra(EXTRA,bundle));
        ((Activity) context).finish();
    }


    public static void startWithExtra(@NonNull Context context, @NonNull Class aClass, @NonNull Bundle bundle) {
        context.startActivity(new Intent(context, aClass).putExtra(EXTRA,bundle));
    }

    public static void startWithClearTop(@NonNull Context context, @NonNull Class aClass) {
        Intent intent = new Intent(context, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void startWithExtraClearTop(@NonNull Context context, @NonNull Class aClass,Bundle bundle) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra(EXTRA,bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    public static Bundle getIntent(@NonNull Context context){
        return ((Activity)context).getIntent().getBundleExtra(EXTRA);
    }

}
