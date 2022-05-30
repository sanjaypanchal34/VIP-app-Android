package com.vip.marrakech.customs.calender;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.vip.marrakech.R;
import com.vip.marrakech.user.models.MyDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomCalenderView extends LinearLayout {
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private ViewPager myViewPager;
    private ViewPagerAdapter adapter;

    int lastVisitedPage = 0;
    private Date maxDate, minDate;
    private Date selectedDate;
    private MyDate chooseDate;

    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI(context, attrs);
    }


    public CustomCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context, attrs);
    }

    private void initUI(Context context, AttributeSet attrs) {
        lastVisitedPage = Calendar.getInstance().get(Calendar.YEAR) - 1970;
        View view = LayoutInflater.from(context).inflate(R.layout.custom_calender, this);
        myViewPager = view.findViewById(R.id.myViewPager);
        myViewPager.setOffscreenPageLimit(0);
        Button btn_done = view.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (chooseDate != null) {
                        if (chooseDate.getDate() == null) {
                            if (selectedDate != null)
                                listener.onDateSelect(new MyDate(selectedDate, true));
                        } else {
                            listener.onDateSelect(chooseDate);
                        }
                    } else {
                        if (selectedDate != null)
                            listener.onDateSelect(new MyDate(selectedDate, true));
                    }
                }
            }
        });
        myViewPager.post(new Runnable() {
            @Override
            public void run() {
                myViewPager.setCurrentItem(lastVisitedPage, false);
                myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        OnMonthChangeListener listener = (OnMonthChangeListener) adapter.instantiateItem(myViewPager, position);
                        if (lastVisitedPage < position) {
                            cal.add(Calendar.MONTH, 1);
                        } else {
                            cal.add(Calendar.MONTH, -1);
                        }
                        lastVisitedPage = position;
                        listener.onSetMonth(cal);
                        Log.e("LAST:::", String.valueOf(lastVisitedPage));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        });




       /* myViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
               *//* OnMonthChangeListener listener = (OnMonthChangeListener) adapter.instantiateItem(myViewPager, 0);
                listener.onSetMonth(cal);*//*
            }
        },1000);*/

    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public interface OnMonthChangeListener {
        void onSetMonth(Calendar calendar);
    }

    public void setFragmentManager(FragmentManager manager, OnCalenderListener listener) {
        this.listener = listener;
        adapter = new ViewPagerAdapter(manager, 1, new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate date) {
                chooseDate = date;

            }
        });
        myViewPager.setAdapter(adapter);

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private OnCalenderListener listener;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, OnCalenderListener listener) {
            super(fm, behavior);
            this.listener = listener;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cal", cal);
            bundle.putSerializable("min", getCal3(minDate));
            bundle.putSerializable("max", getCal(maxDate));
            bundle.putSerializable("selected", getCal2(selectedDate));
            CalenderFragment fragment = new CalenderFragment();
            fragment.setListener(listener);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }


    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }


    private OnCalenderListener listener;


    private Calendar getCal(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar calc = Calendar.getInstance();
            calc.setTime(date);
            calc.add(Calendar.DATE, 1);
            return calc;
        }
    }

    private Calendar getCal3(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar calc = Calendar.getInstance();
            calc.setTime(date);
            return calc;
        }
    }


    private Calendar getCal2(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar calc = Calendar.getInstance();
            calc.setTime(date);
            return calc;
        }
    }
}
