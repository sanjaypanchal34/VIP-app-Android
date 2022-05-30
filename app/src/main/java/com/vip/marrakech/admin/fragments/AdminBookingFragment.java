package com.vip.marrakech.admin.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminAdminBookingActivity;
import com.vip.marrakech.admin.activities.AdminBookingActivity;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.admin.activities.AdminNotificationActivity;
import com.vip.marrakech.admin.activities.AdminPromotionLIst;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.adapters.BookingAdapter;
import com.vip.marrakech.admin.adapters.NewAllAdminBookingAdapter;
import com.vip.marrakech.admin.dialogs.AdminBookingFilterDialogFragment;
import com.vip.marrakech.admin.models.AdminExpandBooking;
import com.vip.marrakech.admin.models.BookingModel;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.admin.models.NewAdminBookingModel;
import com.vip.marrakech.helpers.EndlessRecyclerOnScrollListener;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminBookingFragment extends Fragment implements Toolbar.OnMenuItemClickListener, AdminMainActivity.OnFragmentViewPagerChangeListener, OnCallBackListener {

    private static String startDate, endDate;
    private Toolbar toolBar;
    private TextView toolbar_title;
    private Communication communication;
    private List<NewAdminBookingModel> list = new ArrayList<>();
    private NewAllAdminBookingAdapter adapter;
    private RecyclerView rv_bookings;
    private ImageView iv_setting;
    private String filter = "";
    private ImageView iv_notification;
    private TextView tv_notification_count;
    List<String> selected = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<MasterVenueModel> filterVanueLIst = new ArrayList<>();
    private EndlessRecyclerOnScrollListener scrollListener;
    private String radio = "";

    public AdminBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        toolBar = view.findViewById(R.id.toolBar);
        iv_setting = toolBar.findViewById(R.id.iv_setting);
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        rv_bookings = view.findViewById(R.id.rv_bookings);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_bookings.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {


            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage = page;
                isLoading = true;
                HashMap<String, String> param = new HashMap<>();
                if (!filter.isEmpty()) {
                    param.put("all_booking", filter);
                }
                if (startDate != null && endDate != null) {
                    param.put("from_date", startDate);
                    param.put("to_date", endDate);
                }
                getBookings(filter.isEmpty() ? 0 : 1, param, filterVanueLIst);
            }
        };
        rv_bookings.addOnScrollListener(scrollListener);
        //toolBar.setOnMenuItemClickListener(this);

        adapter = new NewAllAdminBookingAdapter(getActivity(), list, new BookingAdapter.OnBookingListener() {
            @Override
            public void onBookingClick(BookingModel model) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", model);
                if(model.getBooking_type().equalsIgnoreCase("Admin")){
                    GoTo.startWithExtra(getActivity(), AdminBookingActivity.class, bundle);
                }else {
                    GoTo.startWithExtra(getActivity(), AdminAdminBookingActivity.class, bundle);
                }


            }
        });

        adapter.setListener(new NewAllAdminBookingAdapter.OnAllBookingListener() {
            @Override
            public void onHeaderClick(NewAdminBookingModel allBookingModel) {
                for (NewAdminBookingModel bookingModel :
                        list) {
                    if (bookingModel.getName().equals(allBookingModel.getName())) {
                        bookingModel.setVisible(true);
                        if(bookingModel.getBookingList().size()>0){
                            bookingModel.getBookingList().get(0).setVisible(true);
                        }
                    } else {
                        for (AdminExpandBooking expandBooking : bookingModel.getBookingList()) {
                            expandBooking.setVisible(bookingModel.getName().equalsIgnoreCase("Today"));
                        }
                        bookingModel.setVisible(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        rv_bookings.setAdapter(adapter);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminBookingFilterDialogFragment dialogFragment = new AdminBookingFilterDialogFragment();
                dialogFragment.setVenueIds(selected);
                dialogFragment.setSelectedRadio(radio);
                dialogFragment.setData(filter, new AdminBookingFilterDialogFragment.ItemClickListener() {
                    @Override
                    public void onItemClick(String booking, List<MasterVenueModel> venueList, String radio) {
                        list.clear();
                        filterVanueLIst.clear();
                        filterVanueLIst.addAll(venueList);
                        scrollListener.resetState();
                        adapter.notifyDataSetChanged();
                        filter = booking;
                        currentPage = 1;
                        AdminBookingFragment.this.radio = radio;
                        AdminBookingFragment.startDate = null;
                        AdminBookingFragment.endDate = null;
                        HashMap<String, String> param = new HashMap<>();

                        param.put("all_booking", booking);
                        getBookings(booking.isEmpty() && venueList.size() == 0?0:1, param, venueList);
                    }

                    @Override
                    public void onItemClick(String booking, String startDate, String endDate, List<MasterVenueModel> venueList, String radio) {
                        list.clear();
                        filterVanueLIst.clear();
                        filterVanueLIst.addAll(venueList);
                        scrollListener.resetState();
                        adapter.notifyDataSetChanged();
                        filter = booking;
                        currentPage = 1;
                        AdminBookingFragment.this.radio = radio;
                        AdminBookingFragment.startDate = startDate;
                        AdminBookingFragment.endDate = endDate;
                        HashMap<String, String> param = new HashMap<>();
                        param.put("all_booking", booking);
                        param.put("from_date", startDate);
                        param.put("to_date", endDate);
                        getBookings(1, param, venueList);
                    }
                });
                dialogFragment.show(getChildFragmentManager(), "Filter");
            }
        });
        getBookings(0, new HashMap<String, String>(), new ArrayList<MasterVenueModel>());


        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        ImageView gift = (ImageView) toolBar.getMenu().findItem(R.id.opt_gift).getActionView();
        ImageView recomndation = (ImageView) toolBar.getMenu().findItem(R.id.opt_recommandation).getActionView();
        notification.setOnClickListener(customMenuListener);
        gift.setOnClickListener(customMenuListener);
        recomndation.setOnClickListener(customMenuListener);
        super.onViewCreated(view, savedInstanceState);
    }


    private View.OnClickListener customMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMenuItemClick(toolBar.getMenu().findItem(v.getId()));
        }
    };

    /*

        private void getBookings() {
            HashMap<String,String> param = new HashMap<>();
            param.put("action","");
            communication.callPOST(param);

        }*/
    private void getBookings(int filter, HashMap<String, String> param, List<MasterVenueModel> venueList) {
        selected.clear();
        for (int i = 0; i < venueList.size(); i++) {
            MasterVenueModel model = venueList.get(i);
            if (model.isChecked()) {
                selected.add(model.getId());
            }
        }
        if (selected.size() > 0) {

            param.put("filter", "1");
            param.put("venue_ids", TextUtils.join(",", selected));
            param.put("booking_status", radio);
        } else
            param.put("filter", String.valueOf(filter));
        param.put("filter", "1");
        param.put("booking_status", radio);
        param.put("action", "get/all/admin/bookings");
        param.put("page", String.valueOf(currentPage));
        communication.callPOST(param);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(getActivity(), AdminRecommandationActivity.class);
        } else if (item.getItemId() == R.id.opt_gift) {
            GoTo.start(getActivity(), AdminPromotionLIst.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), AdminNotificationActivity.class);
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
        if(communication!=null){
            list.clear();
            getBookings(0, new HashMap<String, String>(), new ArrayList<MasterVenueModel>());
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("get/all/admin/bookings")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray bookingsToday = data.getJSONArray("today");
                JSONArray bookingsPast = data.getJSONArray("past");
                JSONArray bookingsFuture = data.getJSONArray("future");
                list.clear();
                List<AdminExpandBooking> todayBooking = new ArrayList<>();
                List<AdminExpandBooking> pastBooking = new ArrayList<>();
                List<AdminExpandBooking> futureBooking = new ArrayList<>();
                for (int i = 0; i < bookingsToday.length(); i++) {
                    AdminExpandBooking model = new Gson().fromJson(bookingsToday.getJSONObject(i).toString(), AdminExpandBooking.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    if (model.getDate().equals(dateFormat.format(new Date()))) {
                        model.setVisible(true);
                    }
                    todayBooking.add(model);

                }

                for (int i = 0; i < bookingsFuture.length(); i++) {
                    AdminExpandBooking model = new Gson().fromJson(bookingsFuture.getJSONObject(i).toString(), AdminExpandBooking.class);
                    if (i == 0 && todayBooking.size() == 0) {
                        model.setVisible(true);
                    }
                    futureBooking.add(model);

                }

                for (int i = 0; i < bookingsPast.length(); i++) {
                    AdminExpandBooking model = new Gson().fromJson(bookingsPast.getJSONObject(i).toString(), AdminExpandBooking.class);
                    if(i==0 && todayBooking.size()==0 && futureBooking.size()==0){
                        model.setVisible(true);
                    }
                    pastBooking.add(model);

                }
                list.add(new NewAdminBookingModel("Past", pastBooking, todayBooking.size()==0 && futureBooking.size()==0, data.getString("past_pax")));
                list.add(new NewAdminBookingModel("Today", todayBooking, todayBooking.size() != 0, data.getString("today_pax")));
                list.add(new NewAdminBookingModel("Future", futureBooking, todayBooking.size() == 0, data.getString("future_pax")));

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
