package com.vip.marrakech.vendor.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.vip.marrakech.vendor.activities.AddOtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingDetailActivity;
import com.vip.marrakech.vendor.activities.VendorMainActivity;
import com.vip.marrakech.vendor.activities.VendorNotificationActivity;
import com.vip.marrakech.vendor.adapters.NewAllBookingAdapter;
import com.vip.marrakech.vendor.adapters.VendorBookingAdapter;
import com.vip.marrakech.vendor.adapters.VendorBookingAdapterSub;
import com.vip.marrakech.vendor.dialog.BookingDetailDialog;
import com.vip.marrakech.vendor.dialog.FilterDialogAllBooking;
import com.vip.marrakech.vendor.models.AllBookingModel;
import com.vip.marrakech.vendor.models.VendorAllBookingModel;
import com.vip.marrakech.vendor.models.VendorExpandBooking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBookingFragment extends Fragment implements Toolbar.OnMenuItemClickListener, VendorMainActivity.OnFragmentViewPagerChangeListener, OnCallBackListener, View.OnClickListener, VendorBookingAdapterSub.OnBookingListener {

    private Toolbar toolBar;
    private TextView toolbar_title;
    private Communication communication;
    private List<AllBookingModel> list = new ArrayList<>();
    private NewAllBookingAdapter adapter;
    private RecyclerView rv_bookings;
    private ImageView iv_setting,iv_refresh;
    private String filter = "";
    private ImageView iv_notification;
    private TextView tv_notification_count;
    private String conceirge_id = "";
    private String booking_from = "";
    private String startDate = "", endDate = "";
    private String conceirge_name = "";

    public VendorBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        toolBar = view.findViewById(R.id.toolBar);
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        iv_setting = toolBar.findViewById(R.id.iv_setting);
        iv_refresh = toolBar.findViewById(R.id.iv_refresh);
        rv_bookings = view.findViewById(R.id.rv_bookings);
        rv_bookings.setLayoutManager(new LinearLayoutManager(getActivity()));
        toolBar.setOnMenuItemClickListener(this);
        adapter = new NewAllBookingAdapter(getActivity(), list, this);
        adapter.setListener(new NewAllBookingAdapter.OnAllBookingListener() {
            @Override
            public void onHeaderClick(AllBookingModel allBookingModel) {
                for (AllBookingModel bookingModel :
                        list) {
                    if (bookingModel.getName().equals(allBookingModel.getName())) {
                        bookingModel.setVisible(true);
                        if(bookingModel.getBookingList().size()>0){
                            bookingModel.getBookingList().get(0).setVisible(true);
                        }
                    } else {
                        for (VendorExpandBooking expandBooking : bookingModel.getBookingList()) {
                                expandBooking.setVisible(bookingModel.getName().equalsIgnoreCase("Today"));
                        }
                        bookingModel.setVisible(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        rv_bookings.setAdapter(adapter);
        iv_setting.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);

        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuItemClick(toolBar.getMenu().findItem(R.id.opt_notification));
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void getBookings(int is, HashMap<String, String> param) {
        param.put("action", "vendor/all/bookings");
        param.put("filter", String.valueOf(1));
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            param.put("from_date", startDate);
            param.put("to_date", endDate);
        }
        if (!filter.isEmpty()) {
            param.put("all_booking", filter);
        }
        if (!conceirge_id.isEmpty()) {
            param.put("conceirge_id", conceirge_id);
        }
        param.put("booking_from", booking_from);
        Log.e("PARAM::::", param.toString());
        if (communication != null)
            communication.callPOST(param);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_other) {
            GoTo.start(getActivity(), OtherBookingActivity.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), VendorNotificationActivity.class);
        } else if (item.getItemId() == R.id.opt_add) {
            GoTo.start(getActivity(), AddOtherBookingActivity.class);
        }
        return false;
    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        if (toolbar_title != null)
            toolbar_title.setText(name);

        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }

        HashMap<String, String> param = new HashMap<>();
        getBookings(filter.isEmpty() ? 0 : 1, param);

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/all/bookings")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray bookingsToday = data.getJSONArray("today");
                JSONArray bookingsPast = data.getJSONArray("past");
                JSONArray bookingsFuture = data.getJSONArray("future");
                list.clear();
                List<VendorExpandBooking> todayBooking = new ArrayList<>();
                List<VendorExpandBooking> pastBooking = new ArrayList<>();
                List<VendorExpandBooking> futureBooking = new ArrayList<>();
                for (int i = 0; i < bookingsToday.length(); i++) {
                    VendorExpandBooking model = new Gson().fromJson(bookingsToday.getJSONObject(i).toString(), VendorExpandBooking.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    if (model.getDate().equals(dateFormat.format(new Date()))) {
                        model.setVisible(true);
                    }
                    todayBooking.add(model);

                }

                for (int i = 0; i < bookingsFuture.length(); i++) {
                    VendorExpandBooking model = new Gson().fromJson(bookingsFuture.getJSONObject(i).toString(), VendorExpandBooking.class);
                    if (i == 0 && todayBooking.size() == 0) {
                        model.setVisible(true);
                    }
                    futureBooking.add(model);

                }

                for (int i = 0; i < bookingsPast.length(); i++) {
                    VendorExpandBooking model = new Gson().fromJson(bookingsPast.getJSONObject(i).toString(), VendorExpandBooking.class);
                    if(i==0 && todayBooking.size()==0 && futureBooking.size()==0){
                        model.setVisible(true);
                    }
                        pastBooking.add(model);

                }
                list.add(new AllBookingModel("Past", pastBooking, todayBooking.size()==0 && futureBooking.size()==0, data.getString("past_pax")));
                list.add(new AllBookingModel("Today", todayBooking, todayBooking.size() != 0, data.getString("today_pax")));
                list.add(new AllBookingModel("Future", futureBooking, todayBooking.size() == 0, data.getString("future_pax")));

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
        if (view.getId() == R.id.iv_setting) {
            FilterDialogAllBooking dialogFragment = new FilterDialogAllBooking();
            dialogFragment.setData1(conceirge_id,
                    booking_from,
                    startDate,
                    endDate,
                    conceirge_name);
            dialogFragment.setData(filter, new FilterDialogAllBooking.ItemClickListener() {

                @Override
                public void onItemClick(String booking, String id, String from, String conciergeName) {
                    filter = booking;
                    conceirge_id = id;
                    conceirge_name = conciergeName;

                    booking_from = from;
                    HashMap<String, String> param = new HashMap<>();
                    /*       *//*   if (booking.isEmpty()) {
                            getBookings(0, param);
                        } else {

                        }*//*
                      if (!booking.isEmpty()){
                          param.put("all_booking", booking);
                      }
                        if (!id.isEmpty()){
                            param.put("conceirge_id", id);
                        }
                        param.put("booking_from", from);*/
                    getBookings(1, param);
                }

                @Override
                public void onItemClick(String booking, String startDate, String endDate, String id, String from, String conciergeName) {
                    filter = booking;
                    conceirge_id = id;
                    booking_from = from;
                    conceirge_name = conciergeName;
                    VendorBookingFragment.this.startDate = startDate;
                    VendorBookingFragment.this.endDate = endDate;
                    HashMap<String, String> param = new HashMap<>();
                       /* param.put("from_date", startDate);
                        param.put("to_date", endDate);
                        if (!booking.isEmpty()){
                            param.put("all_booking", booking);
                        }
                        if (!id.isEmpty()){
                            param.put("conceirge_id", id);
                        }
                        param.put("booking_from", from);*/
                    getBookings(1, param);
                }
            });
            dialogFragment.show(getChildFragmentManager(), "Filter");
        }else if (view.getId() == R.id.iv_refresh) {
            HashMap<String, String> param = new HashMap<>();
            filter = "";
            getBookings(filter.isEmpty() ? 0 : 1, param);
        }
    }

    @Override
    public void onBookingClick(VendorAllBookingModel model) {
        if (model.getType().equalsIgnoreCase("Self")) {
            Bundle bundle = new Bundle();
            bundle.putString("id", model.getEncrypted_booking_id());
            GoTo.startWithExtra(getActivity(), OtherBookingDetailActivity.class, bundle);

        } else {
            BookingDetailDialog dialog = new BookingDetailDialog();
            dialog.setData(model.getItineraryEncryptedId(), new BookingDetailDialog.ItemClickListener() {
                @Override
                public void onDismiss() {

                    HashMap<String, String> param = new HashMap<>();
                    getBookings(filter.isEmpty() ? 0 : 1, param);
                }
            });
            dialog.show(getChildFragmentManager(), "Booking");
        }

        /*BookingDetailDialog dialog = new BookingDetailDialog();
        dialog.setData(model.getItineraryEncryptedId(), new BookingDetailDialog.ItemClickListener() {
            @Override
            public void onDismiss() {

                HashMap<String, String> param = new HashMap<>();
                param.put("all_booking", filter);
                getBookings(filter.isEmpty() ? 0 : 1, param);
            }
        });
        dialog.show(getChildFragmentManager(), "Booking");*/
    }


    @Override
    public void onResume() {

        HashMap<String, String> param = new HashMap<>();
        getBookings(filter.isEmpty() ? 0 : 1, param);
        super.onResume();
    }
}
