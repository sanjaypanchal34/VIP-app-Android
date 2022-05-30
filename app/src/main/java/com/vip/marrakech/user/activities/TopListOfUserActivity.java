package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminTopAddActivity;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.UserTopAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopListOfUserActivity extends AppCompatActivity implements OnCallBackListener {

    private RecyclerView rv_top_list;
    private List<AdminTopModel> list = new ArrayList<>();
    private Bundle bundle;
    private Communication communication;
    private UserTopAdapter adapter;
    TextView tv_type_name,tv_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list_of_user);
        communication = new Communication(this, this);
        bundle = GoTo.getIntent(this);

        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_no_data = findViewById(R.id.tv_no_data);
     /*   tv_type_name = findViewById(R.id.tv_type_name);
        tv_type_name.setText(bundle.getString("title"));*/
        rv_top_list = findViewById(R.id.rv_top_list);
        rv_top_list.setLayoutManager(new LinearLayoutManager(TopListOfUserActivity.this));
        adapter = new UserTopAdapter(TopListOfUserActivity.this,list);
        adapter.setListener(new UserTopAdapter.OnTopListener() {

            @Override
            public void onRecommendationClick(AdminTopModel model) {
               if(model.getComing_soon().equals("0")){
                   Bundle b = new Bundle();
                   b.putString("id", model.getEncrypted_id());
                   b.putString("type", bundle.getString("type"));
                   GoTo.startWithExtra(TopListOfUserActivity.this, TopDetailUserActivity.class, b);
               }
            }
        });
        rv_top_list.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        getList();
        super.onResume();
    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "recommandation/list/" + bundle.getString("type"));

        communication.callGET(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("recommandation/list/" + bundle.getString("type"))) {
            list.clear();
            try {
                JSONObject  data = jsonObject.getJSONObject("data");
                JSONArray array = data.getJSONArray("recommandations");
                for (int i = 0; i < array.length(); i++) {
                    AdminTopModel model = new Gson().fromJson(array.getJSONObject(i).toString(), AdminTopModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
             /*   if (adapter.getItemCount() == 0){
                    tv_no_data.setVisibility(View.VISIBLE);
                }else {
                    tv_no_data.setVisibility(View.GONE);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            getList();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
