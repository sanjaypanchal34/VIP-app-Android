package com.vip.marrakech.user.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moeidbannerlibrary.banner.BannerLayout;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.adapters.TopDetailInfoAdapter;
import com.vip.marrakech.admin.activities.AddPromotionActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.BaseBannerAdapter;
import com.vip.marrakech.user.models.UserPromotionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PromotionDetailUserActivity extends AppCompatActivity implements OnCallBackListener {

    private String id;
    private TextView tv_user_promotion_title, tv_user_promotion_desc;
    private TextView tv_title;
    private SliderView banner;
    private String vID;
    private String vName;
    private String vType;
    private String longitude, latitude;
    private TextView iv_location;
    private RecyclerView rv_info;
    private List<Map<String, String>> infoMap = new ArrayList<>();
    private LinearLayout ln_book;
    private String deposit_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail_user);
        Communication communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        banner = findViewById(R.id.imageSlider);
        iv_location = findViewById(R.id.iv_location);
        tv_user_promotion_title = findViewById(R.id.tv_user_venue_title);
        tv_title = findViewById(R.id.tv_title);
        tv_user_promotion_desc = findViewById(R.id.tv_user_promotion_desc);
        rv_info = findViewById(R.id.rv_info);
        rv_info.setLayoutManager(new LinearLayoutManager(PromotionDetailUserActivity.this));
        id = GoTo.getIntent(this).getString("id");
        vID = GoTo.getIntent(this).getString("vID");
        vName = GoTo.getIntent(this).getString("vName");
        vType = GoTo.getIntent(this).getString("vType");

        if (id != null) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", String.format("promotion/detail/%s", id));
            communication.callGET(param);

             ln_book = findViewById(R.id.ln_book);
            ln_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(SessionManager.isLogged()){
                       Bundle bundle = new Bundle();
                       bundle.putBoolean("is_venue", true);
                       bundle.putString("venueID", vID);
                       bundle.putString("venueName", vName);
                       bundle.putString("venueType", vType);
                       bundle.putString("latitude", latitude);
                       bundle.putString("longitude", longitude);
                       bundle.putString("type", "P");
                       bundle.putString("type_id", id);
                       bundle.putString("deposit_option", deposit_option);
                       GoTo.startWithExtra(PromotionDetailUserActivity.this, UserBookNowActivity.class, bundle);

                   }else{
                       new AlertDialog.Builder(PromotionDetailUserActivity.this)
                               .setTitle(getString(R.string.view_promotion))
                               .setMessage(R.string.please_login)
                               .setPositiveButton(getString(R.string.cont), new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       SessionManager.setLogged(false);
                                       SessionManager.setVendorConfirmPassword(false);
                                       Resources resources = getResources();
                                       DisplayMetrics dm = resources.getDisplayMetrics();
                                       Configuration config = resources.getConfiguration();
                                       config.setLocale(new Locale("en"));
                                       resources.updateConfiguration(config, dm);
                                       GoTo.startWithClearTop(PromotionDetailUserActivity.this, RegisterActivity.class);
                                   }
                               })

                               .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   }
                               })
                               .show();
                   }

                }
            });

            iv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (latitude != null && longitude != null){
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude", latitude);
                        bundle.putString("longitude", longitude);
                        bundle.putString("venueName", vName);
                        GoTo.startWithExtra(PromotionDetailUserActivity.this, UserMapActivity.class, bundle);
                    }
                }
            });
        }


    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("promotion/detail/%s", id))) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");

                tv_user_promotion_title.setText(data.getString("venue_id").isEmpty()?data.getString("custom_venue_title"):data.getString("venue_title"));
                tv_user_promotion_desc.setText(data.getString("description"));
                tv_title.setText(String.format("%s",data.getString("promotion_title")));
                SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String from_date = showFormat.format(format.parse(data.getString("from_date")));
                String to_date = showFormat.format(format.parse(data.getString("to_date")));
                longitude = data.getString("longitude");
                latitude = data.getString("latitude");
                deposit_option = data.getString("deposit_option");
                if (!latitude.isEmpty() && !longitude.isEmpty() && !data.getString("venue_id").isEmpty()){
                    iv_location.setVisibility(View.VISIBLE);
                }else {
                    iv_location.setVisibility(View.GONE);
                }

                ln_book.setVisibility(data.getString("venue_id").isEmpty()?View.GONE:View.VISIBLE);
                vID = data.getString("venue_id");
                vName = data.getString("venue_title");
                vType = data.getString("venue_type");

                infoMap.clear();
                if (!data.getString("label1").isEmpty()&&!data.getString("busiest_days").isEmpty())
                    infoMap.add(ImmutableMap.of(data.getString("label1"), data.getString("busiest_days")));
                if (!data.getString("label2").isEmpty()&&!data.getString("music_type").isEmpty())
                    infoMap.add(ImmutableMap.of(data.getString("label2"), data.getString("music_type")));
                if (!data.getString("label3").isEmpty()&&!data.getString("opening_hours").isEmpty())
                    infoMap.add(ImmutableMap.of(data.getString("label3"), data.getString("opening_hours")));
                if (!data.getString("label4").isEmpty()&&!data.getString("dress_code").isEmpty())
                    infoMap.add(ImmutableMap.of(data.getString("label4"), data.getString("dress_code")));

                rv_info.setAdapter(new TopDetailInfoAdapter(PromotionDetailUserActivity.this,infoMap));
                final JSONArray images = data.getJSONArray("images");
                final List<String> urls = new ArrayList<>();
                for (int i = 0; i < images.length(); i++) {
                    urls.add(String.format("%s/promotions/%s/%s", ApiClient.HOST, images.getJSONObject(i).getString("promotions_id"), images.getJSONObject(i).getString("image_name")));
                }
                SliderViewAdapter<BannerViewPager> webBannerAdapter = new SliderViewAdapter<BannerViewPager>() {
                    @Override
                    public BannerViewPager onCreateViewHolder(ViewGroup parent) {
                        return new BannerViewPager(LayoutInflater.from(PromotionDetailUserActivity.this).inflate(R.layout.banner_item, parent, false));
                    }

                    @Override
                    public void onBindViewHolder(BannerViewPager holder, int position) {
//                        holder.iv_image.setImageURI(urls.get(position));
                        holder.skeletonLayout.showSkeleton();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(urls.get(position))
                                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                                    @Override
                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                                        holder.skeletonLayout.showOriginal();
                                    }

                                    @Override
                                    public void onFailure(String id, Throwable throwable) {
                                        //Action on failure
                                    }

                                })
                                .build();
                        holder.iv_image.setController(controller);
                        holder.iv_clear.setVisibility(View.GONE);
                    }

                    @Override
                    public int getCount() {
                        return urls.size();
                    }
                };

                banner.setSliderAdapter(webBannerAdapter);
                if (urls.size()>1){
                    banner.setAutoCycle(true);
                    banner.setScrollTimeInSec(3);
                    banner.startAutoCycle();
               }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {
            onBackPressed();
    }

    private class BannerViewPager extends SliderViewAdapter.ViewHolder {

        private final SkeletonLayout skeletonLayout;
        private SimpleDraweeView iv_image;
        private ImageView iv_clear;

        BannerViewPager(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_clear = itemView.findViewById(R.id.iv_clear);
        }
    }
}
