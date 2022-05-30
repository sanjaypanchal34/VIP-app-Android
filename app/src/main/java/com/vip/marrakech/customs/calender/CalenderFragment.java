package com.vip.marrakech.customs.calender;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vip.marrakech.R;
import com.vip.marrakech.user.models.MyDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderFragment extends Fragment implements CustomCalenderView.OnMonthChangeListener {

    private TextView tv_month;
    private RecyclerView rv_calender;
    Calendar cal;
    Calendar calMin;
    Calendar calMax;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private CalenderAdapter adapter;
    private Calendar selected;
    private MyDate chooseDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cal = (Calendar) getArguments().getSerializable("cal");
        calMin = (Calendar) getArguments().getSerializable("min");
        calMax = (Calendar) getArguments().getSerializable("max");
        selected = (Calendar) getArguments().getSerializable("selected");
        initUI(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        tv_month = view.findViewById(R.id.tv_month);
        rv_calender = view.findViewById(R.id.rv_calender);
        rv_calender.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        List<MyDate> dayValueInCells = new ArrayList<MyDate>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(new MyDate(mCal.getTime(), isSelected(mCal)));
            mCal.add(Calendar.DAY_OF_MONTH, 1);

        }

        adapter = new CalenderAdapter(getActivity(), dayValueInCells, cal, calMin,
                calMax);
        adapter.setListener(new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate date) {
                adapter.notifyDataSetChanged();
                chooseDate = date;
                if (listener != null) {
                    listener.onDateSelect(chooseDate == null ? new MyDate(selected.getTime(), true) : chooseDate);
                }
            }
        });
        rv_calender.setAdapter(adapter);

        SimpleDateFormat month_date = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        String month_name = month_date.format(cal.getTime());
        tv_month.setText(month_name);

    }

    private boolean isSelected(Calendar mCal) {
      if (mCal == null){
          return false;
      }else {
        return false;
      }

    }

    private OnCalenderListener listener;

    public void setListener(OnCalenderListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSetMonth(Calendar cal) {
        List<MyDate> dayValueInCells = new ArrayList<MyDate>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(new MyDate(mCal.getTime(), isSelected(mCal)));
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapter = new CalenderAdapter(getActivity(), dayValueInCells, cal, calMin, calMax);
        adapter.setListener(new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate date) {
                adapter.notifyDataSetChanged();
                chooseDate = date;
                if (listener != null) {
                    listener.onDateSelect(chooseDate == null ? new MyDate(selected.getTime(), true) : chooseDate);
                }
            }
        });
        rv_calender.setAdapter(adapter);

        SimpleDateFormat month_date = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        String month_name = month_date.format(cal.getTime());
        tv_month.setText(month_name);
    }
}
