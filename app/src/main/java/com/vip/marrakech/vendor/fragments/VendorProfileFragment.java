package com.vip.marrakech.vendor.fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.admin.activities.EditProfileAdminActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.AvailabilityActivity;
import com.vip.marrakech.vendor.activities.InAppPasswordChangeActivity;
import com.vip.marrakech.vendor.activities.VendorMainActivity;
import com.vip.marrakech.vendor.activities.WebViewActivity;
import com.vip.marrakech.vendor.dialog.ChangePasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorProfileFragment extends Fragment implements OnCallBackListener, View.OnClickListener, VendorMainActivity.OnFragmentViewPagerChangeListener {
    private Communication communication;
    private TextView tv_name, tv_email, tv_number, tv_availability, tv_address, tv_change_password, tv_subscription,
            tv_subscription_expiry__date,tv_in_app_password;
    private SimpleDraweeView iv_profile;
    private ImageView iv_subscription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_subscription = view.findViewById(R.id.tv_subscription);
        tv_subscription_expiry__date = view.findViewById(R.id.tv_subscription_expiry__date);
        tv_number = view.findViewById(R.id.tv_number);
        tv_address = view.findViewById(R.id.tv_address);
        tv_change_password = view.findViewById(R.id.tv_change_password);
        tv_availability = view.findViewById(R.id.tv_availability);
        iv_subscription = view.findViewById(R.id.iv_subscription);
        tv_in_app_password = view.findViewById(R.id.tv_in_app_password);
        tv_name.setText(SessionManager.getName());
        tv_email.setText(SessionManager.getEmail());
        ImageView iv_log_out = view.findViewById(R.id.iv_log_out);
        ImageView iv_edit = view.findViewById(R.id.iv_edit);
        iv_log_out.setOnClickListener(this);
        iv_edit.setOnClickListener(this);

        tv_change_password.setOnClickListener(this);
        tv_availability.setOnClickListener(this);
        tv_subscription.setOnClickListener(this);
        tv_in_app_password.setOnClickListener(this);


        super.onViewCreated(view, savedInstanceState);
    }

    private void setSubscribe() {
        if (SessionManager.getVendorSubscriptionStatus().equalsIgnoreCase("Active")) {
            tv_subscription_expiry__date.setText(String.format("Account expires on : %s", SessionManager.getVendorExpiryDate()));
            if (SessionManager.getIsVendorSubscription() == 1) {
                SpannableString content = new SpannableString(TextUtils.htmlEncode(getResources().getString(R.string.set_underline_cancel_subscription)));
                iv_subscription.setImageResource(R.drawable.ic_clear);
                iv_subscription.setBackgroundResource(R.drawable.profile_circle);
              //  content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tv_subscription.setText(content);
            } else {
                SpannableString content = new SpannableString(TextUtils.htmlEncode(getResources().getString(R.string.set_underline_upgrade)));
                //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tv_subscription.setText(content);
                iv_subscription.setImageResource(R.drawable.ic_upgrade);
                iv_subscription.setBackgroundResource(0);
            }
        }else {
            tv_subscription_expiry__date.setText("");
            SpannableString content = new SpannableString(TextUtils.htmlEncode(getResources().getString(R.string.set_underline_upgrade)));
            //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv_subscription.setText(content);
            iv_subscription.setImageResource(R.drawable.ic_upgrade);
            iv_subscription.setBackgroundResource(0);
        }

    }

    private void getDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "user/detail/" + SessionManager.getEncryptedID());
        communication.callGET(param);
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("logout")) {

            try {
                SessionManager.setLogged(false);
                SessionManager.setVendorConfirmPassword(false);
                GoTo.startWithFinish(getActivity(), LoginActivity.class);
                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("user/detail/" + SessionManager.getEncryptedID())) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                tv_name.setText(data.getString("name"));
                tv_number.setText(data.getString("contact_number"));
                tv_email.setText(data.getString("email"));
                iv_profile.setImageURI(String.format("%s/users/%s/%s", ApiClient.HOST, data.getString("id"), data.getString("image")));
//                tv_address.setText(String.format("%s, %s", data.getString("state"), data.getString("city")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("cancel/subscription")) {
          getAccountInfo();
        } else if (tag.equals("getAccountInfo")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                SessionManager.setVendorExpiryDate(data.getString("expire_date"));
                SessionManager.setVendorSubscriptionType(data.getString("subscription_type"));
                SessionManager.setVendorIsSubscription(data.getInt("is_subscribed"));
                SessionManager.setVendorSubscriptionStatus(data.getString("subscription_status"));
                setSubscribe();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAccountInfo() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "getAccountInfo");
        communication.callGET(param);
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onResume() {
        getDetail();
        getAccountInfo();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_change_password) {
            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.show(getChildFragmentManager(), "Change Password");
        } else if (view.getId() == R.id.tv_availability) {
            GoTo.start(getActivity(), AvailabilityActivity.class);
        } else if (view.getId() == R.id.tv_subscription) {
            if (SessionManager.getVendorSubscriptionStatus().equalsIgnoreCase("Active")) {
                tv_subscription_expiry__date.setText(String.format("Account expire On : %s", SessionManager.getVendorExpiryDate()));
                if (SessionManager.getIsVendorSubscription() == 1) {
                    cancelSubscription();

                } else {
                    GoTo.start(getActivity(), WebViewActivity.class);
                }
            }else {
                GoTo.start(getActivity(), WebViewActivity.class);
            }
        } else if (view.getId() == R.id.iv_edit) {
            GoTo.start(getActivity(), EditProfileAdminActivity.class);
        } else if (view.getId() == R.id.tv_in_app_password) {
            GoTo.start(getActivity(), InAppPasswordChangeActivity.class);
        } else if (view.getId() == R.id.iv_log_out) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to logout?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("action", "logout");
                            communication.callPOST(param);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void cancelSubscription() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to cancel Subscription?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "cancel/subscription");
                        communication.callPOST(param);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        getDetail();
        setSubscribe();
    }
}
