package com.vip.marrakech.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vip.marrakech.R;

import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class SorryDialog extends DialogFragment {
    private TextView tv_msg, tv_slot1, tv_slot2, tv_ok;
    private List<String> slot1, slot2;
    private String msg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sorry, container, false);
    }

    public void setData(String msg, List<String> slot1, List<String> slot2) {
        this.msg = msg;
        this.slot1 = slot1;
        this.slot2 = slot2;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv_msg = view.findViewById(R.id.tv_msg);
        tv_slot1 = view.findViewById(R.id.tv_slot1);
        tv_slot2 = view.findViewById(R.id.tv_slot2);
        tv_ok = view.findViewById(R.id.tv_ok);


        tv_msg.setText(msg);


        for (String s : slot1) {
            tv_slot1.append(s);
            tv_slot1.append("\n");
        }
        for (String s : slot2) {
            tv_slot2.append(s);
            tv_slot2.append("\n");
        }


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener!=null){
                    listener.onOkCLick();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener{
        void onOkCLick();
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            Window window = getDialog().getWindow();
            Point size = new Point();
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            window.setLayout((int) (size.x * 0.95),ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);//solves issue with statusbar
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        }

    }
}
