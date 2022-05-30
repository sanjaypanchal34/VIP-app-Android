package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class OtherBookingDetailActivity extends AppCompatActivity implements OnCallBackListener, Toolbar.OnMenuItemClickListener {

    private TextView tv_f_name, tv_l_name, tv_email, tv_mobile, tv_pax, tv_date, tv_time, tv_from,tv_concierge,/*tv_bottle_spend,*/ tv_total_spend, tv_comment;
    private Communication communication;
    private String id;
    private RadioButton rd_non_arrived, rd_arrived;
    private Button btn_cancel;
    private LinearLayout ln_total_spend;
    private TextView tv_table_no;
    private TextView tv_group_type,tv_table,tv_no_of_tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_booking_detail);
        communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
        initUI();
        btn_cancel.setEnabled(false);
        Bundle bundle = GoTo.getIntent(this);
        id = bundle.getString("id");
      /*  HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("vendor/other/booking/detail/%s", id));
        communication.callGET(param);*/


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OtherBookingDetailActivity.this)
                        .setMessage("Are you sure want to cancel the booking?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("action", String.format("vendor/cancel/other/booking/%s", id));
                                communication.callGET(param);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    private void initUI() {
        ln_total_spend = findViewById(R.id.ln_total_spend);
        tv_f_name = findViewById(R.id.tv_f_name);
        tv_l_name = findViewById(R.id.tv_l_name);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_pax = findViewById(R.id.tv_pax);
        tv_group_type = findViewById(R.id.tv_group_type);
        tv_no_of_tables = findViewById(R.id.tv_no_of_tables);
        tv_table = findViewById(R.id.tv_table);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_concierge = findViewById(R.id.tv_concierge);
        tv_from = findViewById(R.id.tv_from);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        /*tv_bottle_spend = findViewById(R.id.tv_bottle_spend);*/
        tv_comment = findViewById(R.id.tv_comment);
        rd_arrived = findViewById(R.id.rd_arrived);
        rd_non_arrived = findViewById(R.id.rd_non_arrived);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_table_no = findViewById(R.id.tv_table_no);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("vendor/other/booking/detail/%s", id))) {
            try {

                btn_cancel.setEnabled(true);
                JSONObject data = jsonObject.getJSONObject("data");
                tv_f_name.setText(data.getString("first_name"));
                tv_l_name.setText(data.getString("last_name"));
                tv_email.setText(data.getString("email"));
                tv_mobile.setText(data.getString("phone_no"));
                tv_pax.setText(data.getString("pax"));
                tv_group_type.setText(data.getString("group_type"));
                tv_no_of_tables.setText(data.getString("table_reserved"));
                if (SessionManager.getVenue_Type().equalsIgnoreCase(VenueTypes.NIGHTCLUB.toString()) || SessionManager.getVenue_Type().equalsIgnoreCase(VenueTypes.DAY_PARTIES.toString())) {
                    tv_table.setText(data.getString("table_name"));
                }else{
                    tv_table.setText("N/A");
                }
                SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                tv_date.setText(showFormat.format(finalFormate.parse(data.getString("booking_date"))));
                tv_time.setText(data.getString("booking_time"));
                tv_concierge.setText(data.getString("concierge_name"));
                tv_from.setText(data.getString("booking_from"));
                tv_concierge.setTag(data.getString("concierge_id"));
                tv_total_spend.setText(data.getString("total_spend").isEmpty()?"0": String.format("%s", data.getString("total_spend")));
               // tv_bottle_spend.setText(data.getString("bottle_spend").isEmpty()?"0": String.format("%s", data.getString("bottle_spend")));
                tv_table_no.setText(data.getString("table_no"));
                tv_comment.setText(data.getString("description"));
               if (data.getString("status").equalsIgnoreCase("NA")) {
                    ln_total_spend.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.VISIBLE);
                    rd_non_arrived.setEnabled(true);
                    rd_non_arrived.setChecked(true);
                    rd_arrived.setChecked(false);
                    rd_arrived.setEnabled(false);
                }else  if (data.getString("status").equalsIgnoreCase("A")){
                   ln_total_spend.setVisibility(View.VISIBLE);
                   btn_cancel.setVisibility(View.INVISIBLE);
                   rd_arrived.setChecked(true);
                   rd_arrived.setEnabled(true);
                   rd_non_arrived.setChecked(false);
                   rd_non_arrived.setEnabled(false);
               }else  if (data.getString("status").equalsIgnoreCase("CNF")){
                   btn_cancel.setVisibility(View.VISIBLE);
               }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        } else if (tag.equals(String.format("vendor/cancel/other/booking/%s", id))) {

            try {
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(OtherBookingDetailActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals(String.format("vendor/delete/other/booking/%s", id))) {

            try {
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(OtherBookingDetailActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_edit) {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            GoTo.startWithExtra(OtherBookingDetailActivity.this, EditOtherBookingActivity.class, bundle);
        } else if (item.getItemId() == R.id.opt_delete) {
            new AlertDialog.Builder(OtherBookingDetailActivity.this)
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("action", String.format("vendor/delete/other/booking/%s", id));
                            communication.callDelete(param);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }
        return false;
    }


    @Override
    protected void onResume() {
        if (id != null) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", String.format("vendor/other/booking/detail/%s", id));
            communication.callGET(param);
        }
        super.onResume();
    }
}
