package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminPromotionAdapter;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.UserPromotionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminPromotionLIst extends AppCompatActivity implements OnCallBackListener, AdminPromotionAdapter.OnPromotionListener {

    private RecyclerView rv_admin_promotion;
    private AdminPromotionAdapter adapter;
    private List<UserPromotionModel> list = new ArrayList<>();
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_promotion_list);
        communication = new Communication(this,this);

        initUI();

    }

    private void getPromotions() {
        HashMap<String,String> param = new HashMap<>();
        param.put("action","promotion/list");
        communication.callGET(param);


    }
    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.opt_add){
                    GoTo.start(AdminPromotionLIst.this,AddPromotionActivity.class);
                }
                return false;
            }
        });
        rv_admin_promotion = findViewById(R.id.rv_admin_promotion);
        rv_admin_promotion.setLayoutManager(new LinearLayoutManager(AdminPromotionLIst.this));
        adapter = new AdminPromotionAdapter(AdminPromotionLIst.this,list);
        adapter.setListener(this);
        rv_admin_promotion.setAdapter(adapter);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("promotion/list")){

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray promotions = data.getJSONArray("promotions");
                list.clear();
                for (int i=0;i<promotions.length();i++){
                    UserPromotionModel model = new Gson().fromJson(promotions.getJSONObject(i).toString(),UserPromotionModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                Log.e("Promotion Size:::", String.valueOf(list.size()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (tag.contains("promotion/delete/")){
            Toast.makeText(AdminPromotionLIst.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            getPromotions();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onPromotionClick(UserPromotionModel model) {

    }

    @Override
    public void onPromotionEdit(UserPromotionModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",model);
        GoTo.startWithExtra(AdminPromotionLIst.this,EditPromotionActivity.class,bundle);
    }

    @Override
    public void onPromotionDelete(final UserPromotionModel model) {

        new AlertDialog.Builder(AdminPromotionLIst.this)
                .setMessage("Are you sure you want to delete this Promotion?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String,String> param = new HashMap<>();
                        param.put("action","promotion/delete/"+model.getEncrypted_id());
                        communication.callDelete(param);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();

    }


    @Override
    protected void onResume() {
        getPromotions();
        super.onResume();
    }
}
