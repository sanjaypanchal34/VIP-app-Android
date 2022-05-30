package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.UserTopAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRecommendationSearchActivity extends AppCompatActivity implements TextWatcher, OnCallBackListener {

    private EditText edt_search;
    private RecyclerView rv_top_list;
    private List<AdminTopModel> list = new ArrayList<>();
    private UserTopAdapter adapter;
    private Communication communication;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recommendation_search);
        communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edt_search = findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(this);
        rv_top_list = findViewById(R.id.rv_top_list);
        rv_top_list.setLayoutManager(new LinearLayoutManager(UserRecommendationSearchActivity.this));
        adapter = new UserTopAdapter(UserRecommendationSearchActivity.this, list);
        adapter.setListener(new UserTopAdapter.OnTopListener() {

            @Override
            public void onRecommendationClick(AdminTopModel model) {
                if (model.getComing_soon().equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", model.getEncrypted_id());
                    bundle.putString("type", "1");
                    GoTo.startWithExtra(UserRecommendationSearchActivity.this, TopDetailUserActivity.class, bundle);

                }
            }
        });
        rv_top_list.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        communication.cancel("search/recommandation/" + query);
        query = s.toString();
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "search/recommandation/" + query);
        communication.callGETRetry(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.contains("search/recommandation/")) {
            list.clear();
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray array = data.getJSONArray("recommandations");
                for (int i = 0; i < array.length(); i++) {
                    AdminTopModel model = new Gson().fromJson(array.getJSONObject(i).toString(), AdminTopModel.class);
                    list.add(model);
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
