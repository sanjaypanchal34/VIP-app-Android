package com.vip.marrakech.vendor.fragments;


import android.os.Bundle;
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
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.VendorMainActivity;
import com.vip.marrakech.vendor.activities.VendorNotificationActivity;
import com.vip.marrakech.vendor.dialog.FilterDialogFragment;
import com.vip.marrakech.vendor.dialog.ProfilePasswordDialog;
import com.vip.marrakech.vendor.dialog.SetProfilePasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorStatasticsFragment extends Fragment implements Toolbar.OnMenuItemClickListener, OnCallBackListener, View.OnClickListener, VendorMainActivity.OnFragmentViewPagerChangeListener, ProfilePasswordDialog.OnPasswordConfirmListener, SetProfilePasswordDialog.OnProfilePasswordSetListener {

    private Toolbar toolBar;
    private TextView toolbar_title;
    private Communication communication;
    private TextView tv_total_client, tv_total_booking, tv_total_no_shows, tv_total_spend, tv_average_spend;
    private ImageView iv_setting;
    private String filter = "";
    private TextView tv_notification_count;
    private ProfilePasswordDialog passwordDialog;
    private SetProfilePasswordDialog setPasswordDialog;

    public VendorStatasticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_statastics, container, false);
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
        tv_total_no_shows = view.findViewById(R.id.tv_total_no_shows);
        tv_total_spend = view.findViewById(R.id.tv_total_spend);
        tv_average_spend = view.findViewById(R.id.tv_average_spend);
        iv_setting.setOnClickListener(this);


        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuItemClick(toolBar.getMenu().findItem(R.id.opt_notification));
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void getStatistics(int filter, HashMap<String, String> param) {

        if (SessionManager.isVendorConfirmPassword() && SessionManager.isProfilePasswordSet() == 1) {
            param.put("action", "get/vendor/statistics");
            param.put("filter", String.valueOf(filter));
            param.put("vendor_id", SessionManager.getEncryptedID());
            communication.callPOST(param);
        } else if (getUserVisibleHint()){

            if (SessionManager.isProfilePasswordSet() == 1 ) {
                if (passwordDialog!=null && passwordDialog.isVisible()){
                    passwordDialog.dismiss();
                }
                passwordDialog = new ProfilePasswordDialog();
                passwordDialog.setListener(this);
                passwordDialog.show(getChildFragmentManager(), "confirm Profile password");
            } else {
                if (setPasswordDialog!=null && setPasswordDialog.isVisible()){
                    setPasswordDialog.dismiss();
                }
                setPasswordDialog = new SetProfilePasswordDialog();
                setPasswordDialog.setListener(this);
                setPasswordDialog.show(getChildFragmentManager(), "set Profile password");
            }
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(getActivity(), AdminRecommandationActivity.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), VendorNotificationActivity.class);
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
       if (!b){
           HashMap<String, String> param = new HashMap<>();
           param.put("all_booking", filter);
           getStatistics(filter.isEmpty() ? 0 : 1, param);
       }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("get/vendor/statistics")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                tv_total_client.setText(getResources().getString(R.string.total_number_of_client_s, data.getString("totClients")));
                tv_total_booking.setText(getResources().getString(R.string.total_number_of_booking_s, data.getString("totBookings")));
                tv_total_no_shows.setText(getResources().getString(R.string.total_number_of_no_show_s, data.getString("totNoShows")));
                tv_total_spend.setText(String.format("%s", data.getString("totSpend")));
                tv_average_spend.setText(getResources().getString(R.string.average_spend_per_client, data.getString("totSpendAvrage")));
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
            FilterDialogFragment dialogFragment = new FilterDialogFragment();
            dialogFragment.setData(filter, new FilterDialogFragment.ItemClickListener() {
                @Override
                public void onItemClick(String booking) {
                    filter = booking;
                    HashMap<String, String> param = new HashMap<>();
                    if (booking.isEmpty()) {
                        getStatistics(0, param);
                    } else {
                        param.put("all_booking", booking);
                        getStatistics(1, param);
                    }
                }

                @Override
                public void onItemClick(String booking, String startDate, String endDate) {
                    filter = booking;
                    HashMap<String, String> param = new HashMap<>();
                    param.put("all_booking", booking);
                    param.put("from_date", startDate);
                    param.put("to_date", endDate);
                    getStatistics(1, param);
                }
            });
            dialogFragment.show(getChildFragmentManager(), "Filter");
        }
    }


    @Override
    public void onResume() {
        tv_total_client.setText(getResources().getString(R.string.total_number_of_client_s, ""));
        tv_total_booking.setText(getResources().getString(R.string.total_number_of_booking_s, ""));
        tv_total_no_shows.setText(getResources().getString(R.string.total_number_of_no_show_s, ""));
        tv_total_spend.setText(String.format("%s", ""));
        tv_average_spend.setText(getResources().getString(R.string.average_spend_per_client, ""));
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getStatistics(filter.isEmpty() ? 0 : 1, param);
        super.onResume();
    }

    @Override
    public void onPasswordConfirm() {
        SessionManager.setVendorConfirmPassword(true);
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getStatistics(filter.isEmpty() ? 0 : 1, param);
    }

    @Override
    public void onProfilePassword() {
        SessionManager.setVendorConfirmPassword(true);
        HashMap<String, String> param = new HashMap<>();
        param.put("all_booking", filter);
        getStatistics(filter.isEmpty() ? 0 : 1, param);
    }


}
