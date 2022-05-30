package com.vip.marrakech.user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.customs.NonSwipeableViewPager;
import com.vip.marrakech.helpers.AppController;
import com.vip.marrakech.helpers.AsyncTask;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.fragments.UserItinereryFragment;
import com.vip.marrakech.user.fragments.UserProfileFragment;
import com.vip.marrakech.user.fragments.UserPromotionFragment;
import com.vip.marrakech.user.fragments.UserRecommendationFragment;
import com.vip.marrakech.user.interfaces.OnGuestLoginListener;
import com.vip.marrakech.vendor.activities.VendorMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;

public class UserMainActivity extends AppCompatActivity implements OnCallBackListener {
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private NonSwipeableViewPager viewPager;
    private int[] tabIcons = new int[]{R.drawable.ic_avatar, R.drawable.ic_travel, R.drawable.ic_star, R.drawable.ic_ticket};
    private Communication communication;
    private DatabaseHelper databaseHelper;
    private String notification_count = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        communication = new Communication(this,this);
        databaseHelper = new DatabaseHelper(this);
        initUI();

        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.app_guide);
            File output_file = new File(getCacheDir(), "app_guide.mp4");
            OutputStream outputStream = new FileOutputStream(output_file);
            byte [] data = new byte[1024];
            int total = 0;
            int count;
            while ((count = in_s.read(data)) != -1) {
                total += count;

                outputStream.write(data, 0, count);

            }
            in_s.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
                View view = tab.getCustomView();
                OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                listener.onRefresh(viewPager.getCurrentItem(),notification_count);
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



        viewPager.setCurrentItem(getIntent().getIntExtra("page",2),false);
        getMasterData();

    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if(fragment instanceof UserProfileFragment){
            ((UserProfileFragment) fragment).setOnLanguageListener(new UserProfileFragment.OnLanguageListener() {
                @Override
                public void onLLanguageChange() {
                    Resources resources = getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    Configuration config = resources.getConfiguration();
                    config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
                    resources.updateConfiguration(config, dm);
                    Intent intent = getIntent();
                    intent.putExtra("page", 0);
                    startActivity(intent);
                    finish();
                }
            }, new OnGuestLoginListener() {
                @Override
                public void onLoginActionCancel() {
                    viewPager.setCurrentItem(2);
                }
            });
        }else if(fragment instanceof UserItinereryFragment){
            ((UserItinereryFragment) fragment).setListener(new OnGuestLoginListener() {
                @Override
                public void onLoginActionCancel() {
                    viewPager.setCurrentItem(2);
                }
            });
        }
        super.onAttachFragment(fragment);
    }

    private void getMasterData() {
        HashMap<String,String> param = new HashMap<>();
        param.put("action","allVenueBottles");
        communication.callGET(param);
    }


    private void initUI() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(adapter.getCount());
        setTabIcons();
    }

    private void setTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(UserMainActivity.this).inflate(R.layout.custom_tab, null);
                ImageView tab_icon = view.findViewById(R.id.tab_icon);
                TextView tab_label = view.findViewById(R.id.tab_label);
                tab.setCustomView(view);
                if (0 == i) {
                    OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                    listener.onRefresh(viewPager.getCurrentItem(),notification_count);
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
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("allVenueBottles")){
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
        }else  if (tag.equalsIgnoreCase("notification/counts")){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                notification_count = data.getString("notification_count");
                OnFragmentViewPagerChangeListener listener = (OnFragmentViewPagerChangeListener) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                listener.onRefresh(viewPager.getCurrentItem(),notification_count);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
        resources.updateConfiguration(config, dm);

        getNotificationCount();
        registerReceiver(receiver,new IntentFilter("pushNotification"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
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
       if(SessionManager.isLogged()){
           HashMap<String,String> param = new HashMap<>();
           param.put("action","notification/counts");
           param.put("type","User");
           param.put("user_id", SessionManager.getEncryptedID());
           communication.callPOST(param);
       }
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
                    return new UserProfileFragment();

                case 1:
                    return new UserItinereryFragment();

                case 2:
                    return new UserRecommendationFragment();

                case 3:
                    return new UserPromotionFragment();

                default:
                    return new UserProfileFragment();

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
                return getString(R.string.my_account_n);

            case 1:
                return getString(R.string.my_booking);

            case 2:
                return getString(R.string.recommendations);

            case 3:
                return getString(R.string.event_promo);

            default:
                return getString(R.string.my_account_n);

        }
    }
    public interface OnFragmentViewPagerChangeListener{
        void onRefresh(int page, String notification_count);
    }


    @Override
    public void onBackPressed() {
        if (AppController.getExoPlayer()!=null){
            AppController.getExoPlayer().stop();
        }
        super.onBackPressed();
    }



}
