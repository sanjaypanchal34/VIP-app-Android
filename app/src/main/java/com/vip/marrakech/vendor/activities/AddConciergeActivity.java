package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.models.ConciergeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddConciergeActivity extends AppCompatActivity implements OnCallBackListener {

    private EditText edt_name, edt_email, edt_commission;
    private Button btn_save;
    private Communication communication;
    private ConciergeModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concierge);
        communication = new Communication(this, this);

        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        initUI();

        Bundle bundle = GoTo.getIntent(this);
        if (bundle!=null) {
            model = (ConciergeModel) bundle.getSerializable("data");

            if (model != null) {
                toolbar_title.setText(getResources().getString(R.string.edit_concierge));
                edt_name.setText(model.getName());
                edt_email.setText(model.getEmail());
                edt_email.setEnabled(false);
                edt_commission.setText(model.getComissionPercentage());
            }
        }

    }

    private void initUI() {
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_commission = findViewById(R.id.edt_commission);
        btn_save = findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.isEmpty(edt_name)) {
                    Validator.setError(edt_name, "Enter Concierge Name");
                } else if (Validator.isNotEmail(edt_email)) {
                    Validator.setError(edt_email, "Enter Valid Email");
                } else if (Validator.isEmpty(edt_commission)) {
                    Validator.setError(edt_commission, "Enter Commission");
                }else if (Integer.parseInt(Validator.getText(edt_commission))>100) {
                    Validator.setError(edt_commission, "Commission should not be Greater than 100");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    if (model == null) {
                        param.put("action", "concierge/add");
                    }else {
                        param.put("action", "concierge/update");
                        param.put("concierge_id", model.getEncryptedConciergeId());
                    }
                    param.put("name", Validator.getText(edt_name));
                    param.put("email", Validator.getText(edt_email));
                    param.put("comission_percentage", Validator.getText(edt_commission));
                    communication.callPOST(param);
                }
            }
        });
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("concierge/add")) {
            try {
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(AddConciergeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (tag.equalsIgnoreCase("concierge/update")) {
            try {
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(AddConciergeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
}
