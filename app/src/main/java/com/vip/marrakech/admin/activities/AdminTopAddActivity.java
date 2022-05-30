package com.vip.marrakech.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.VideoPlayerActivity;
import com.vip.marrakech.dialogs.SearchDialog;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.retrofit.models.PART;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dk.nodes.filepicker.FilePickerActivity;
import dk.nodes.filepicker.FilePickerConstants;
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper;

public class AdminTopAddActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private TextView edt_location;
    private Button btn_save;
    private EditText edt_title, edt_desc;
    private EditText edt_label1, edt_label_1_value;
    private EditText edt_label2, edt_label_2_value;
    private EditText edt_label3, edt_label_3_value;
    private EditText edt_label4, edt_label_4_value;
    private Communication communication;
    private Button btn_menu, btn_feature, btn_galley;
    private SimpleDraweeView iv_menu, iv_feature;
    private ImageView iv_menu_clear, iv_feature_clear;
    private SliderView imageSlider;
    private File menuFile, featutreFile;
    private ArrayList<String> imageList = new ArrayList<>();
    private SliderViewAdapter<BannerViewHolder> bannerAdapter;
    private CheckBox cb_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_add);
        communication = new Communication(this, this);
        initUI();


        edt_location.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
        iv_menu_clear.setOnClickListener(this);
        btn_feature.setOnClickListener(this);
        iv_feature_clear.setOnClickListener(this);
        btn_galley.setOnClickListener(this);

        bannerAdapter = new SliderViewAdapter<BannerViewHolder>() {
            @Override
            public int getCount() {
                return imageList.size();
            }

            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent) {
                return new BannerViewHolder(LayoutInflater.from(AdminTopAddActivity.this).inflate(R.layout.banner_item, parent, false));
            }

            @Override
            public void onBindViewHolder(final BannerViewHolder holder, final int position) {
                File file = new File(imageList.get(position));
                if (!file.getName().endsWith(".jpg")&&!file.getName().endsWith(".png")){
                    holder.iv_play.setVisibility(View.VISIBLE);
                }else {
                    holder.iv_play.setVisibility(View.GONE);
                }
                holder.iv_image.setImageURI(Uri.fromFile(file));
                holder.iv_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminTopAddActivity.this, VideoPlayerActivity.class);
                        intent.putExtra("link",file.getAbsolutePath());
                        startActivity(intent);
                    }
                });
                holder.iv_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(AdminTopAddActivity.this)
                                .setMessage("Are you sure you want to delete this Image?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        imageList.remove(position);
                                        bannerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                });
            }
        };
        imageSlider.setSliderAdapter(bannerAdapter);
    }


    private class BannerViewHolder extends SliderViewAdapter.ViewHolder {
        private SimpleDraweeView iv_image;
        private ImageView iv_clear,iv_play;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_clear = itemView.findViewById(R.id.iv_clear);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }

    private void initUI() {

        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_menu = findViewById(R.id.btn_menu);
        edt_label1 = findViewById(R.id.edt_label1);
        edt_label_1_value = findViewById(R.id.edt_label_1_value);
        edt_label2 = findViewById(R.id.edt_label2);
        edt_label_2_value = findViewById(R.id.edt_label_2_value);
        edt_label3 = findViewById(R.id.edt_label3);
        edt_label_3_value = findViewById(R.id.edt_label_3_value);
        edt_label4 = findViewById(R.id.edt_label4);
        edt_label_4_value = findViewById(R.id.edt_label_4_value);
        cb_check = findViewById(R.id.cb_check);
        btn_galley = findViewById(R.id.btn_galley);
        iv_menu = findViewById(R.id.iv_menu);
        iv_menu_clear = findViewById(R.id.iv_menu_clear);
        btn_feature = findViewById(R.id.btn_feature);
        iv_feature = findViewById(R.id.iv_feature);
        iv_feature_clear = findViewById(R.id.iv_feature_clear);
        imageSlider = findViewById(R.id.imageSlider);

        btn_save = findViewById(R.id.btn_save);
        edt_location = findViewById(R.id.edt_location);
        edt_title = findViewById(R.id.edt_title);
        edt_desc = findViewById(R.id.edt_desc);
    }


    private void pickImage(int id, boolean b) {
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
                .setMaxSize(Integer.MAX_VALUE)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
//                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(id)                //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (requestCode == 1) {
                if (images != null && images.size() > 0) {
                    iv_feature_clear.setVisibility(View.VISIBLE);
                    featutreFile = new File(images.get(0).getPath());
                }
            }else
            if (requestCode == 2) {
                File gallery = FilePickerUriHelper.getFile(AdminTopAddActivity.this, data);
                if (gallery!=null){
                    imageList.add(gallery.getPath());
                    bannerAdapter.notifyDataSetChanged();
                    Log.e("MENu:::", gallery.getAbsolutePath());
                }
             /*   if (images != null && images.size() > 0) {
                    for (Image image :
                            images) {
                        imageList.add(image.getPath());
                    }
                    bannerAdapter.notifyDataSetChanged();
                }*/
            }else if (requestCode == 3){
                menuFile = FilePickerUriHelper.getFile(AdminTopAddActivity.this,data);
                Log.e("MENu:::",menuFile.getAbsolutePath());
                iv_menu_clear.setVisibility(View.VISIBLE);
            }
        }
        // You MUST have this line to be here
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_menu: {
                Intent intent = new Intent(AdminTopAddActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerConstants.CHOOSER_TEXT, "Select Menu");
                intent.putExtra(FilePickerConstants.FILE, true);
                intent.putExtra(FilePickerConstants.MULTIPLE_TYPES, new String[]{FilePickerConstants.MIME_IMAGE, FilePickerConstants.MIME_PDF});
                startActivityForResult(intent, 3);
            }
            break;
            case R.id.iv_menu_clear: {
                new AlertDialog.Builder(AdminTopAddActivity.this)
                        .setMessage("Are you sure you want to delete Menu?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                iv_menu_clear.setVisibility(View.GONE);
                                iv_menu.setImageURI("");
                                menuFile = null;
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
            break;
            case R.id.btn_feature: {
                pickImage(1, false);
            }
            break;
            case R.id.btn_galley: {
//                pickImage(2, false);
                Intent intent = new Intent(AdminTopAddActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerConstants.CHOOSER_TEXT, "Select File");
                intent.putExtra(FilePickerConstants.FILE, true);
                intent.putExtra(FilePickerConstants.MULTIPLE_TYPES, new String[]{FilePickerConstants.MIME_IMAGE, FilePickerConstants.MIME_VIDEO});
                startActivityForResult(intent, 2);
            }
            break;
            case R.id.iv_feature_clear: {
                new AlertDialog.Builder(AdminTopAddActivity.this)
                        .setMessage("Are you sure you want to delete Feature Image?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                iv_feature_clear.setVisibility(View.GONE);
                                iv_feature.setImageURI("");
                                featutreFile = null;
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
            break;
            case R.id.edt_location: {
//                openAddressSearch();

                SearchDialog dialog = new SearchDialog();
                dialog.setVenue_type(getIntent().getStringExtra("title"));
                dialog.setListener(new SearchDialog.OnSearchListener() {
                    @Override
                    public void OnVenueSelected(String id, String title, String venueType) {
                        Log.e(title, id);

                        edt_location.setText(title);
                        edt_location.setTag(id);
                    }
                });
                dialog.show(getSupportFragmentManager(), "SEARCH");

            }
            break;

            case R.id.btn_save: {


                if (Validator.isEmpty(edt_title)) {
                    Validator.setError(edt_title, "Enter Title");
                } else if (Validator.isEmpty(edt_desc)) {
                    Validator.setError(edt_desc, "Enter Description");
                } else if (Validator.isEmpty(edt_location)) {
                    Toast.makeText(this, "Select Venue", Toast.LENGTH_SHORT).show();
                }/* else if (menuFile == null) {
                    Toast.makeText(this, "Select Menu", Toast.LENGTH_SHORT).show();
                } */else if (featutreFile == null) {
                    Toast.makeText(this, "Select Feature Image", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "recommandation/update");
                    param.put("title", Validator.getText(edt_title));
                    param.put("description", Validator.getText(edt_desc));
                    param.put("label1", Validator.getText(edt_label1));
                    param.put("busiest_days", Validator.getText(edt_label_1_value));
                    param.put("label2", Validator.getText(edt_label2));
                    param.put("music_type", Validator.getText(edt_label_2_value));
                    param.put("label3", Validator.getText(edt_label3));
                    param.put("opening_hours", Validator.getText(edt_label_3_value));
                    param.put("label4", Validator.getText(edt_label4));
                    param.put("dress_code", Validator.getText(edt_label_4_value));
                    param.put("coming_soon",cb_check.isChecked() ? "1" : "0");
                    param.put("type_id", getIntent().getStringExtra("type"));
                    param.put("venue_id", edt_location.getTag() != null ? edt_location.getTag().toString() : "");
                    List<PART> partList = new ArrayList<>();
                    List<PART> partListVideo = new ArrayList<>();
                    int img = 0;
                    int vid = 0;
                    for (int i = 0; i < imageList.size(); i++) {
                        File file = new File(imageList.get(i));

                        if (!file.getName().endsWith(".jpg")&&!file.getName().endsWith(".png")){
                            partListVideo.add(new PART("video["+vid+"]",file));
                            vid++;
                        }else {
                            partList.add(new PART("image[" + img + "]", file));
                            img++;
                        }
                    }
                    if (menuFile!=null){
                        communication.callPOST(param,new PART("feature_image",featutreFile),new PART("menu_image",menuFile), partList,partListVideo);
                    }else {
                        communication.callPOST(param,new PART("feature_image",featutreFile), partList,partListVideo);
                    }
                }
            }
            break;
        }
    }



    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("recommandation/update")) {
            try {
                Toast.makeText(AdminTopAddActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
