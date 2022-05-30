package com.vip.marrakech.admin.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.MyDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminBookingFilterDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnCallBackListener {

    public static final String TAG = "ActionBottomDialog";
    private TextView tv_set, tv_start_date, tv_end_date, tv_reset, tv_to;
    private CheckBox cb_today, cb_week, cb_month, cb_custom, cb_year;
    private Calendar myCalendar = Calendar.getInstance();
    private String startDate = "";
    private String endDate = "";
    private ItemClickListener listener;
    private String filter = "";
    private RecyclerView rv_venues;
    //    LayoutBottomSheetBinding bi;
    private DatabaseHelper databaseHelper;
    private GRecyclerAdapter<MasterVenueModel, ViewHolder> adapter;
    private List<MasterVenueModel> venueList = new ArrayList<>();
    private Communication communication;
    private List<String> selected;
    private String radio = "";
    private RadioButton rd_booked, rd_approved, rd_cancel;

    public void setData(String filter, ItemClickListener listener) {
        this.filter = filter;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_booking_filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communication = new Communication(getActivity(), this);
        databaseHelper = new DatabaseHelper(getActivity());
        initUI(view);
        HashMap<String, String> parm = new HashMap<>();
        parm.put("action", "getAllAdminVenues");
        communication.callGET(parm);
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
        tv_to = view.findViewById(R.id.tv_to);
        tv_reset = view.findViewById(R.id.tv_reset);
        rv_venues = view.findViewById(R.id.rv_venues);
        rv_venues.setVisibility(View.VISIBLE);
        rv_venues.setNestedScrollingEnabled(false);
        rv_venues.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_set = view.findViewById(R.id.tv_set);
        tv_start_date = view.findViewById(R.id.tv_start_date);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        cb_today = view.findViewById(R.id.cb_today);
        cb_week = view.findViewById(R.id.cb_week);
        cb_month = view.findViewById(R.id.cb_month);
        cb_year = view.findViewById(R.id.cb_year);
        cb_custom = view.findViewById(R.id.cb_custom);


        rd_booked = view.findViewById(R.id.rd_booked);
        rd_approved = view.findViewById(R.id.rd_approved);
        rd_cancel = view.findViewById(R.id.rd_cancel);

        rd_booked.setOnClickListener(this);
        rd_approved.setOnClickListener(this);
        rd_cancel.setOnClickListener(this);

        adapter = new GRecyclerAdapter<MasterVenueModel, ViewHolder>(getActivity(), venueList) {
            @Override
            public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_venue, viewGroup, false));
            }

            @Override
            public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<MasterVenueModel> mList) {
                viewHolder.cb_string.setText(String.format("%s(%s)", mList.get(i).getTitle(), mList.get(i).getVenue_type()));
                viewHolder.cb_string.setOnCheckedChangeListener(null);
                viewHolder.cb_string.setChecked(mList.get(i).isChecked());
                viewHolder.cb_string.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        venueList.get(viewHolder.getAdapterPosition()).setChecked(isChecked);
                        notifyDataSetChanged();
                    }
                });
            }
        };
        rv_venues.setAdapter(adapter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("getAllAdminVenues")) {
            Log.e("FILTER VENUE:::", jsonObject.toString());
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray venues = data.getJSONArray("venues");
                venueList.clear();
                for (int i = 0; i < venues.length(); i++) {
                    MasterVenueModel model = new Gson().fromJson(venues.getJSONObject(i).toString(), MasterVenueModel.class);
                    if (selected.contains(model.getId())) {
                        model.setChecked(true);
                    }
                    venueList.add(model);
                }

                switch (radio) {
                    case "A":
                        rd_approved.setChecked(true);
                        break;
                    case "B":
                        rd_booked.setChecked(true);
                        break;
                    case "C":
                        rd_cancel.setChecked(true);
                        break;
                    default: {
                        rd_approved.setChecked(false);
                        rd_booked.setChecked(false);
                        rd_cancel.setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    public void setVenueIds(List<String> selected) {
        this.selected = selected;
    }


    public void setSelectedRadio(String radio) {
        this.radio = radio;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_string;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_string = itemView.findViewById(R.id.cb_string);
        }
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
                    /*DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, sStartDate, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();*/
                    CalenderDialog dialog = new CalenderDialog();
                    dialog.setListener(new OnCalenderListener() {
                        @Override
                        public void onDateSelect(MyDate mydate) {
                            myCalendar.setTime(mydate.getDate());
                            updateScheduleDate();
                        }
                    });
                    dialog.show(getChildFragmentManager(), "date");
                }
            }
            break;
            case R.id.tv_end_date: {
                if (cb_custom.isChecked()) {
                    if (!startDate.isEmpty()) {
                       /* DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, sEndDate, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                        datePickerDialog.show();*/
                        CalenderDialog dialog = new CalenderDialog();
                        dialog.setListener(new OnCalenderListener() {
                            @Override
                            public void onDateSelect(MyDate mydate) {
                                myCalendar.setTime(mydate.getDate());
                                updateEndDate();
                            }
                        });
                        dialog.show(getChildFragmentManager(), "date");
                    } else {
                        Toast.makeText(getContext(), "Please select start date", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case R.id.tv_set: {
                if (listener != null) {
                    if (cb_today.isChecked()) {
                        listener.onItemClick("1", venueList, radio);
                        dismiss();
                    } else if (cb_week.isChecked()) {
                        listener.onItemClick("2", venueList, radio);
                        dismiss();
                    } else if (cb_month.isChecked()) {
                        listener.onItemClick("3", venueList, radio);
                        dismiss();
                    } else if (cb_year.isChecked()) {
                        listener.onItemClick("4", venueList, radio);
                        dismiss();
                    } else if (cb_custom.isChecked()) {
                        if (startDate.isEmpty()) {
                            Toast.makeText(getContext(), "Please select start date", Toast.LENGTH_SHORT).show();
                        } else if (endDate.isEmpty()) {
                            Toast.makeText(getContext(), "Please select end date", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.onItemClick("5", startDate, endDate, venueList,radio);
                            dismiss();
                        }
                    } else {
                        listener.onItemClick("", venueList,radio);
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
                rd_cancel.setChecked(false);
                rd_approved.setChecked(false);
                rd_booked.setChecked(false);
                radio="";
                for (MasterVenueModel model : venueList) {
                    model.setChecked(false);
                }
                adapter.notifyDataSetChanged();
                listener.onItemClick("", new ArrayList<>(), radio);
                dismiss();
            }
            break;

            case R.id.rd_cancel: {
                rd_cancel.setChecked(true);
                rd_approved.setChecked(false);
                rd_booked.setChecked(false);
                radio = "C";
            }
            break;
            case R.id.rd_approved: {
                rd_cancel.setChecked(false);
                rd_approved.setChecked(true);
                rd_booked.setChecked(false);
                radio = "A";
            }
            break;
            case R.id.rd_booked: {
                rd_cancel.setChecked(false);
                rd_approved.setChecked(false);
                rd_booked.setChecked(true);
                radio = "B";
            }
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
        } else {
            tv_start_date.setVisibility(View.GONE);
            tv_end_date.setVisibility(View.GONE);
            tv_to.setVisibility(View.GONE);
        }
    }

    public interface ItemClickListener {
        void onItemClick(String booking, List<MasterVenueModel> venueList, String radio);

        void onItemClick(String booking, String startDate, String endDate, List<MasterVenueModel> venueList, String radio);
    }
}