package com.vip.marrakech.admin.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.ItineraryDetailAdapter;
import com.vip.marrakech.admin.models.AdminItineraryModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.models.ItineryDetail.ItineriDetailModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminItineraryDetailActivity extends AppCompatActivity implements OnCallBackListener, Toolbar.OnMenuItemClickListener {

    private TextView tv_client, tv_group, tv_pax, tv_total_spend,tv_balance_to_pay,tv_deposit_paid;

    private RecyclerView rv_booking_detail;
    private List<GetItineraryDayDetail> list = new ArrayList<>();
    private ItineraryDetailAdapter adapter;
    private Communication communication;
    private Bundle bundle;
    private AdminItineraryModel model;
    private ItineriDetailModel detailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_booking_detail);
        communication = new Communication(this,this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolBar.inflateMenu(R.menu.admin_booking_edit);
        toolBar.setOnMenuItemClickListener(this);


    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("itinerary/%s", model.getEncryptedId()));
        communication.callGET(param);
    }

    private void initUI() {

        tv_client = findViewById(R.id.tv_client);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        tv_balance_to_pay = findViewById(R.id.tv_balance_to_pay);
        tv_deposit_paid = findViewById(R.id.tv_deposit_paid);
        rv_booking_detail = findViewById(R.id.rv_booking_detail);
        rv_booking_detail.setLayoutManager(new LinearLayoutManager(AdminItineraryDetailActivity.this));

        adapter = new ItineraryDetailAdapter(AdminItineraryDetailActivity.this, list);
        rv_booking_detail.setAdapter(adapter);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("itinerary/%s", model.getEncryptedId()))){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                detailModel = new Gson().fromJson(data.toString(), ItineriDetailModel.class);
                Log.e("ID::::",detailModel.getPax());
                tv_client.setText(getResources().getString(R.string.cline_name_s,detailModel.getClientName()));
                tv_group.setText(getResources().getString(R.string.group_type_s,detailModel.getGroup()));
                tv_pax.setText(getResources().getString(R.string.pax_s,detailModel.getPax()));
                tv_total_spend.setText(String.format("%s", detailModel.getTotalSpend()));
                tv_deposit_paid.setText(String.format("%s", detailModel.getHolding_deposit()));
                tv_balance_to_pay.setText(String.format("%s", detailModel.getBalance()));
                list.clear();
                list.addAll(detailModel.getGetItineraryDayDetail());
                adapter.notifyDataSetChanged();
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
        if (item.getItemId() == R.id.opt_edit){
            Bundle bundle = new Bundle();
            bundle.putSerializable("id",model.getEncryptedId());
            bundle.putSerializable("data",detailModel);
            GoTo.startWithExtra(AdminItineraryDetailActivity.this,EditIteniraryActivity.class,bundle);
        }else   if (item.getItemId() == R.id.opt_copy){
            new AlertDialog.Builder(AdminItineraryDetailActivity.this)
                    .setMessage("Are you sure want to copy this Itinerary?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data",model);
                            GoTo.startWithExtra(AdminItineraryDetailActivity.this, CopyIteniraryActivity.class,bundle);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return false;
    }


    @Override
    protected void onResume() {
        bundle = GoTo.getIntent(this);

        if (bundle != null) {
            initUI();
            if (bundle.getSerializable("model") != null) {
                model = (AdminItineraryModel) bundle.getSerializable("model");
                getList();


            }
        }
        super.onResume();
    }
}
