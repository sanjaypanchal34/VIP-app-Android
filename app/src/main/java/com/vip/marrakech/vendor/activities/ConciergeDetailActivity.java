package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.adapters.ConciergeViewAdapter;
import com.vip.marrakech.vendor.models.ConciergeClientModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConciergeDetailActivity extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_total_commission, tv_concierge;
    private RecyclerView rv_concierge_detail;
    private List<ConciergeClientModel> list = new ArrayList<>();
    private ConciergeViewAdapter adapter;
    private String id;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concierge_detail);
        communication = new Communication(this, this);
        Bundle bundle = GoTo.getIntent(this);
        id = bundle.getString("id");

        initUI();


        getDetail();
    }

    private void getDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "vendor/view/concierge");
        param.put("concierge_id", id);
        param.put("venue_type", SessionManager.getVenue_Type());
        communication.callPOST(param);
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_concierge = findViewById(R.id.tv_concierge);
        tv_total_commission = findViewById(R.id.tv_total_commission);
        rv_concierge_detail = findViewById(R.id.rv_concierge_detail);
        rv_concierge_detail.setLayoutManager(new LinearLayoutManager(ConciergeDetailActivity.this));

        adapter = new ConciergeViewAdapter(ConciergeDetailActivity.this, list);
        rv_concierge_detail.setAdapter(adapter);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        try {
            if (tag.equals("vendor/view/concierge")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject concierge_detail = data.getJSONObject("concierge_detail");
                tv_concierge.setText(concierge_detail.getString("concierge_name"));
                tv_total_commission.setText(String.format("%s", concierge_detail.getString("total_comission")));
                JSONArray client_details = concierge_detail.getJSONArray("client_details");
                list.clear();
                for (int i = 0; i < client_details.length(); i++) {
                    ConciergeClientModel model = new Gson().fromJson(client_details.getJSONObject(i).toString(), ConciergeClientModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
