package com.vip.marrakech.user.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moeidbannerlibrary.banner.BannerLayout;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.PdfViewActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.activities.VideoPlayerActivity;
import com.vip.marrakech.adapters.TopDetailInfoAdapter;
import com.vip.marrakech.admin.activities.AdminTopEditActivity;
import com.vip.marrakech.admin.models.AdminRecommendationDetail;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.admin.models.Image;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.BaseBannerAdapter;
import com.vip.marrakech.user.models.UerItinareryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TopDetailUserActivity extends AppCompatActivity implements OnCallBackListener {

    private String id;
    private TextView tv_user_venue_title, tv_title, tv_book_now;
    private TextView tv_user_promotion_desc;
    private SliderView banner;
    private AdminRecommendationDetail detail;
    private String type;
    private TextView iv_location;
    private RecyclerView rv_info;
    private List<Map<String, String>> infoMap = new ArrayList<>();
    private Button btn_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_detail_of_user);
        Communication communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        banner = findViewById(R.id.imageSlider);
        rv_info = findViewById(R.id.rv_info);
        rv_info.setLayoutManager(new LinearLayoutManager(TopDetailUserActivity.this));

        iv_location = findViewById(R.id.iv_location);
        iv_location.setVisibility(View.GONE);
        tv_user_venue_title = findViewById(R.id.tv_user_venue_title);
        tv_title = findViewById(R.id.tv_title);
        tv_book_now = findViewById(R.id.tv_book_now);
        LinearLayout ln_book = findViewById(R.id.ln_book);
         btn_menu = findViewById(R.id.btn_menu);
        tv_user_promotion_desc = findViewById(R.id.tv_user_promotion_desc);

        id = GoTo.getIntent(this).getString("id");
        type = GoTo.getIntent(this).getString("type");

        if (id != null && type != null) {
            HashMap<String, String> param = new HashMap<>();
            if (type.equals("7")) {
                btn_menu.setVisibility(View.GONE);
                ln_book.setVisibility(View.GONE);
                param.put("action", String.format("attraction/detail/%s", id));
            } else {
                param.put("action", String.format("recommandation/detail/%s", id));
            }
            communication.callGET(param);


            ln_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(SessionManager.isLogged()){
                        if (detail != null) {
                            if (detail.getVenue_type().equalsIgnoreCase("spa")) {
                                Uri u = Uri.parse("tel:" + detail.getPhone_no());
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                try {
                                    startActivity(i);
                                } catch (SecurityException s) {
                                    Toast.makeText(TopDetailUserActivity.this, s.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_venue", true);
                                bundle.putString("venueID", detail.getVenue_id());
                                bundle.putString("venueName", detail.getVenue_title());
                                bundle.putString("venueType", detail.getVenue_type());
                                bundle.putString("type", "R");
                                bundle.putString("latitude", detail.getLatitude());
                                bundle.putString("longitude", detail.getLongitude());
                                bundle.putString("type_id", id);
                                bundle.putString("deposit_option", detail.getDeposit_option());
                                GoTo.startWithExtra(TopDetailUserActivity.this, UserBookNowActivity.class, bundle);
                            }
                        }
                    }else{
                        new AlertDialog.Builder(TopDetailUserActivity.this)
                                .setTitle(R.string.start_now)
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
                                        GoTo.startWithClearTop(TopDetailUserActivity.this, RegisterActivity.class);
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
                    if (detail != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude", detail.getLatitude());
                        bundle.putString("longitude", detail.getLongitude());
                        bundle.putString("venueName", detail.getVenue_title());
                        GoTo.startWithExtra(TopDetailUserActivity.this, UserMapActivity.class, bundle);
                    }
                }
            });


            btn_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.getExtention().isEmpty()) {
                        Toast.makeText(TopDetailUserActivity.this, getResources().getString(R.string.no_menu_avavilable), Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "Menu");
                        if (detail.getExtention().contains("pdf")) {
                            bundle.putString("url", String.format("http://docs.google.com/gview?embedded=true&url=%s/recommandation/%s/menu/%s", ApiClient.HOST, detail.getId(), detail.getMenu_image()));
                            GoTo.startWithExtra(TopDetailUserActivity.this, PdfViewActivity.class, bundle);
                        } else {
                            bundle.putString("url", String.format("%s/recommandation/%s/menu/%s", ApiClient.HOST, detail.getId(), detail.getMenu_image()));
                            GoTo.startWithExtra(TopDetailUserActivity.this, PdfViewActivity.class, bundle);
                        }
                    }
                }
            });
        }


    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("recommandation/detail/%s", id)) || tag.equals(String.format("attraction/detail/%s", id))) {
            try {
                detail = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), AdminRecommendationDetail.class);
                if (detail.getLatitude() == null || detail.getLongitude() == null || detail.getLatitude().isEmpty() || detail.getLongitude().isEmpty()) {
                    iv_location.setVisibility(View.GONE);
                } else {
                    iv_location.setVisibility(View.VISIBLE);
                }
                tv_user_venue_title.setText(detail.getVenue_title());
                tv_title.setText(String.format("%s", detail.getTitle()));
                tv_user_promotion_desc.setText(detail.getDescription());

                if (detail.getVenue_type() != null && detail.getVenue_type().equalsIgnoreCase("Spa")) {
                    tv_book_now.setText(getResources().getString(R.string.call_now));
                } else {
                    tv_book_now.setText(getResources().getString(R.string.book_now));

                }

               /* if(detail.getVenue_type().equalsIgnoreCase("Day Party")){
                    btn_menu.setText(getResources().getString(R.string.bed_option_menu));
                }else if(detail.getVenue_type().equalsIgnoreCase("Nightclub")){
                    btn_menu.setText(getResources().getString(R.string.table_option_menu));
                }else{
                    btn_menu.setText(getResources().getString(R.string.menu));
                }*/
                btn_menu.setText(getResources().getString(R.string.menu));
                infoMap.clear();
                if (!detail.getLabel1().isEmpty()&&!detail.getBusiest_days().isEmpty())
                    infoMap.add(ImmutableMap.of(detail.getLabel1(), detail.getBusiest_days()));

                if (!detail.getLabel2().isEmpty()&&!detail.getMusic_type().isEmpty())
                    infoMap.add(ImmutableMap.of(detail.getLabel2(), detail.getMusic_type()));

                if (!detail.getLabel3().isEmpty()&&!detail.getOpening_hours().isEmpty())
                    infoMap.add(ImmutableMap.of(detail.getLabel3(), detail.getOpening_hours()));

                if (!detail.getLabel4().isEmpty()&&!detail.getDress_code().isEmpty())
                    infoMap.add(ImmutableMap.of(detail.getLabel4(), detail.getDress_code()));





                rv_info.setAdapter(new TopDetailInfoAdapter(TopDetailUserActivity.this,infoMap));
              /*  final List<String> urls = new ArrayList<>();
                for (int i = 0; i < detail.getImageList().size(); i++) {
                        urls.add(String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), detail.getImageList().get(i).getImageName()));
                }*/
                SliderViewAdapter<TopDetailUserActivity.BannerViewPager> webBannerAdapter = new SliderViewAdapter<TopDetailUserActivity.BannerViewPager>() {
                    @Override
                    public TopDetailUserActivity.BannerViewPager onCreateViewHolder(ViewGroup parent) {
                        return new TopDetailUserActivity.BannerViewPager(LayoutInflater.from(TopDetailUserActivity.this).inflate(R.layout.banner_item, parent, false));
                    }

                    @Override
                    public void onBindViewHolder(TopDetailUserActivity.BannerViewPager holder, int position) {
                        com.vip.marrakech.admin.models.Image image = detail.getImageList().get(position);
                        Log.e("VIDEO THUMB:::", String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), image.getThumb_name()));
                        if (image.getType().equalsIgnoreCase("video")) {
                            // holder.iv_image.setImageURI(String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), image.getThumb_name()));
                            holder.iv_play.setVisibility(View.VISIBLE);
                            holder.skeletonLayout.showSkeleton();
                            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                    .setUri(image.getThumb_name())
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
                        } else {
                            holder.iv_play.setVisibility(View.GONE);
                            // holder.iv_image.setImageURI(String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), image.getImageName()));
                            holder.skeletonLayout.showSkeleton();
                            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                    .setUri(String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), image.getImageName()))
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
                        }
                        holder.iv_clear.setVisibility(View.GONE);

                        holder.iv_play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TopDetailUserActivity.this, VideoPlayerActivity.class);
                                if (image.isLocal()) {
                                    intent.putExtra("link", image.getImageName());
                                } else {
                                    intent.putExtra("link", String.format("%s/recommandation/%s/%s", ApiClient.HOST, detail.getId(), image.getImageName()));
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public int getCount() {
                        return detail.getImageList().size();
                    }
                };

                banner.setSliderAdapter(webBannerAdapter);
                if (detail.getImageList().size() > 1) {
                    banner.setAutoCycle(true);
                    banner.setScrollTimeInSec(3);
                    banner.startAutoCycle();
                }
//                banner.setIndicatorAnimation(IndicatorAnimations.WORM);
//                banner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    private class BannerViewPager extends SliderViewAdapter.ViewHolder {

        private SkeletonLayout skeletonLayout;
        private SimpleDraweeView iv_image;
        private ImageView iv_clear, iv_play;

        BannerViewPager(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_clear = itemView.findViewById(R.id.iv_clear);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }


}

