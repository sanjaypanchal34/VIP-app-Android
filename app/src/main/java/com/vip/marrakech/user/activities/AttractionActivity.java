package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminRecommendationAdapter;
import com.vip.marrakech.admin.models.AdminRecommendation;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.AttractionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttractionActivity extends AppCompatActivity implements OnCallBackListener {

    private Communication communication;
    private RecyclerView rv_attraction;
    private AttractionAdapter adapter;
    private List<AdminRecommendation> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);
        communication = new Communication(this, this);
        rv_attraction = findViewById(R.id.rv_attraction);
        rv_attraction.setLayoutManager(new LinearLayoutManager(this));
       /* adapter = new AttractionAdapter(this, list);
        adapter.setListener(new AttractionAdapter.OnRecommendationListener() {
            @Override
            public void onRecommendationClick(AdminRecommendation model) {

            }
        });
        rv_attraction.setAdapter(adapter);*/
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {

    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
