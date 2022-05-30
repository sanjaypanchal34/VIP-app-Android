package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class InAppOTPVerifyDialog extends DialogFragment
        implements View.OnClickListener, OnCallBackListener {

    private Communication communication;
    private Toolbar toolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_in_app_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communication = new Communication(getActivity(), this);
        initUI(view);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
    }

    private void initUI(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        final OtpView otpView = view.findViewById(R.id.otp_view);
        final LinearLayout layout = (LinearLayout)view.findViewById(R.id.ln_otp);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = (int) (layout.getMeasuredWidth() -getResources().getDimension(R.dimen._15sdp));
                int height = layout.getMeasuredHeight();
                otpView.setItemWidth(width/4);

            }
        });

        Button btn_verify = view.findViewById(R.id.btn_verify);
        otpView.setCursorVisible(true);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
               /* HashMap<String, String> param = new HashMap<>();
                param.put("action", "vendor/validate/otp");
                param.put("vendor_id", SessionManager.getEncryptedID());
                param.put("otp", otp);
                communication.callPOST(param);*/
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpView.getText().toString().length() == 4){
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "vendor/validate/otp");
                    param.put("vendor_id", SessionManager.getEncryptedID());
                    param.put("otp", otpView.getText().toString());
                    communication.callPOST(param);
                }else {
                    Toast.makeText(getActivity(), "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);//solves issue with statusbar
                dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                onBackPress();
            }
        };
    }

    private void onBackPress() {
       /* adapter.notifyDataSetChanged();
        scrollListener.resetState();*/
        dismiss();
        //listener.onDismiss();
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {

        if (tag.equals("vendor/validate/otp")) {
            Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            listener.onOTPVerify();
            dismiss();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    private OnOTPListener listener;

    public void setListener(OnOTPListener listener) {
        this.listener = listener;
    }

    public interface OnOTPListener {
        void onOTPVerify();

    }
}