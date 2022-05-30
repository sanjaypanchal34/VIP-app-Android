package com.vip.marrakech.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vip.marrakech.R;

import java.util.Locale;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class ProgressDialog extends DialogFragment {


    private TextView tv_title, tv_progress;
    private Button tv_cancel;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tv_title = view.findViewById(R.id.tv_title);
        progress = view.findViewById(R.id.progress);
        tv_progress = view.findViewById(R.id.tv_progress);
        tv_cancel = view.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDownloadCancel();
                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void setProgress(int p) {
        Log.e("Progress::", String.valueOf(p));
        if (progress != null) {
            progress.setProgress(p);
            tv_progress.setText(String.format(Locale.ENGLISH, "%d", p));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        getDialog().setCancelable(false);
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout( (int)(size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT );
        window.setGravity( Gravity.CENTER );
    }

    private OnProgressCancelListener listener;

    public void setListener(OnProgressCancelListener listener) {
        this.listener = listener;
    }

    public interface OnProgressCancelListener{
        void onDownloadCancel();
    }
}
