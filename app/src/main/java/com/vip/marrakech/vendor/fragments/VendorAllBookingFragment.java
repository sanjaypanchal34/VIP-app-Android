package com.vip.marrakech.vendor.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingDetailActivity;
import com.vip.marrakech.vendor.adapters.VendorAllBookingAdapter;
import com.vip.marrakech.vendor.dialog.BookingDetailDialog;
import com.vip.marrakech.vendor.dialog.FilterDialogAllBooking;
import com.vip.marrakech.vendor.dialog.FilterDialogFragment;
import com.vip.marrakech.vendor.models.VendorAllBookingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorAllBookingFragment extends Fragment implements Toolbar.OnMenuItemClickListener, OtherBookingActivity.OnFragmentViewPagerChangeListener, OnCallBackListener, View.OnClickListener, VendorAllBookingAdapter.OnBookingListener {

    private Toolbar toolBar;
    private TextView toolbar_title;
    private Communication communication;
    private List<VendorAllBookingModel> list = new ArrayList<>();
    private VendorAllBookingAdapter adapter;
    private RecyclerView rv_bookings;
    private ImageView iv_setting;
    private String filter = "";

    public VendorAllBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_all_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        toolBar = view.findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        iv_setting = toolBar.findViewById(R.id.iv_setting);
        rv_bookings = view.findViewById(R.id.rv_bookings);
        rv_bookings.setLayoutManager(new LinearLayoutManager(getActivity()));
        toolBar.setOnMenuItemClickListener(this);
        adapter = new VendorAllBookingAdapter(getActivity(), list);
        adapter.setListener(this);
        rv_bookings.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);
    }

    private void getBookings(int filter, HashMap<String, String> param) {
        param.put("action", "vendor/all/bookings");
        param.put("filter", String.valueOf(filter));
        if (communication != null)
            communication.callPOST(param);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_filter){
            FilterDialogAllBooking dialogFragment = new FilterDialogAllBooking();
            dialogFragment.setData(filter, new FilterDialogAllBooking.ItemClickListener() {
             /*   @Override
                public void onItemClick(String booking) {
                    filter = booking;
                    HashMap<String, String> param = new HashMap<>();
                    if (booking.isEmpty()) {
                        getBookings(0, param);
                    } else {
                        param.put("all_booking", booking);
                        getBookings(1, param);
                    }
                }

                @Override
                public void onItemClick(String booking, String startDate, String endDate) {
                    filter = booking;
                    HashMap<String, String> param = new HashMap<>();
                    param.put("all_booking", booking);
                    param.put("from_date", startDate);
                    param.put("to_date", endDate);
                    getBookings(1, param);
                }*/

                @Override
                public void onItemClick(String booking, String conceirge_id, String booking_from,String conciergeName) {

                }

                @Override
                public void onItemClick(String booking, String startDate, String endDate, String conceirge_id, String booking_from,String conciergeName) {

                }
            });
            dialogFragment.show(getChildFragmentManager(), "Filter");
        }
        return false;
    }

    @Override
    public void onRefresh(int page, String name) {
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getBookings(filter.isEmpty() ? 0 : 1, param);

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/all/bookings")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray bookings = data.getJSONArray("all_booking");
                list.clear();
                for (int i = 0; i < bookings.length(); i++) {
                    VendorAllBookingModel model = new Gson().fromJson(bookings.getJSONObject(i).toString(), VendorAllBookingModel.class);
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
    public void onClick(View view) {

    }

    @Override
    public void onBookingClick(VendorAllBookingModel model) {
       if (model.getType().equalsIgnoreCase("Admin")||model.getType().equalsIgnoreCase("User")){
           BookingDetailDialog dialog = new BookingDetailDialog();
           dialog.setData(model.getItineraryEncryptedId(), new BookingDetailDialog.ItemClickListener() {
               @Override
               public void onDismiss() {

                   HashMap<String, String> param = new HashMap<>();
                   param.put("all_booking", filter);
                   getBookings(filter.isEmpty() ? 0 : 1, param);
               }
           });
           dialog.show(getChildFragmentManager(), "Booking");
       }else {
           Bundle bundle = new Bundle();
           bundle.putString("id",model.getEncrypted_booking_id());
           GoTo.startWithExtra(getActivity(), OtherBookingDetailActivity.class,bundle);
       }
    }


    @Override
    public void onResume() {

        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getBookings(filter.isEmpty() ? 0 : 1, param);
        super.onResume();
    }
}
