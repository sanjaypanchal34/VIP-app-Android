package com.vip.marrakech.vendor.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vip.marrakech.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "ActionBottomDialog";
    BottomSheetBehavior bottomSheetBehavior;
    private TextView tv_set, tv_start_date, tv_end_date,tv_to,tv_reset;
    private CheckBox cb_today, cb_week, cb_month, cb_custom, cb_year;
    private Calendar myCalendar = Calendar.getInstance();
    private String startDate = "";
    private String endDate = "";
    private ItemClickListener listener;
    private String filter = "";
//    LayoutBottomSheetBinding bi;

    public void setData(String filter, ItemClickListener listener) {
        this.filter = filter;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        cb_today.setOnCheckedChangeListener(this);
        cb_week.setOnCheckedChangeListener(this);
        cb_month.setOnCheckedChangeListener(this);
        cb_year.setOnCheckedChangeListener(this);
        cb_custom.setOnCheckedChangeListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        tv_set.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        switch (filter) {
            case "1": {
                cb_today.setChecked(true);
                tv_start_date.setVisibility(View.GONE);
                tv_end_date.setVisibility(View.GONE);
                 tv_to.setVisibility(View.GONE);
            }
            break;
            case "2": {
                cb_week.setChecked(true);
                tv_start_date.setVisibility(View.GONE);
                tv_end_date.setVisibility(View.GONE);
                 tv_to.setVisibility(View.GONE);
            }
            break;
            case "3": {
                cb_month.setChecked(true);
                tv_start_date.setVisibility(View.GONE);
                tv_end_date.setVisibility(View.GONE);
                 tv_to.setVisibility(View.GONE);
            }
            break;
            case "4": {
                cb_year.setChecked(true);
                tv_start_date.setVisibility(View.GONE);
                tv_end_date.setVisibility(View.GONE);
                 tv_to.setVisibility(View.GONE);
            }
            break;
            case "5": {
                cb_custom.setChecked(true);
                tv_start_date.setVisibility(View.VISIBLE);
                tv_end_date.setVisibility(View.VISIBLE);
                 tv_to.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    private void initUI(View view) {
        tv_reset = view.findViewById(R.id.tv_reset);
        tv_to = view.findViewById(R.id.tv_to);
        tv_set = view.findViewById(R.id.tv_set);
        tv_start_date = view.findViewById(R.id.tv_start_date);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        cb_today = view.findViewById(R.id.cb_today);
        cb_week = view.findViewById(R.id.cb_week);
        cb_month = view.findViewById(R.id.cb_month);
        cb_year = view.findViewById(R.id.cb_year);
        cb_custom = view.findViewById(R.id.cb_custom);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_date: {
                if (cb_custom.isChecked()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, sStartDate, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
            break;
            case R.id.tv_end_date: {
                if (cb_custom.isChecked()) {
                    if (!startDate.isEmpty()) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, sEndDate, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                        datePickerDialog.show();
                    } else {
                        Toast.makeText(getContext(), "Please select start date", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case R.id.tv_set: {
                if (listener != null) {
                    if (cb_today.isChecked()) {
                        listener.onItemClick("1");
                        dismiss();
                    } else if (cb_week.isChecked()) {
                        listener.onItemClick("2");
                        dismiss();
                    } else if (cb_month.isChecked()) {
                        listener.onItemClick("3");
                        dismiss();
                    } else if (cb_year.isChecked()) {
                        listener.onItemClick("4");
                        dismiss();
                    } else if (cb_custom.isChecked()) {
                        if (startDate.isEmpty()) {
                            Toast.makeText(getContext(), "Please select start date", Toast.LENGTH_SHORT).show();
                        } else if (endDate.isEmpty()) {
                            Toast.makeText(getContext(), "Please select end date", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.onItemClick("5", startDate, endDate);
                            dismiss();
                        }
                    } else {
                        listener.onItemClick("");
                        dismiss();
                    }
                }
            }
            break;

            case R.id.tv_reset: {
                cb_today.setChecked(false);
                cb_week.setChecked(false);
                cb_month.setChecked(false);
                cb_year.setChecked(false);
                cb_custom.setChecked(false);
                tv_start_date.setVisibility(View.GONE);
                tv_end_date.setVisibility(View.GONE);
                tv_to.setVisibility(View.GONE);
                listener.onItemClick("");
                dismiss();
            }
            break;
        }
    }

    DatePickerDialog.OnDateSetListener sStartDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateScheduleDate();
        }
    };

    DatePickerDialog.OnDateSetListener sEndDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDate();
        }
    };

    private void updateScheduleDate() {
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat diaplayDateFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        String date = diaplayDateFormat.format(myCalendar.getTime());
        startDate = apiDateFormat.format(myCalendar.getTime());
        tv_start_date.setText(date);
    }

    private void updateEndDate() {
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat diaplayDateFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        String date = diaplayDateFormat.format(myCalendar.getTime());
        endDate = apiDateFormat.format(myCalendar.getTime());
        tv_end_date.setText(date);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.cb_today: {
                    cb_today.setChecked(b);
                    cb_week.setChecked(false);
                    cb_month.setChecked(false);
                    cb_year.setChecked(false);
                    cb_custom.setChecked(false);
                    tv_start_date.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.GONE);
                    tv_to.setVisibility(View.GONE);
                }
                break;
                case R.id.cb_week: {
                    cb_week.setChecked(b);
                    cb_today.setChecked(false);
                    cb_month.setChecked(false);
                    cb_year.setChecked(false);
                    cb_custom.setChecked(false);
                    tv_start_date.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.GONE);
                    tv_to.setVisibility(View.GONE);
                }
                break;
                case R.id.cb_month: {
                    cb_month.setChecked(b);
                    cb_week.setChecked(false);
                    cb_today.setChecked(false);
                    cb_year.setChecked(false);
                    cb_custom.setChecked(false);
                    tv_start_date.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.GONE);
                    tv_to.setVisibility(View.GONE);
                }
                break;
                case R.id.cb_year: {
                    cb_year.setChecked(b);
                    cb_week.setChecked(false);
                    cb_month.setChecked(false);
                    cb_today.setChecked(false);
                    cb_custom.setChecked(false);
                    tv_start_date.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.GONE);
                    tv_to.setVisibility(View.GONE);
                }
                break;
                case R.id.cb_custom: {
                    cb_custom.setChecked(b);
                    cb_week.setChecked(false);
                    cb_month.setChecked(false);
                    cb_year.setChecked(false);
                    cb_today.setChecked(false);
                    tv_start_date.setVisibility(View.VISIBLE);
                    tv_end_date.setVisibility(View.VISIBLE);
                    tv_to.setVisibility(View.VISIBLE);
                }
                break;
            }
        }else {
            tv_start_date.setVisibility(View.GONE);
            tv_end_date.setVisibility(View.GONE);
            tv_to.setVisibility(View.GONE);
        }
    }

    public interface ItemClickListener {
        void onItemClick(String booking);

        void onItemClick(String booking, String startDate, String endDate);
    }
}