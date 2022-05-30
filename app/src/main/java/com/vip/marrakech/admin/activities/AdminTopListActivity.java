package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminTopListAdapter;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.retrofit.models.PART;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminTopListActivity extends AppCompatActivity implements OnCallBackListener {

    private RecyclerView rv_top_list;
    private List<AdminTopModel> list = new ArrayList<>();
    private Bundle bundle;
    private Communication communication;
    private AdminTopListAdapter adapter;
    private ImageView tv_add;
    TextView tv_type_name, tv_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_list);
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
        tv_type_name = findViewById(R.id.tv_type_name);
        tv_type_name.setText(bundle.getString("title"));
        tv_add = findViewById(R.id.tv_add);
        rv_top_list = findViewById(R.id.rv_top_list);
        rv_top_list.setLayoutManager(new LinearLayoutManager(AdminTopListActivity.this));
        adapter = new AdminTopListAdapter(AdminTopListActivity.this, list);
        adapter.setListener(new AdminTopListAdapter.OnTopListener() {
            @Override
            public void onDelete(final AdminTopModel model) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminTopListActivity.this);
                builder1.setMessage("Are you sure want to delete this recommendation?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("action", "recommandation/delete/" + model.getEncrypted_id());
                                communication.callDelete(param);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

            @Override
            public void onEdit(AdminTopModel model) {
                Bundle b = new Bundle();
                b.putSerializable("data", (Serializable) model);
                if (bundle.getString("type").equals("7")) {
                    GoTo.startWithExtra(AdminTopListActivity.this, AdminAttractionEditActivity.class, b);
                } else {
                    GoTo.startWithExtra(AdminTopListActivity.this, AdminTopEditActivity.class, b);
                }


            }
        });
        rv_top_list.setAdapter(adapter);


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle != null)
                    if (bundle.getString("type").equals("7")) {
                        startActivity(new Intent(AdminTopListActivity.this, AdminAttractionAddActivity.class).putExtra("type", bundle.getString("type")).putExtra("title", bundle.getString("title")));
                    } else {
                        startActivity(new Intent(AdminTopListActivity.this, AdminTopAddActivity.class).putExtra("type", bundle.getString("type")).putExtra("title", bundle.getString("title")));

                    }
            }
        });
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
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray array = data.getJSONArray("recommandations");
                for (int i = 0; i < array.length(); i++) {
                    AdminTopModel model = new Gson().fromJson(array.getJSONObject(i).toString(), AdminTopModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
               /* if (adapter.getItemCount() == 0){
                    tv_no_data.setVisibility(View.VISIBLE);
                }else {
                    tv_no_data.setVisibility(View.GONE);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(AdminTopListActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            getList();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
