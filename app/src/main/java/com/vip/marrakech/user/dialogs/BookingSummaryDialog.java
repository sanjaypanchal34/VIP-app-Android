package com.vip.marrakech.user.dialogs;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
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
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookingSummaryDialog extends DialogFragment implements OnCallBackListener {


    private MaterialSpinner sp_currency;
    private String date, pax, table_name, table_no;
    private String total_mad;
    private Communication communication;
    private String total_converted_price, charge_amount_MAD;
    private TextView tv_charge_mad, tv_charge_home, tv_for_female,edt_total_MAD;
    private boolean isFemale;
    private OnPaymentCLickListener lickListener;
    private String deposit_percentage;
    private TextView tv_balance_to_pay;
    private String balance_AMD;
    private String table_display_name;
    private String venuType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.dialog_bookig_summary, container, false);
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
            param.put("deposit_percentage", deposit_percentage);
            communication.callPOST(param);
            tv_for_female.setVisibility(View.GONE);
        } else {
            tv_for_female.setVisibility(View.VISIBLE);
            if(table_name.equals("Entry Ticket (Females only)") || table_name.equals("Normal Entry") || table_name.equals("Entry tickets")){
                tv_charge_mad.setText("0");
                tv_charge_home.setText("0");
            }else{
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "convert/amount");
                param.put("total_price", total_mad.replaceAll("MAD",""));
                param.put("currency", "MAD");
                param.put("deposit_percentage", deposit_percentage);
                param.put("pax", pax);
                communication.callPOST(param);
            }
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
         edt_total_MAD = view.findViewById(R.id.edt_total_MAD);
        tv_charge_mad = view.findViewById(R.id.tv_charge_mad);
        tv_charge_home = view.findViewById(R.id.tv_charge_home);
        tv_for_female = view.findViewById(R.id.tv_for_female);
        tv_balance_to_pay = view.findViewById(R.id.tv_balance_to_pay);
        TextView tv_pax = view.findViewById(R.id.tv_pax);
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_table = view.findViewById(R.id.tv_table);
        TextView tv_table_no = view.findViewById(R.id.tv_table_no);
        sp_currency = view.findViewById(R.id.sp_currency);
        sp_currency.setItems("MAD", "GBP", "EUR", "USD");

        tv_charge_mad.setText("0");
        tv_charge_home.setText("0");
        tv_balance_to_pay.setText("0");


        tv_date.setText(String.format(getString(R.string.time_date_n_s), date));
        tv_pax.setText(String.format(getString(R.string.group_size_n_s), pax));
        tv_table.setText(String.format(getString(R.string.table_type_n_s), table_display_name));
        if(table_no!=null) {
            tv_table_no.setText(String.format(getString(R.string.no_of_table_n_s), table_no));
        }
        edt_total_MAD.setText(total_mad);


        sp_currency.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (!sp_currency.getText().toString().isEmpty() && !isFemale) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "convert/amount");
                    param.put("total_price", total_mad.replaceAll("MAD",""));
                    param.put("currency", sp_currency.getText().toString());
                    param.put("pax",pax);
                    param.put("deposit_percentage", deposit_percentage);
                    communication.callPOST(param);
                }else if(table_name.equals("Entry Ticket (Females only)") || table_name.equals("Normal Entry") || table_name.equals("Entry tickets")){
                    tv_charge_mad.setText("0");
                    tv_charge_home.setText("0");
                }else{
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "convert/amount");
                    param.put("total_price", total_mad.replaceAll("MAD",""));
                    param.put("currency", sp_currency.getText().toString());
                    param.put("deposit_percentage", deposit_percentage);
                    param.put("pax", pax);
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

        if(venuType.equalsIgnoreCase(VenueTypes.EXPERIENCES.toString())){
            tv_for_female.setVisibility(View.GONE);
            tv_table_no.setVisibility(View.GONE);
            tv_table.setText(table_display_name);
        }
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

    public void setData(String venuType,String date, String pax, String table_name, String table_display_name, String table_no, String total_mad, boolean isFemale, String deposit_percentage) {
       /* SimpleDateFormat main = new SimpleDateFormat("yyyy-MM-dd hh:mm",Locale.getDefault());
        SimpleDateFormat change = new SimpleDateFormat("EEE dd/MM hh:mm a",Locale.getDefault());
        try {
            Date mainDate = main.parse(date);
            this.date = change.format(mainDate);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = date;
        }*/
        this.venuType=venuType;
        this.date=date;
        this.pax = pax;
        this.table_name = table_name;
        this.table_no = table_no;
        this.total_mad = total_mad;
        this.isFemale = isFemale;
        this.deposit_percentage = deposit_percentage;
        this.table_display_name = table_display_name;


    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {

        if (tag.equals("convert/amount")) {
            Log.e(tag,jsonObject.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            total_converted_price = data.getString("charge_amount");
            charge_amount_MAD = data.getString("charge_amount_MAD");
            balance_AMD = data.getString("balance_MAD");
            edt_total_MAD.setText(data.getString("total_price"));
            tv_charge_home.setText(total_converted_price);
            tv_charge_mad.setText(charge_amount_MAD);
            tv_balance_to_pay.setText(balance_AMD);


        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    public void setLickListener(OnPaymentCLickListener lickListener) {
        this.lickListener = lickListener;
    }

    public interface OnPaymentCLickListener{
        void onPayment_click(String currency);
    }
}

