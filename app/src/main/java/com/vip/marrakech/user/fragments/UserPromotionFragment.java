package com.vip.marrakech.user.fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.PromotionDetailUserActivity;
import com.vip.marrakech.user.activities.UserBookNowActivity;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.user.activities.UserNotificationActivity;
import com.vip.marrakech.user.adapters.UserPromotionAdapter;
import com.vip.marrakech.user.models.UserPromotionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserPromotionFragment extends Fragment implements OnCallBackListener, UserMainActivity.OnFragmentViewPagerChangeListener {
    private RecyclerView rv_user_promotion;
    private List<UserPromotionModel> list = new ArrayList<>();
    private UserPromotionAdapter adapter;
    private Communication communication;
    private Toolbar toolBar;
    private TextView tv_notification_count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_promostion, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);

        LinearLayout ln_book = view.findViewById(R.id.ln_book);
        ln_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.start(getActivity(), UserBookNowActivity.class);
            }
        });

        getPromotions();
        super.onViewCreated(view, savedInstanceState);
    }

    private void getPromotions() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "promotion/list");
        if (communication != null)
            communication.callGET(param);
    }

    private void initUI(View view) {
        rv_user_promotion = view.findViewById(R.id.rv_user_promotion);
        rv_user_promotion.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserPromotionAdapter(getActivity(), list);
        adapter.setListener(new UserPromotionAdapter.OnPromotionListener() {
            @Override
            public void onPromotionClick(UserPromotionModel model) {
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getEncrypted_id());
                bundle.putString("vID", model.getVenue_id());
                bundle.putString("vName", model.getVenueTitle());
                bundle.putString("vType", model.getVenue_type());
                GoTo.startWithExtra(getActivity(), PromotionDetailUserActivity.class, bundle);
            }
        });
        rv_user_promotion.setAdapter(adapter);
        toolBar = view.findViewById(R.id.toolBar);
        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SessionManager.isLogged()) {
                    GoTo.start(getActivity(), UserNotificationActivity.class);
                }else{
                    new AlertDialog.Builder(getActivity())
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
    }

    @Override
    public void onRefresh(int page, String notification_count) {
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }
        getPromotions();
        /*if(getActivity()!=null && SessionManager.isLogged()) {

        }else{
            new AlertDialog.Builder(getActivity())
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
                            GoTo.startWithClearTop(getActivity(), LoginActivity.class);
                        }
                    })

                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }*/
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("promotion/list")) {

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray promotions = data.getJSONArray("promotions");
                list.clear();
                for (int i = 0; i < promotions.length(); i++) {
                    UserPromotionModel model = new Gson().fromJson(promotions.getJSONObject(i).toString(), UserPromotionModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                Log.e("Promotion Size:::", String.valueOf(list.size()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
