package com.vip.marrakech.vendor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.adapters.DailyAvailabilityAdapter;
import com.vip.marrakech.vendor.dialog.MyNumberPicker;
import com.vip.marrakech.vendor.models.DateAvailabilityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends Fragment implements OnCallBackListener, DailyAvailabilityAdapter.onAvailabilityListener {

    private RecyclerView rv_daily;
    private List<DateAvailabilityModel> list = new ArrayList<>();
    private DailyAvailabilityAdapter adapter;
    private Communication communication;
    private Button btn_save;

    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);

        HashMap<String, String> param = new HashMap<>();
        param.put("action", "vendor/availability/list");
        param.put("venue_slug", SessionManager.getSlug());
        param.put("vendor_id", SessionManager.getID());
        communication.callPOST(param);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "vendor/availability/update");
                param.put("venue_slug", SessionManager.getSlug());
                param.put("vendor_id", SessionManager.getID());
                HashMap<String, String> daily = new HashMap<>();
                for (DateAvailabilityModel model :
                        list) {
                    daily.put(model.getDate(), model.getTime());
                }
                param.put("daily", new Gson().toJson(daily));
                Log.e("PARAM:::", param.toString());
                communication.callPOST(param);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        btn_save = view.findViewById(R.id.btn_save);
        rv_daily = view.findViewById(R.id.rv_daily);
        rv_daily.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DailyAvailabilityAdapter(getActivity(), list);
        rv_daily.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {
        if (tag.equals("vendor/availability/update")) {
            Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray daily_Availability = data.getJSONArray("daily_Availability");
            for (int i = 0; i < daily_Availability.length(); i++) {
                JSONObject value = daily_Availability.getJSONObject(i);
                DateAvailabilityModel model = new DateAvailabilityModel(value.getString("value"), value.getString("name"), value.getString("display"));
                list.add(model);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onDateClick(DateAvailabilityModel model, int adapterPosition) {

    }

    @Override
    public void onTimeClick(DateAvailabilityModel model, int adapterPosition) {
        MyNumberPicker dialog = new MyNumberPicker(getActivity(), 0, 10000, new NumberPickerDialog.NumberPickerCallBack() {
            @Override
            public void onSelectingValue(int value) {
                model.setTime(String.valueOf(value));
                adapter.notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    @Override
    public void onConciergeDelete(DateAvailabilityModel model, int adapterPosition) {

    }
}
