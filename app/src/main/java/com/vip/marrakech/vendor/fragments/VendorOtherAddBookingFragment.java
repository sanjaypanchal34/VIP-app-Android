package com.vip.marrakech.vendor.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.SPAdapter;
import com.vip.marrakech.admin.adapters.AdminItineraryDayDataBottleAdapter;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.dialog.SearchConciergeDialog;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorOtherAddBookingFragment extends Fragment implements OtherBookingActivity.OnFragmentViewPagerChangeListener, OnCallBackListener, View.OnClickListener, MaterialSpinner.OnExpandListener {


    private Communication communication;
    private EditText edt_first_name, edt_last_name, edt_email, edt_mobile, edt_spend, edt_desc;
    private MaterialSpinner sp_pax, sp_table;
    private TextView tv_date, tv_time, sp_c_name;
    private Button btn_save;
    private Calendar myCalendar = Calendar.getInstance();
    List<String> paxList = new ArrayList<>();
    private ImageView iv_add_bottle;
    private RecyclerView rv_bottle;
    private AdminItineraryDayDataBottleAdapter bottleAdapter;
    private List<GetItineraryDayBottleDetail> bottleList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_other_add_booking, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);

        for (int i = 1; i <= 30; i++) {
            paxList.add(String.valueOf(i));
        }


        initUI();

        btn_save.setOnClickListener(this);
        sp_pax.setOnClickListener(this);
        sp_table.setOnClickListener(this);
        sp_c_name.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        iv_add_bottle.setOnClickListener(this);

        sp_pax.setAdapter(new SPAdapter<String>(getActivity(), paxList) {
            @Override
            protected String setSpinnerText(String item) {
                return item;
            }
        });

        sp_table.setAdapter(new SPAdapter<String>(getActivity(), Arrays.asList(getResources().getStringArray(R.array.select_table_type))) {
            @Override
            protected String setSpinnerText(String item) {
                return item;
            }
        });
        sp_pax.setOnExpandListener(this);
                sp_table.setOnExpandListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI() {
        Toolbar toolBar = getView().findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        edt_first_name = getView().findViewById(R.id.edt_first_name);
        edt_last_name = getView().findViewById(R.id.edt_last_name);
        edt_email = getView().findViewById(R.id.edt_email);
        edt_mobile = getView().findViewById(R.id.edt_mobile);
        sp_pax = getView().findViewById(R.id.sp_pax);
        tv_date = getView().findViewById(R.id.tv_date);
        tv_time = getView().findViewById(R.id.tv_time);
        sp_table = getView().findViewById(R.id.sp_table_type);
        sp_c_name = getView().findViewById(R.id.sp_c_name);
        edt_spend = getView().findViewById(R.id.edt_spend);
        edt_desc = getView().findViewById(R.id.edt_desc);
        iv_add_bottle = getView().findViewById(R.id.iv_add_bottle);
        rv_bottle = getView().findViewById(R.id.rv_bottle);
        rv_bottle.setLayoutManager(new LinearLayoutManager(getActivity()));
        bottleList.add(new GetItineraryDayBottleDetail());
        bottleAdapter = new AdminItineraryDayDataBottleAdapter(getActivity(), bottleList, SessionManager.getVenueID().isEmpty()?"1":SessionManager.getVenueID(), new AdminItineraryDayDataBottleAdapter.OnBottleSelectListener() {
            @Override
            public void onBottleSelect(MasterBottelModel item, int pos) {
                bottleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBottleDelete(int adapterPosition) {
                bottleAdapter.notifyDataSetChanged();
            }
        });
        rv_bottle.setAdapter(bottleAdapter);
        btn_save = getView().findViewById(R.id.btn_save);
    }

    @Override
    public void onRefresh(int page, String name) {

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {

    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: {
                if (Validator.isEmpty(edt_first_name)) {
                    Validator.setError(edt_first_name, "Enter First Name");
                } else if (Validator.isEmpty(edt_last_name)) {
                    Validator.setError(edt_last_name, "Enter Last Name");
                } else if (Validator.isNotEmail(edt_email)) {
                    Validator.setError(edt_email, "Enter Valid Email");
                } else if (Validator.isNotMinLength(edt_mobile, 11)) {
                    Validator.setError(edt_mobile, "Enter Valid Mobile");
                } else if (Validator.isEmpty(sp_pax)) {
                    Toast.makeText(getActivity(), "Select Group Size", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(tv_date)) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(tv_time)) {
                    Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(sp_table)) {
                    Toast.makeText(getActivity(), "Select Table No", Toast.LENGTH_SHORT).show();
                } else if (sp_c_name.getTag() == null) {
                    Toast.makeText(getActivity(), "Select Concierge", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "vendor/book/now");
                        param.put("venue_type", SessionManager.getVenue_Type());
                        param.put("vendor_id", SessionManager.getID());
                        param.put("concierge_id", sp_c_name.getTag().toString());
                        param.put("first_name", Validator.getText(edt_first_name));
                        param.put("last_name", Validator.getText(edt_last_name));
                        param.put("email", Validator.getText(edt_email));
                        param.put("phone_no", Validator.getText(edt_mobile));
                        param.put("pax", Validator.getText(sp_pax));
                        SimpleDateFormat apiDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        param.put("booking_date", finalFormate.format(apiDateFormat.parse(Validator.getText(tv_date))));
                        SimpleDateFormat hms = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
                        SimpleDateFormat hm = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                        param.put("booking_time", hms.format(hm.parse(Validator.getText(tv_time))));
                        param.put("table_type", Validator.getText(sp_table));
                        param.put("table_spend", Validator.getText(edt_spend));
                        param.put("description", Validator.getText(edt_desc));
                        communication.callPOST(param);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
          /*  case R.id.sp_pax: {
                sp_pax.select();
            }
            break;
            case R.id.sp_table: {

                sp_table.select();
            }
            break;*/
            case R.id.sp_c_name: {
                SearchConciergeDialog dialog = new SearchConciergeDialog();
                dialog.setListener(new SearchConciergeDialog.OnSearchListener() {
                    @Override
                    public void onConciergeSelect(String id, String title, String email) {
                        Log.e(title, id);
                        sp_c_name.setText(title);
                        sp_c_name.setTag(id);
                    }
                });
                dialog.show(getChildFragmentManager(), "Concierge");
            }
            break;
            case R.id.tv_date: {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat apiDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        tv_date.setText(apiDateFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
            break;
            case R.id.tv_time: {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tv_time.setText(String.format(Locale.ENGLISH, "%d:%d", hourOfDay, minute));
                            }
                        }, myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH), false);
                timePickerDialog.show();
            }
            break;
            case R.id.iv_add_bottle: {
                if (bottleList.size() == 0) {
                    bottleList.add(new GetItineraryDayBottleDetail());
                } else {
                    GetItineraryDayBottleDetail data = bottleList.get(bottleList.size() - 1);
                    if (data.getBottleType().isEmpty() || data.getBottleName().isEmpty()) {
                        Toast.makeText(getActivity(), "Select Bottle Type or Name", Toast.LENGTH_SHORT).show();
                    } else {
                        bottleList.add(new GetItineraryDayBottleDetail());
                    }
                }
                bottleAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onExpand() {
        hideKeyboard(getActivity());
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
