package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class ChangePasswordDialog extends DialogFragment
        implements View.OnClickListener, OnCallBackListener {

    private ItemClickListener listener;
    private TextInputEditText edt_old_password,edt_new_password,edt_confirm_password;
    private Communication communication;
    private Toolbar toolBar;
    private Button btn_confirm;
//    LayoutBottomSheetBinding bi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.change_password_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communication = new Communication(getActivity(),this);
        initUI(view);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
        btn_confirm.setOnClickListener(this);
    }

    private void initUI(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        edt_old_password = view.findViewById(R.id.edt_old_password);
        edt_new_password = view.findViewById(R.id.edt_new_password);
        edt_confirm_password = view.findViewById(R.id.edt_confirm_password);
        btn_confirm = view.findViewById(R.id.btn_confirm);
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
        switch (view.getId()){
            case R.id.btn_confirm:{
                if (edt_old_password.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Old password", Toast.LENGTH_SHORT).show();
                }else if (edt_new_password.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter New password", Toast.LENGTH_SHORT).show();
                }else if (edt_confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Confirm New password", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "change/password");
                    param.put("current_password",edt_old_password.getText().toString());
                    param.put("new_password",edt_new_password.getText().toString());
                    param.put("confirm_password",edt_confirm_password.getText().toString());
                    communication.callPOST(param);
                }
            }break;
        }
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
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("change/password")){
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    public interface ItemClickListener {
        void onItemClick(String booking);
        void onItemClick(String booking, String startDate, String endDate);
    }
}