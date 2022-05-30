package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.retrofit.models.PART;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EditProfileAdminActivity extends AppCompatActivity implements OnCallBackListener {

    private EditText edt_user_name, edt_email, edt_mobile;
    private Communication communication;
    private SimpleDraweeView iv_profile;
    private ImageView iv_upload;
    private String uploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin);
        communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.opt_done) {
                    if (Validator.isEmpty(edt_user_name)) {
                        Validator.setError(edt_user_name, "Enter Username");
                    } else if (Validator.isEmpty(edt_email)) {
                        Validator.setError(edt_email, "Enter Email");
                    } else if (Validator.isNotEmail(edt_email)) {
                        Validator.setError(edt_email, "Enter Valid Email");
                    } else if (!Validator.isEmpty(edt_mobile) && Validator.isNotMinLength(edt_mobile, 11)) {
                        Validator.setError(edt_mobile, "Enter Valid Contact Number");
                    } /*else if (Validator.isNotMinLength(edt_mobile, 11)) {
                        Validator.setError(edt_mobile, "Enter Valid Contact Number");
                    } */else {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "user/update");
                        param.put("id", SessionManager.getEncryptedID());
                        param.put("name", Validator.getText(edt_user_name));
                        param.put("email",Validator.getText(edt_email));
                        param.put("contact_number", Validator.getText(edt_mobile));
                        if (uploadFile != null) {
                            communication.callPOST(param, new PART("image", new File(uploadFile)));
                        }else {
                            communication.callPOST(param);

                        }
                    }
                }
                return false;
            }
        });
        initUI();

        getDetail();


        iv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
    }


    private void pickImage() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(false)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setMaxSize(10)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
//                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(1100)                //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1100 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            uploadFile = images.get(0).getPath();
            iv_profile.setImageURI(Uri.fromFile(new File(uploadFile)));
        }
        // You MUST have this line to be here
    }

    private void getDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "user/detail/" + SessionManager.getEncryptedID());
        communication.callGET(param);
    }

    private void initUI() {
        iv_profile = findViewById(R.id.iv_profile);
        iv_upload = findViewById(R.id.iv_upload);
        edt_user_name = findViewById(R.id.edt_user_name);

        edt_email = findViewById(R.id.edt_email);

        edt_mobile = findViewById(R.id.edt_mobile);


    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("user/detail/" + SessionManager.getEncryptedID())) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                edt_user_name.setText(data.getString("name"));
                edt_email.setText(data.getString("email"));
                edt_mobile.setText(data.getString("contact_number"));
                System.out.println("PROFILE::::"+String.format("%s/users/%s/%s", ApiClient.HOST, data.getString("id"), data.getString("image")));
                iv_profile.setImageURI(String.format("%s/users/%s/%s", ApiClient.HOST,data.getString("id"), data.getString("image")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (tag.equals( "user/update")){
            SessionManager.setEmail(Validator.getText(edt_email));
            SessionManager.setName(Validator.getText(edt_user_name));
            onBackPressed();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
