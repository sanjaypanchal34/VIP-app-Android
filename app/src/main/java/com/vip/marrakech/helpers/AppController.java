package com.vip.marrakech.helpers;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.multidex.MultiDex;

import com.downloader.PRDownloader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vip.marrakech.R;
import com.vip.marrakech.user.activities.LatestMixesActivity;
import com.vip.marrakech.user.models.MixesModel;
import com.vip.marrakech.vendor.player.AudioServiceBinder;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private static AlertDialog internetDialog;
    private static SimpleExoPlayer exoPlayer;
    private static MixesModel model;

    public static Context getInstance() {
        return mInstance;
    }

    public static SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public static void initEXO(){
        exoPlayer = new SimpleExoPlayer.Builder(mInstance).build();
        exoPlayer.setPlayWhenReady(true);
    }

    public static void resetEXO(){
        if (exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();

        }
        exoPlayer = null;
    }

    public static MixesModel getLastMixes() {
        return model;
    }

    public static void setLastMixes(MixesModel model) {
        AppController.model = model;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        PRDownloader.initialize(getApplicationContext());
        Fresco.initialize(this);
        FirebaseApp.initializeApp(this);
        //   MultiDex.install(this);
        new SessionManager(this);
        Thread.setDefaultUncaughtExceptionHandler(new CustomizedExceptionHandler(
                "/mnt/sdcard/"));
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();

                            Log.e("AAAA", "Refreshed token: " + token);
                            SessionManager.saveFSMToken(token);
                        }
                    }
                });

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

// combines above two lines
        imagePipeline.clearCaches();

    }

    public static void initNoInternet(Context context) {
        if (internetDialog != null && !internetDialog.isShowing()) {
            internetDialog = new AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).create();

        }else if (internetDialog==null){
            internetDialog = new AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
        }


    }


    public static void showInternetDialog(String msg) {
        if (internetDialog != null && !internetDialog.isShowing()) {
            internetDialog.setMessage(msg);
            internetDialog.show();
        }
    }
}
