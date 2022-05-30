package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class ProfilePasswordDialog extends DialogFragment implements OnCallBackListener {

    private Communication communication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_profile_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(),this);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final EditText edt_password = view.findViewById(R.id.edt_password);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validator.isEmpty(edt_password)) {
                    Validator.setError(edt_password, "Enter Password");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "vendor/validate/password");
                    param.put("vendor_id", SessionManager.getEncryptedID());
                    param.put("password", Validator.getText(edt_password));
                    communication.callPOST(param);
                }


            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private OnPasswordConfirmListener listener;

    public void setListener(OnPasswordConfirmListener listener) {
        this.listener = listener;
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {
        if (tag.equalsIgnoreCase("vendor/validate/password")){
            if (listener!=null){
                SessionManager.setVendorConfirmPassword(true);
                listener.onPasswordConfirm();
                dismiss();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    public interface OnPasswordConfirmListener{
        void onPasswordConfirm();
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }
}
