package com.vip.marrakech.vendor.dialog;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vip.marrakech.R;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TableDetailDialog extends DialogFragment implements OnCallBackListener {
    private TextView tv_c_name, tv_email, tv_mobile, tv_date, tv_source,tv_pax, tv_time, tv_venue, tv_concierge;
    private Communication communication;
    private TextView tv_cost;
    private TextView tv_deposit;
    private TextView tv_balance_to_pay,tv_no;
    private EditText edt_table;
    private Button btn_submit;
    private JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_qr_detail, container, false);
    }

    public void setData(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        tv_cost = (TextView) view.findViewById(R.id.tv_cost);
        tv_no = (TextView) view.findViewById(R.id.tv_no);
        tv_deposit = (TextView) view.findViewById(R.id.tv_deposit);
        tv_balance_to_pay = (TextView) view.findViewById(R.id.tv_balance_to_pay);
        tv_c_name = (TextView) view.findViewById(R.id.tv_c_name);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_pax = (TextView) view.findViewById(R.id.tv_pax);
        tv_source = (TextView) view.findViewById(R.id.tv_source);
        tv_concierge = (TextView) view.findViewById(R.id.tv_concierge);
        tv_venue = (TextView) view.findViewById(R.id.tv_venue);
        edt_table = (EditText) view.findViewById(R.id.edt_table);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        setValue();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_table.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Table No.", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "update/table");
                    param.put("type", jsonObject.optString("type"));
                    param.put("table_no", edt_table.getText().toString());
                    param.put("booking_id", jsonObject.optString("booking_id"));
                    communication.callPOST(param);
                }

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void setValue() {
        if (jsonObject != null) {
            tv_c_name.setText(Html.fromHtml(String.format("<b>Client Name</b> : %s", jsonObject.optJSONObject("data").optString("client_name"))));
            tv_email.setText(Html.fromHtml(String.format("<b>Email</b> : %s", jsonObject.optJSONObject("data").optString("client_email"))));
            tv_mobile.setText(Html.fromHtml(String.format("<b>Telephone</b> : %s", jsonObject.optJSONObject("data").optString("contact_number"))));
            tv_date.setText(Html.fromHtml(String.format("<b>Date</b> : %s", jsonObject.optJSONObject("data").optString("date"))));
            tv_time.setText(Html.fromHtml(String.format("<b>Time</b> : %s", jsonObject.optJSONObject("data").optString("time"))));
            tv_pax.setText(Html.fromHtml(String.format("<b>Group Size</b> : %s", jsonObject.optJSONObject("data").optString("pax"))));
            tv_source.setText(Html.fromHtml(String.format("<b>Source</b> : %s", jsonObject.optJSONObject("data").optString("booking_from"))));

            tv_concierge.setText(Html.fromHtml(String.format("<b>Concierge</b> : %s", jsonObject.optJSONObject("data").optString("conceirge_name"))));
            tv_venue.setText(Html.fromHtml(String.format("<b>Venue</b> : %s", jsonObject.optJSONObject("data").optString("venue_name"))));

            if(SessionManager.getVenue_Type().equals(VenueTypes.DAY_PARTIES.toString())){
                tv_no.setText("Enter Bed No.");
            }else{
                tv_no.setText(R.string.enter_table_no);
            }

            if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
                tv_cost.setText(Html.fromHtml(String.format("<b>Total Cost</b> : %s", jsonObject.optJSONObject("data").optString("total_spend"))));
                tv_deposit.setText(Html.fromHtml(String.format("<b>Deposit</b> : %s", jsonObject.optJSONObject("data").optString("deposite"))));
                tv_balance_to_pay.setText(Html.fromHtml(String.format("<b>Balance to pay</b> : %s", jsonObject.optJSONObject("data").optString("balance"))));
            }else if ( SessionManager.getDepositOption().equalsIgnoreCase("fixed")) {
                tv_cost.setText(Html.fromHtml(String.format("<b>Total Cost</b> : %s", "N/A")));
                tv_deposit.setText(Html.fromHtml(String.format("<b>Deposit</b> : %s", jsonObject.optJSONObject("data").optString("deposite").isEmpty()?"N/A":jsonObject.optJSONObject("data").optString("deposite"))));
                tv_balance_to_pay.setText(Html.fromHtml(String.format("<b>Balance to pay</b> : %s", "N/A")));
            }else {
                tv_cost.setText(Html.fromHtml(String.format("<b>Total Cost</b> : %s", "N/A")));
                tv_deposit.setText(Html.fromHtml(String.format("<b>Deposit</b> : %s", "N/A")));
                tv_balance_to_pay.setText(Html.fromHtml(String.format("<b>Balance to pay</b> : %s", "N/A")));
            }

            if( jsonObject.optJSONObject("data").optString("booking_from").equals("Manual Booking")){
                tv_cost.setText(Html.fromHtml(String.format("<b>Total Cost</b> : %s", "N/A")));
                tv_deposit.setText(Html.fromHtml(String.format("<b>Deposit</b> : %s", "N/A")));
                tv_balance_to_pay.setText(Html.fromHtml(String.format("<b>Balance to pay</b> : %s", "N/A")));
                tv_concierge.setVisibility(View.VISIBLE);
            }else {
                tv_concierge.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {
        if (tag.equalsIgnoreCase("update/table")) {
            Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            if (listener != null) {

                listener.onTableEdit();
                dismiss();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    private OnTableEditListener listener;

    public void setListener(OnTableEditListener listener) {
        this.listener = listener;
    }

    public interface OnTableEditListener {
        void onTableEdit();
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
