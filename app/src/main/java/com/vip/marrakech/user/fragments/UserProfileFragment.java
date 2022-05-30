package com.vip.marrakech.user.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.BuildConfig;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.activities.RegisterActivity;
import com.vip.marrakech.admin.activities.EditProfileAdminActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.user.dialogs.HelpDialog;
import com.vip.marrakech.user.interfaces.OnGuestLoginListener;
import com.vip.marrakech.vendor.activities.VendorMainActivity;
import com.vip.marrakech.vendor.dialog.ChangePasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class UserProfileFragment extends Fragment implements OnCallBackListener, View.OnClickListener, VendorMainActivity.OnFragmentViewPagerChangeListener, UserMainActivity.OnFragmentViewPagerChangeListener {
    private Communication communication;
    private TextView tv_name, tv_email, tv_number, tv_address, tv_change_password;
    private SimpleDraweeView iv_profile;
    private TextView tv_change_language;
    private int selectedLanguage = 0;
    private LinearLayout ln_share, ln_language;
    private TextView tv_register;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        selectedLanguage = SessionManager.getLanguage().equalsIgnoreCase("en") ? 0 : 1;
        communication = new Communication(getActivity(), this);
        iv_profile = view.findViewById(R.id.iv_profile);
        ln_language = view.findViewById(R.id.ln_language);
        ln_share = view.findViewById(R.id.ln_share);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_number = view.findViewById(R.id.tv_number);
        tv_register = view.findViewById(R.id.tv_register);
        tv_address = view.findViewById(R.id.tv_address);
        tv_change_password = view.findViewById(R.id.tv_change_password);
        tv_change_language = view.findViewById(R.id.tv_change_language);
        tv_change_language.setVisibility(View.VISIBLE);
        ln_language.setVisibility(View.VISIBLE);
        view.findViewById(R.id.iv_language).setVisibility(View.VISIBLE);
        ImageView iv_log_out = view.findViewById(R.id.iv_log_out);
        ImageView iv_edit = view.findViewById(R.id.iv_edit);
        tv_name.setText(SessionManager.getName());
        tv_email.setText(SessionManager.getEmail());
        tv_change_password.setOnClickListener(this);
        tv_change_language.setOnClickListener(this);
        iv_log_out.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        ln_share.setVisibility(View.VISIBLE);
        ln_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Download The VIP App - everyone’s personal concierge.\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        ((LinearLayout) view.findViewById(R.id.ln_help)).setVisibility(View.VISIBLE);
        ((LinearLayout) view.findViewById(R.id.ln_help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog dialog = new HelpDialog();
                dialog.show(getChildFragmentManager(), "help");
            }
        });

        tv_change_password.setVisibility(SessionManager.isLogged() ? View.VISIBLE : View.GONE);
        tv_change_password.setVisibility(SessionManager.isLogged() ? View.VISIBLE : View.GONE);
        ((LinearLayout) view.findViewById(R.id.ln_edit)).setVisibility(SessionManager.isLogged() ? View.VISIBLE : View.GONE);
        tv_register.setVisibility(SessionManager.isLogged() ? View.GONE : View.VISIBLE);
        ((LinearLayout) view.findViewById(R.id.ln_logout)).setVisibility(SessionManager.isLogged() ? View.VISIBLE : View.GONE);


        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setLogged(false);
                SessionManager.setVendorConfirmPassword(false);
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                config.setLocale(new Locale("en"));
                resources.updateConfiguration(config, dm);
                GoTo.startWithClearTop(getActivity(), RegisterActivity.class);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDetail() {

        if (SessionManager.isLogged()) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "user/detail/" + SessionManager.getEncryptedID());
            if (communication != null)
                communication.callGET(param);

        }
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("logout")) {

            try {
                SessionManager.setLogged(false);
                SessionManager.setVendorConfirmPassword(false);
                SessionManager.setLanguage("en");
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
                resources.updateConfiguration(config, dm);
                GoTo.startWithFinish(getActivity(), LoginActivity.class);
                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("update/user/language")) {

            try {
                SessionManager.setLanguage(selectedLanguage == 0 ? "en" : "fr");
                onLanguageListener.onLLanguageChange();
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
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onResume() {
        getDetail();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_change_password) {
            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.show(getChildFragmentManager(), "Change Password");
        } else if (view.getId() == R.id.tv_change_language) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Choose a Language");
            final String[] listItems = new String[]{"English", "French"};
            alertDialog.setSingleChoiceItems(listItems, selectedLanguage, new DialogInterface.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedLanguage = which;
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "update/user/language");
                    param.put("lang", selectedLanguage == 0 ? "en" : "fr");
                    param.put("user_id", SessionManager.getEncryptedID());
                    communication.callPOST(param);
                    dialog.dismiss();
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // create and build the AlertDialog instance
            // with the AlertDialog builder instance
            AlertDialog customAlertDialog = alertDialog.create();

            // show the alert dialog when the button is clicked
            customAlertDialog.show();
        } else if (view.getId() == R.id.iv_edit) {
            GoTo.start(getActivity(), EditProfileAdminActivity.class);
        } else if (view.getId() == R.id.iv_log_out) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.are_you_sure_want_logout)

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

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        getDetail();
    }

    @Override
    public void onRefresh(int page, String notification_count) {
        if (SessionManager.isLogged()) {
            getDetail();
        }/*else if(getActivity()!=null){
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.please_login)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.cont), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            GoTo.startWithClearTop(getActivity(),LoginActivity.class);
                        }
                    })

                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onLoginActionCancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }*/
    }


    public interface OnLanguageListener {
        void onLLanguageChange();
    }

    private OnLanguageListener onLanguageListener;
    public OnGuestLoginListener listener;


    public void setOnLanguageListener(OnLanguageListener onLanguageListener, OnGuestLoginListener listener) {
        this.onLanguageListener = onLanguageListener;
        this.listener = listener;
    }
}
