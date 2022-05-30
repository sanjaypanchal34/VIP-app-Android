package com.vip.marrakech.dialogs;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vip.marrakech.R;
import com.vip.marrakech.customs.calender.CustomCalenderView;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.user.models.MyDate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalenderDialog extends DialogFragment {

    private Date maxDate;
    private Date minDate;
    private Date selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.dialog_calender, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        CustomCalenderView custom_calender = view.findViewById(R.id.custom_calender);

        custom_calender.setMaxDate(maxDate);
        custom_calender.setMinDate(minDate);
        custom_calender.setSelectedDate(selectedDate);
        custom_calender.setFragmentManager(getChildFragmentManager(), new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate myDate) {

                dismiss();
               if (listener != null){
                   listener.onDateSelect(myDate);
               }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), (int) (size.y * 0.65));
        window.setGravity(Gravity.CENTER);
    }

    private OnCalenderListener listener;

    public void setListener(OnCalenderListener listener) {
        this.listener = listener;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }


    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
