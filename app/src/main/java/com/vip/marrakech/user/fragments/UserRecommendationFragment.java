package com.vip.marrakech.user.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.activities.VideoPlayerActivity;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.activities.AdminTopAddActivity;
import com.vip.marrakech.admin.activities.AdminTopListActivity;
import com.vip.marrakech.admin.adapters.AdminRecommendationAdapter;
import com.vip.marrakech.admin.models.AdminRecommendation;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.LatestMixesActivity;
import com.vip.marrakech.user.activities.TopDetailUserActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.user.activities.TopListOfUserActivity;
import com.vip.marrakech.user.activities.UserBookNowActivity;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.user.activities.UserNotificationActivity;
import com.vip.marrakech.user.activities.UserRecommendationSearchActivity;
import com.vip.marrakech.user.dialogs.FAQDialog;
import com.vip.marrakech.user.dialogs.FAQFullDialog;
import com.vip.marrakech.user.dialogs.HelpDialog;
import com.vip.marrakech.user.dialogs.UserTopPickDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserRecommendationFragment extends Fragment implements UserMainActivity.OnFragmentViewPagerChangeListener, OnCallBackListener {
    private Toolbar toolBar;
    private TextView tv_notification_count;
    private RecyclerView rv_recommendation;
    private List<AdminRecommendation> list = new ArrayList<>();
    private AdminRecommendationAdapter adapter;
    private Communication communication;
    private LinearLayout ln_music;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_recommandation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        communication = new Communication(getActivity(), this);
        Button btn_top_pick = view.findViewById(R.id.btn_top_pick);
        LinearLayout ln_faq = view.findViewById(R.id.ln_faq);

        btn_top_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTopPickDialog pickDialog = new UserTopPickDialog();
                pickDialog.setListener(new UserTopPickDialog.OnTopClickListener() {
                    @Override
                    public void onTopPickCLick(UserTopPickDialog.TopPickModel pickModel) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", pickModel.getName());
                        bundle.putString("type", pickModel.getRecommandation_type_id());
                        bundle.putString("type", pickModel.getRecommandation_type_id());
                        GoTo.startWithExtra(getActivity(), TopListOfUserActivity.class, bundle);
                    }
                });
                pickDialog.show(getChildFragmentManager(), "top pick");
            }
        });
        ln_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("link", String.valueOf(RawResourceDataSource.buildRawResourceUri(R.raw.app_guide)));
                startActivity(intent);

               /* new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.currently_video_not_available)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();*/
            }
        });


        toolBar = view.findViewById(R.id.toolBar);

        ImageView iv_search = toolBar.findViewById(R.id.iv_search);
        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        RelativeLayout opt_help = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_help).getActionView();
//        RelativeLayout search = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_search).getActionView();

        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SessionManager.isLogged()) {
                    GoTo.start(getActivity(), UserNotificationActivity.class);
                }else  if(getActivity()!=null){
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.start_now))
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
                                    GoTo.startWithClearTop(getActivity(), RegisterActivity.class);
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

        opt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog dialog = new HelpDialog();
                dialog.show(getChildFragmentManager(),"help");
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.start(getActivity(), UserRecommendationSearchActivity.class);
            }
        });

        ln_music = view.findViewById(R.id.ln_music);
        rv_recommendation = view.findViewById(R.id.rv_recommendation);
        rv_recommendation.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminRecommendationAdapter(getActivity(), list);
        adapter.setListener(new AdminRecommendationAdapter.OnRecommendationListener() {
            @Override
            public void onRecommendationClick(AdminRecommendation model) {

                if (model.getId().equals("8")) {
                    FAQFullDialog faqDialog = new FAQFullDialog();
                    faqDialog.show(getChildFragmentManager(), "faq");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", model.getType_name());
                    bundle.putString("type", model.getId());
                    bundle.putString("type", model.getId());
                    GoTo.startWithExtra(getActivity(), TopListOfUserActivity.class, bundle);
                }
            }
        });
        rv_recommendation.setAdapter(adapter);

        ln_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            GoTo.start(getActivity(), LatestMixesActivity.class);
                        } else {
                            Toast.makeText(getActivity(),getResources().getString( R.string.permission_need_for_download), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        getList();

    }

    @Override
    public void onRefresh(int page, String notification_count) {
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }

        getList();
    }


    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "recommandation/type/list");
        if (communication != null)
            communication.callGET(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("recommandation/type/list")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray recommandation_types = data.getJSONArray("recommandation_types");
                list.clear();
                for (int i = 0; i < recommandation_types.length(); i++) {
                    AdminRecommendation recommendation = new Gson().fromJson(recommandation_types.getJSONObject(i).toString(), AdminRecommendation.class);
                    list.add(recommendation);
                }
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
