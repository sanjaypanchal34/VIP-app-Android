package com.vip.marrakech.vendor.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.AddOtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingDetailActivity;
import com.vip.marrakech.vendor.adapters.OtherBookingAdapter;
import com.vip.marrakech.vendor.models.OtherBookingModel;
import com.vip.marrakech.vendor.models.VendorBookingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorOtherBookingFragment extends Fragment implements OnCallBackListener, OtherBookingActivity.OnFragmentViewPagerChangeListener, Toolbar.OnMenuItemClickListener {


    private RecyclerView rv_bookings;
    private OtherBookingAdapter adapter;
    private List<OtherBookingModel> list = new ArrayList<>();
    private Communication communication;
    private String filter = "";

    public VendorOtherBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_other_booking, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(),this);
        Toolbar toolBar = view.findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
        rv_bookings = view.findViewById(R.id.rv_bookings);
        rv_bookings.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OtherBookingAdapter(getActivity(),list);
        adapter.setListener(new OtherBookingAdapter.OnBookingListener() {
            @Override
            public void onBookingClick(OtherBookingModel model) {
                Bundle bundle = new Bundle();
                bundle.putString("id",model.getEncrypted_booking_id());
                GoTo.startWithExtra(getActivity(), OtherBookingDetailActivity.class,bundle);
            }
        });
        rv_bookings.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);

    }

    private void getBookings(int filter, HashMap<String, String> param) {
        param.put("action", "vendor/all/other/bookings");
        param.put("filter", String.valueOf(filter));
        if (communication != null)
            communication.callGET(param);

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/all/other/bookings")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray bookings = data.getJSONArray("all_other_booking");
                list.clear();
                for (int i = 0; i < bookings.length(); i++) {
                    OtherBookingModel model = new Gson().fromJson(bookings.getJSONObject(i).toString(), OtherBookingModel.class);
                    list.add(model);
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

    @Override
    public void onRefresh(int page, String name) {
        Log.e("TITLE:::",name);
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getBookings(filter.isEmpty() ? 0 : 1, param);
    }


    @Override
    public void onResume() {
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getBookings(filter.isEmpty() ? 0 : 1, param);
        super.onResume();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_add){
            GoTo.start(getActivity(), AddOtherBookingActivity.class);
        }
        return false;
    }
}
