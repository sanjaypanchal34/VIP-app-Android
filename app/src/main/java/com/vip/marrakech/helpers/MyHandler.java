package com.vip.marrakech.helpers;

import android.os.Handler;

public class MyHandler {

    private Runnable runnable;
    private Handler handler;

    public  MyHandler(final OnHandlerListener listener) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                listener.onRun();
            }
        };

    }

    public void setDelay(long time) {
        handler.postDelayed(runnable, time);
    }

    public void revoke() {
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }

    public interface OnHandlerListener {
        void onRun();
    }
}
