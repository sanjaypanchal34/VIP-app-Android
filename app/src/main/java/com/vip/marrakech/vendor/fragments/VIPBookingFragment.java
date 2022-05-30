package com.vip.marrakech.vendor.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.adapters.AvailabilityAdapter;
import com.vip.marrakech.vendor.dialog.MyNumberPicker;
import com.vip.marrakech.vendor.models.DateAvailabilityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class VIPBookingFragment extends Fragment implements AvailabilityAdapter.onAvailabilityListener, OnCallBackListener {


    private RecyclerView rv_date;
    private List<DateAvailabilityModel> list = new ArrayList<>();
    private AvailabilityAdapter adapter;
    private FloatingActionButton tv_add;
    private Calendar myCalendar = Calendar.getInstance();
    private String type;
    private Communication communication;
    private Button btn_save;

    public VIPBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vipbooking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        Bundle bundle = getArguments();
        type = bundle.getString("type");

        initUI(view);

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (SessionManager.getVendorSubscriptionStatus().equalsIgnoreCase("Active") && type.equalsIgnoreCase("Other") || type.equalsIgnoreCase("VIP")) {
                    if (list.size() == 0) {
                        list.add(new DateAvailabilityModel("", "", ""));
                    } else if (!list.get(list.size() - 1).getDate().isEmpty() && !list.get(list.size() - 1).getTime().isEmpty()) {
                        list.add(new DateAvailabilityModel("", "", ""));
                    } else {
                        Toast.makeText(getActivity(), "Add Last Date and Time", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showAlert();
                }*/
                if (list.size() == 0) {
                    list.add(new DateAvailabilityModel("", "", ""));
                } else if (!list.get(list.size() - 1).getDate().isEmpty() && !list.get(list.size() - 1).getTime().isEmpty()) {
                    list.add(new DateAvailabilityModel("", "", ""));
                } else {
                    Toast.makeText(getActivity(), "Add Last Date and Time", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });
        HashMap<String, String> param = new HashMap<>();
//        param.put("action", String.format("vendor/availability/list/%s", type));
        param.put("action", "vendor/availability/list");
        param.put("venue_slug", SessionManager.getSlug());
        param.put("vendor_id", SessionManager.getID());
        communication.callPOST(param);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      if (SessionManager.getVendorSubscriptionStatus().equalsIgnoreCase("Active") && type.equalsIgnoreCase("Other") || type.equalsIgnoreCase("VIP")) {
                    Log.e("UPDATE::;", new Gson().toJson(list));
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "vendor/availability/update");
                    param.put("booking_type", type);
                    param.put("exception", new Gson().toJson(list));
                    param.put("daily_count", Validator.getText(tv_daily).isEmpty() ? "0" : Validator.getText(tv_daily));
                    Log.e("PARAM:::", param.toString());
                    communication.callPOST(param);

                }else {
                    showAlert();
                }*/
                Log.e("UPDATE::;", new Gson().toJson(list));
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "vendor/availability/update");
                param.put("venue_slug", SessionManager.getSlug());
                param.put("vendor_id", SessionManager.getID());
//                param.put("booking_type", type);
                param.put("exception", new Gson().toJson(list));
                Log.e("PARAM:::", param.toString());
                communication.callPOST(param);

            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void showAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage("To enable Live booking please upgrade your plan first.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void initUI(View view) {
        btn_save = view.findViewById(R.id.btn_save);
        tv_add = view.findViewById(R.id.tv_add);
        rv_date = view.findViewById(R.id.rv_date);
        rv_date.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AvailabilityAdapter(getActivity(), list);
        adapter.setListener(this);
        rv_date.setAdapter(adapter);
    }

    @Override
    public void onDateClick(final DateAvailabilityModel model, int adapterPosition) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                model.setDate(apiDateFormat.format(myCalendar.getTime()));
                adapter.notifyDataSetChanged();
            }
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onTimeClick(final DateAvailabilityModel model, int adapterPosition) {

        MyNumberPicker dialog = new MyNumberPicker(getActivity(), 0, 10000, new NumberPickerDialog.NumberPickerCallBack() {
            @Override
            public void onSelectingValue(int value) {
                model.setTime(String.valueOf(value));
                adapter.notifyDataSetChanged();
            }
        });

        dialog.show();
     /*
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                       model.setTime(String.format(Locale.ENGLISH, "%d:%d", hourOfDay, minute));
                       adapter.notifyDataSetChanged();
                    }
                }, myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog.show();*/
    }

    @Override
    public void onConciergeDelete(DateAvailabilityModel model, int adapterPosition) {
      /*  if (SessionManager.getVendorSubscriptionStatus().equalsIgnoreCase("Active") && type.equalsIgnoreCase("Other") || type.equalsIgnoreCase("VIP")) {

        }else {
            showAlert();
        }*/
        if (model.getId().isEmpty()) {
            list.remove(adapterPosition);
            adapter.notifyDataSetChanged();
        } else {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", String.format("vendor/availability/delete/%s", model.getId()));
            communication.callDelete(param);
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/availability/list")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray exception_Availability = data.getJSONArray("exception_Availability");
                list.clear();
                for (int i = 0; i < exception_Availability.length(); i++) {
                    list.add(new DateAvailabilityModel(exception_Availability.getJSONObject(i).getString("availability_count"), exception_Availability.getJSONObject(i).getString("availability_date"), exception_Availability.getJSONObject(i).getString("encrypted_exception_availability_id")));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("vendor/availability/update")) {
            Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "vendor/availability/list");
            param.put("venue_slug", SessionManager.getSlug());
            param.put("vendor_id", SessionManager.getID());
            communication.callPOST(param);
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
