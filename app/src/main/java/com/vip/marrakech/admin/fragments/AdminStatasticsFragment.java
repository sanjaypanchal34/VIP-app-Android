package com.vip.marrakech.admin.fragments;


import android.os.Bundle;
import android.text.TextUtils;
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

import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.admin.activities.AdminNotificationActivity;
import com.vip.marrakech.admin.activities.AdminPromotionLIst;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.dialogs.AdminBookingFilterDialogFragment;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminStatasticsFragment extends Fragment implements Toolbar.OnMenuItemClickListener, AdminMainActivity.OnFragmentViewPagerChangeListener, OnCallBackListener {

    private Toolbar toolBar;
    private TextView toolbar_title;
    private Communication communication;
    private TextView tv_total_client, tv_total_booking, tv_total_spend, tv_total_cancellation;
    private ImageView iv_setting;
    private String filter = "";
    private ImageView iv_notification;
    private TextView tv_notification_count;
    List<String> selected = new ArrayList<>();
    private String radio = "";

    public AdminStatasticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_statastics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        toolBar = view.findViewById(R.id.toolBar);
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        iv_setting = toolBar.findViewById(R.id.iv_setting);
        toolBar.setOnMenuItemClickListener(this);
        tv_total_client = view.findViewById(R.id.tv_total_client);
        tv_total_booking = view.findViewById(R.id.tv_total_booking);
        tv_total_spend = view.findViewById(R.id.tv_total_spend);
        tv_total_cancellation = view.findViewById(R.id.tv_total_cancellation);
        getStatistics(0,new HashMap<String, String>(),new ArrayList<MasterVenueModel>());


        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminBookingFilterDialogFragment dialogFragment = new AdminBookingFilterDialogFragment();
                dialogFragment.setVenueIds(selected);
                dialogFragment.setSelectedRadio(radio);
                dialogFragment.setData(filter,new AdminBookingFilterDialogFragment.ItemClickListener() {
                    @Override
                    public void onItemClick(String booking, List<MasterVenueModel> venueList, String radio) {
                        filter = booking;
                        HashMap<String, String> param = new HashMap<>();
                        AdminStatasticsFragment.this.radio = radio;
                       /* if (booking.isEmpty()){
                            getStatistics(0,param, venueList);
                        }else {
                        }*/
                        param.put("all_booking", booking);
                        getStatistics(booking.isEmpty() && venueList.size() == 0?0:1, param, venueList);
                    }

                    @Override
                    public void onItemClick(String booking, String startDate, String endDate, List<MasterVenueModel> venueList, String radio) {
                        filter = booking;
                        AdminStatasticsFragment.this.radio = radio;
                        HashMap<String, String> param = new HashMap<>();
                        param.put("all_booking",booking);
                        param.put("from_date",startDate);
                        param.put("to_date",endDate);
                        getStatistics(1,param, venueList);
                    }
                });
                dialogFragment.show(getChildFragmentManager(),"Filter");
            }
        });

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


    private void getStatistics(int filter, HashMap<String, String> param, List<MasterVenueModel> venueList) {
        param.put("action", "statistics");
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
        } else
            param.put("filter", String.valueOf(filter));
        param.put("filter", String.valueOf(filter));
        param.put("booking_status", radio);
        communication.callPOST(param);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(getActivity(), AdminRecommandationActivity.class);
        }else if (item.getItemId() == R.id.opt_gift) {
            GoTo.start(getActivity(), AdminPromotionLIst.class);
        }else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), AdminNotificationActivity.class);
        }
        return false;
    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        if (toolbar_title != null)
            toolbar_title.setText(name);

        if (tv_notification_count!=null && !notification_count.equals("0")){
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        }else if (tv_notification_count!=null){
            tv_notification_count.setVisibility(View.GONE);
        }

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("statistics")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                tv_total_client.setText(getResources().getString(R.string.total_number_of_people_s,data.getString("totClients")));
                tv_total_booking.setText(getResources().getString(R.string.total_number_of_individual_bookings_s,data.getString("totBookings")));
                tv_total_spend.setText(getResources().getString(R.string.total_spend_s,data.getString("totSpend")));
                tv_total_cancellation.setText(getResources().getString(R.string.total_number_of_cancellation_s,data.getString("totCancelBookings")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
