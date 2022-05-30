package com.vip.marrakech.vendor.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.user.fragments.UserPastItinerary;
import com.vip.marrakech.user.fragments.UserUpocomingItinerary;
import com.vip.marrakech.vendor.fragments.DailyFragment;
import com.vip.marrakech.vendor.fragments.VIPBookingFragment;

public class AvailabilityActivity extends AppCompatActivity {

    private TabLayout tabAvailability;
    private ViewPager vp_availability;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);


        initUI();
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabAvailability = findViewById(R.id.tabAvailability);
        vp_availability = findViewById(R.id.vp_availability);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        vp_availability.setAdapter(adapter);
        tabAvailability.setupWithViewPager(vp_availability);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("type","VIP");
                Fragment fragment = new DailyFragment();
                fragment.setArguments(bundle);
                return fragment;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("type","Other");
                Fragment fragment = new VIPBookingFragment();
                fragment.setArguments(bundle);
                return fragment;
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
                return getResources().getString(R.string.daily);
            } else {
                return getResources().getString(R.string.exception_dates);
            }
        }
    }
}
