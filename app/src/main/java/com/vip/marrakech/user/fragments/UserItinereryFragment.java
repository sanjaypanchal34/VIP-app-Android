package com.vip.marrakech.user.fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.customs.NonSwipeableViewPager;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.user.activities.UserNotificationActivity;
import com.vip.marrakech.user.interfaces.OnGuestLoginListener;

import java.util.Locale;

public class UserItinereryFragment extends Fragment implements UserMainActivity.OnFragmentViewPagerChangeListener {
    private TabLayout tabLayoutItinerary;
    private NonSwipeableViewPager vp_itinerary;
    private ViewPagerAdapter adapter;
    private TextView tv_notification_count;
    private Toolbar toolBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_itinerary, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        vp_itinerary = view.findViewById(R.id.vp_itinerary);
        tabLayoutItinerary = view.findViewById(R.id.tabLayoutItinerary);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), 0);

        vp_itinerary.setAdapter(adapter);
        tabLayoutItinerary.setupWithViewPager(vp_itinerary);

        toolBar = view.findViewById(R.id.toolBar);
        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !SessionManager.isLogged()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.start_now)
                            .setMessage(R.string.please_login)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.cont), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SessionManager.setLogged(false);
                                    SessionManager.setVendorConfirmPassword(false);
                                    Resources resources = getResources();
                                    DisplayMetrics dm = resources.getDisplayMetrics();
                                    Configuration config = resources.getConfiguration();
                                    config.setLocale(new Locale("en"));
                                    resources.updateConfiguration(config, dm);
                                    GoTo.startWithClearTop(getActivity(), RegisterActivity.class);
                                }
                            })

                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    listener.onLoginActionCancel();
                                }
                            })
                            .show();
                }else{
                    GoTo.start(getActivity(), UserNotificationActivity.class);
                }

            }
        });
    }

    @Override
    public void onRefresh(int page, String notification_count) {
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }


    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new UserUpocomingItinerary();
            } else {
                return new UserPastItinerary();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getResources().getString(R.string.upcoming);
            } else {
                return getResources().getString(R.string.past);
            }
        }
    }


    public OnGuestLoginListener listener;

    public void setListener(OnGuestLoginListener listener) {
        this.listener = listener;
    }
}
