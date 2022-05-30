package com.vip.marrakech.user.dialogs;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RestaurentBookingSummaryDialog extends DialogFragment implements OnCallBackListener {


    private MaterialSpinner sp_currency;
    private String date_time, pax;
    private String total_mad;
    private Communication communication;
    private String total_converted_price, charge_amount_MAD;
    private TextView tv_charge_mad, tv_charge_home, tv_for_female;
    private boolean isFemale;
    private String deposit_percentage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.dialog_retsturant_bookig_summary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);

        if (!isFemale) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "convert/amount");
            param.put("total_price", total_mad.replaceAll("MAD",""));
            param.put("currency", "MAD");
            param.put("pax", pax);
            param.put("deposit_percentage", "");
            communication.callPOST(param);
            tv_for_female.setVisibility(View.GONE);
        } else {
            tv_for_female.setVisibility(View.VISIBLE);
        }


        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        tv_charge_mad = view.findViewById(R.id.tv_charge_mad);
        tv_charge_home = view.findViewById(R.id.tv_charge_home);
        tv_for_female = view.findViewById(R.id.tv_for_female);
        TextView tv_pax = view.findViewById(R.id.tv_pax);
        TextView tv_date = view.findViewById(R.id.tv_date);
        sp_currency = view.findViewById(R.id.sp_currency);
        sp_currency.setItems("MAD", "GBP", "EUR", "USD");

        tv_charge_mad.setText("0");
        tv_charge_home.setText("0");

        tv_date.setText(String.format( getResources().getString(R.string.time_date_n_s),date_time));
        tv_pax.setText(String.format("%s:\n%s", getResources().getString(R.string.group_size),pax));


        sp_currency.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (!sp_currency.getText().toString().isEmpty() && !isFemale) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "convert/amount");
                    param.put("total_price", total_mad.replaceAll("MAD",""));
                    param.put("currency", sp_currency.getText().toString());
                    param.put("pax", pax);
                    param.put("deposit_percentage", "");
                    communication.callPOST(param);
                }
            }
        });


        view.findViewById(R.id.tv_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                lickListener.onPayment_click(sp_currency.getText().toString().isEmpty()?"MAD":sp_currency.getText().toString());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), (int) (size.y * 0.9));
        window.setGravity(Gravity.CENTER);
    }

    public void setData(String date_time, String pax, String total_mad, boolean isFemale, String deposit_percentage) {
       /* SimpleDateFormat main = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        SimpleDateFormat change = new SimpleDateFormat("EEE dd/MM hh:mm a",Locale.ENGLISH);
        try {
            Date mainDate = main.parse(date_time);
            this.date_time = change.format(mainDate);
        } catch (ParseException e) {
            e.printStackTrace();

        }*/
        this.date_time = date_time;
        this.pax = pax;
        this.total_mad = total_mad;
        this.isFemale = isFemale;
        this.deposit_percentage = deposit_percentage;

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {

        if (tag.equals("convert/amount")) {
            JSONObject data = jsonObject.getJSONObject("data");
            total_converted_price = data.getString("charge_amount");
            charge_amount_MAD = data.getString("charge_amount_MAD");
            tv_charge_home.setText(total_converted_price);
            tv_charge_mad.setText(charge_amount_MAD);
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    private  OnPaymentCLickListener lickListener;


    public void setLickListener(OnPaymentCLickListener lickListener) {
        this.lickListener = lickListener;
    }

    public interface OnPaymentCLickListener{
        void onPayment_click(String currency);
    }
}

