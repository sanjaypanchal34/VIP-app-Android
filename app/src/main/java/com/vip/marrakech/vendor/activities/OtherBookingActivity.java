package com.vip.marrakech.vendor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.customs.NonSwipeableViewPager;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.user.fragments.UserProfileFragment;
import com.vip.marrakech.vendor.fragments.SacnFragment;
import com.vip.marrakech.vendor.fragments.VendorAllBookingFragment;
import com.vip.marrakech.vendor.fragments.VendorBookingFragment;
import com.vip.marrakech.vendor.fragments.VendorConciergeCommissionListFragment;
import com.vip.marrakech.vendor.fragments.VendorConciergeListFragment;
import com.vip.marrakech.vendor.fragments.VendorOtherAddBookingFragment;
import com.vip.marrakech.vendor.fragments.VendorOtherBookingFragment;
import com.vip.marrakech.vendor.fragments.VendorStatasticsFragment;

public class OtherBookingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private NonSwipeableViewPager viewPager;
//    private int[] tabIcons = new int[]{R.drawable.ic_booking, R.drawable.ic_conceirge, R.drawable.ic_percentage, R.drawable.ic_booking};
    private int[] tabIcons = new int[]{ R.drawable.ic_concierge, R.drawable.ic_percentage};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_booking);

        initUI();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
                View view = tab.getCustomView();
                OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                listener.onRefresh(viewPager.getCurrentItem(), tabTitle(viewPager.getCurrentItem()));
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab_icon.setColorFilter(getResources().getColor(R.color.tb_selected), PorterDuff.Mode.SRC_IN);
                tab_label.setTextColor(getResources().getColor(R.color.tb_selected));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab_icon.setColorFilter(getResources().getColor(R.color.tabUnselected), PorterDuff.Mode.SRC_IN);
                tab_label.setTextColor(getResources().getColor(R.color.tabUnselected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    private void initUI() {

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(adapter.getCount());
        setTabIcons();

    }

    private void setTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(OtherBookingActivity.this).inflate(R.layout.custom_tab_admin, null);
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab.setCustomView(view);
                if (0 == i) {
//                    OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager, i);
//                    listener.onRefresh(viewPager.getCurrentItem(), tabTitle(viewPager.getCurrentItem()));
                    tab_label.setText(tabNAme(i));
                    tab_icon.setImageResource(tabIcons[i]);
                    tab_label.setTextColor(getResources().getColor(R.color.tb_selected));
                    tab_icon.setColorFilter(getResources().getColor(R.color.tb_selected), PorterDuff.Mode.SRC_IN);
                } else {
                    tab_label.setText(tabNAme(i));
                    tab_icon.setImageResource(tabIcons[i]);
                    tab_label.setTextColor(getResources().getColor(R.color.tabUnselected));
                    tab_icon.setColorFilter(getResources().getColor(R.color.tabUnselected), PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {


        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
              /*  case 0:
                    return new VendorOtherBookingFragment();*/

                case 0:
                    return new VendorConciergeListFragment();

                case 1:
                    return new VendorConciergeCommissionListFragment();


                default:
                    return new VendorAllBookingFragment();

            }

        }

        @Override
        public int getCount() {
            return 2;
        }


    }

    private String tabNAme(int pos) {
        switch (pos) {
          /*  case 0:
                return "Other\nBooking";*/

            case 0:
                return "Concierge";

            case 1:
                return "Commission";

            case 3:
                return "All\nBooking";

            default:
                return "Booking";

        }
    }

    private String tabTitle(int pos) {
        switch (pos) {
            case 0:
                return "Other\nBooking";

            case 1:
                return "Concierge";

            case 2:
                return "Commission";

            case 3:
                return "All\nBooking";

            default:
                return "Booking";

        }
    }

    public interface OnFragmentViewPagerChangeListener {
        void onRefresh(int page, String name);
    }

    IntentFilter intentFilterACSD = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                SessionManager.setVendorConfirmPassword(false);
            }
        }
    };

    /*@Override
    protected void onUserLeaveHint() {
        SessionManager.setVendorConfirmPassword(false);
        super.onUserLeaveHint();
    }*/

    @Override
    protected void onResume() {
//        OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
//        listener.onRefresh(viewPager.getCurrentItem(), tabTitle(viewPager.getCurrentItem()));
        registerReceiver(broadcastReceiver, intentFilterACSD);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}
