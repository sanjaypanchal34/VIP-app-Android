package com.vip.marrakech.admin.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.admin.activities.EditProfileAdminActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.dialog.ChangePasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminProfileFragment extends Fragment implements OnCallBackListener, AdminMainActivity.OnFragmentViewPagerChangeListener, View.OnClickListener {
    private Communication communication;
    private TextView tv_name, tv_email, tv_number, tv_address;
    private SimpleDraweeView iv_profile;
    private TextView tv_change_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        tv_change_password = view.findViewById(R.id.tv_change_password);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_number = view.findViewById(R.id.tv_number);
        tv_address = view.findViewById(R.id.tv_address);
        ImageView iv_log_out = view.findViewById(R.id.iv_log_out);
        ImageView iv_edit = view.findViewById(R.id.iv_edit);
        iv_log_out.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
     /*   toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.opt_log_out){

                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure you want to logout?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String,String> param = new HashMap<>();
                                    param.put("action","logout");
                                    communication.callPOST(param);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }else if (item.getItemId() == R.id.opt_edit){
                    GoTo.start(getActivity(), EditProfileAdminActivity.class);
                }
                return false;
            }
        });*/

        tv_change_password.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
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


                //tv_address.setText(String.format("%s, %s", data.getString("state"), data.getString("city")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {

    }

    @Override
    public void onResume() {
        getDetail();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_change_password) {
            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.show(getChildFragmentManager(), "Change Password");
        } else if (v.getId() == R.id.iv_edit) {
            GoTo.start(getActivity(), EditProfileAdminActivity.class);
        } else if (v.getId() == R.id.iv_log_out) {
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
}
