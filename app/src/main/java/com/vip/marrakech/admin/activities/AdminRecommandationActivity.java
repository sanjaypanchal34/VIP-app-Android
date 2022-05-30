package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminRecommendationAdapter;
import com.vip.marrakech.admin.models.AdminRecommendation;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.TopDetailUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminRecommandationActivity extends AppCompatActivity implements OnCallBackListener {

    private RecyclerView rv_recommendation;
    private List<AdminRecommendation> list = new ArrayList<>();
    private AdminRecommendationAdapter adapter;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recommandation);
        communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rv_recommendation = findViewById(R.id.rv_recommendation);
        rv_recommendation.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminRecommendationAdapter(this, list);
        adapter.setListener(new AdminRecommendationAdapter.OnRecommendationListener() {
            @Override
            public void onRecommendationClick(AdminRecommendation model) {
                Bundle bundle = new Bundle();
                bundle.putString("title",model.getType_name());
                bundle.putString("type",model.getId());
                GoTo.startWithExtra(AdminRecommandationActivity.this,AdminTopListActivity.class,bundle);
            }
        });
        rv_recommendation.setAdapter(adapter);

        getList();

    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "recommandation/type/list");
        communication.callGET(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("recommandation/type/list")){
            try {
                JSONObject data  = jsonObject.getJSONObject("data");
                JSONArray recommandation_types = data.getJSONArray("recommandation_types");
                list.clear();
                for (int i = 0;i<recommandation_types.length();i++){
                    AdminRecommendation recommendation = new Gson().fromJson(recommandation_types.getJSONObject(i).toString(),AdminRecommendation.class);
                   if (!recommendation.getId().equals("8")){
                       list.add(recommendation);
                   }
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
