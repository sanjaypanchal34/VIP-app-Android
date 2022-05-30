package com.vip.marrakech.vendor.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.customs.NonSwipeableViewPager;
import com.vip.marrakech.helpers.AsyncTask;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.dialog.InAppOTPVerifyDialog;
import com.vip.marrakech.vendor.fragments.SacnFragment;
import com.vip.marrakech.vendor.fragments.VendorBookingFragment;
import com.vip.marrakech.vendor.fragments.VendorProfileFragment;
import com.vip.marrakech.vendor.fragments.VendorStatasticsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VendorMainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, OnCallBackListener {
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private NonSwipeableViewPager viewPager;
    private String notification_count = "0";
    private int[] tabIcons = new int[]{R.drawable.ic_booking, R.drawable.ic_statics, R.drawable.ic_qr_tab, R.drawable.ic_avatar};
    private Communication  communication;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        databaseHelper =new DatabaseHelper(this);
        communication = new Communication(this,this);
        initUI();
        Log.e("TOKeN:::",SessionManager.getToken());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
                View view = tab.getCustomView();
                OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                listener.onRefresh(viewPager.getCurrentItem(),tabTitle(viewPager.getCurrentItem()),notification_count, false);
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab_icon.setColorFilter(getResources().getColor(R.color.tb_selected), PorterDuff.Mode.SRC_IN);
                tab_label.setTextColor(getResources().getColor(R.color.tb_selected));
                getNotificationCount();
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


        getMasterData();

    }
    private void getMasterData() {
        HashMap<String,String> param = new HashMap<>();
        param.put("action","allVenueBottles");
        communication.callGET(param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_main, menu);
        return true;
    }

    private void initUI() {

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(adapter.getCount());
        setTabIcons();



      /*  OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,0);
        listener.onRefresh(viewPager.getCurrentItem(),tabTitle(0));*/
    }

    private void setTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(VendorMainActivity.this).inflate(R.layout.custom_tab_admin, null);
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab.setCustomView(view);
                if (0 == i) {
                    OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,i);
                    listener.onRefresh(viewPager.getCurrentItem(),tabTitle(viewPager.getCurrentItem()), notification_count, false);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("notification/counts")){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                notification_count = data.getString("notification_count");
                OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                listener.onRefresh(viewPager.getCurrentItem(),tabTitle(viewPager.getCurrentItem()),notification_count,true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else  if (tag.equals("allVenueBottles")){
            try {
                databaseHelper.deleteDB();
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray venue = data.getJSONArray("venues");
                JSONArray bottle_details = data.getJSONArray("bottle_details");
                for (int i=0;i<venue.length();i++){
                    MasterVenueModel model = new Gson().fromJson(venue.getJSONObject(i).toString(),MasterVenueModel.class);

                    new AsyncTask<>(new AsyncTask.OnAsyncTaskListener<Boolean,MasterVenueModel>() {
                        @Override
                        public Boolean onBackground(MasterVenueModel venueModel) {
                            return databaseHelper.insertVenue(venueModel);
                        }

                        @Override
                        public void onPreTask() {

                        }

                        @Override
                        public void onPostTask(Boolean object,MasterVenueModel venueModel) {
                            Log.e("IS_VENUE_INSERT:::",venueModel.getId());
                        }
                    },model);
                }

                for (int i=0;i<bottle_details.length();i++){
                    final MasterBottelModel model = new Gson().fromJson(bottle_details.getJSONObject(i).toString(),MasterBottelModel.class);

                    new AsyncTask<>(new AsyncTask.OnAsyncTaskListener<Boolean,MasterBottelModel>() {
                        @Override
                        public Boolean onBackground(MasterBottelModel venueModel) {
                            return databaseHelper.insertBottle(venueModel);
                        }

                        @Override
                        public void onPreTask() {

                        }

                        @Override
                        public void onPostTask(Boolean object,MasterBottelModel bottelModel) {
                            Log.e("IS_BOTTLE_INSERT:::",bottelModel.getId());
                        }
                    },model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        getNotificationCount();
        registerReceiver(receiver,new IntentFilter("pushNotification"));
        registerReceiver(broadcastReceiver, intentFilterACSD);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ADMIN:::","NOTIFY");
            getNotificationCount();
        }
    };

    private void getNotificationCount() {
        HashMap<String,String> param = new HashMap<>();
        param.put("action","notification/counts");
        param.put("type","Vendor");
        param.put("user_id", SessionManager.getEncryptedID());
        communication.callPOST(param);
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {


        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new VendorBookingFragment();

                case 1:
                    return new VendorStatasticsFragment();

                case 2:
                    return new SacnFragment();

                case 3:
                    return new VendorProfileFragment();

                default:
                    return new VendorBookingFragment();

            }
        }

        @Override
        public int getCount() {
            return 4;
        }


    }

    private String tabNAme(int pos) {
        switch (pos) {
            case 0:
                return "Bookings";

            case 1:
                return "Statistics";

            case 2:
                return "Scan";

            case 3:
                return "My\nAccount";

            default:
                return "Bookings";

        }
    }

    private String tabTitle(int pos) {
        switch (pos) {
            case 0:
                return "All Bookings";

            case 1:
                return "Statistics";

            case 2:
                return "Itinerary";

            case 3:
                return "Scan";
            case 4:
                return "My Profile";

            default:
                return "All Bookings";

        }
    }


    public interface OnFragmentViewPagerChangeListener{
        void onRefresh(int page, String name, String notification_count, boolean b);
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SacnFragment){
            ((SacnFragment) fragment).setListener(new SacnFragment.OnScanCompleteListener() {
                @Override
                public void onScanCompleted() {
                    viewPager.setCurrentItem(0,false);
                }
            });
        }
    }

    IntentFilter intentFilterACSD = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                Log.e("LEAVE BROADCAST:::","TRUE");
                SessionManager.setVendorConfirmPassword(false);
            }
        }
    };

  /*  @Override
    protected void onUserLeaveHint() {
        Log.e("LEAVE METHOD:::","TRUE");
        SessionManager.setVendorConfirmPassword(false);
        super.onUserLeaveHint();
    }
*/
}
