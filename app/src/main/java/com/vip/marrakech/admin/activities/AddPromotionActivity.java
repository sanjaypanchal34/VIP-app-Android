package com.vip.marrakech.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moeidbannerlibrary.banner.BannerLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.CustomBannerAdapter;
import com.vip.marrakech.dialogs.SearchDialog;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.retrofit.models.PART;


import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddPromotionActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {
    private TextView edt_from, edt_to;
    private EditText edt_venue;
    private Button btn_save;
    private Button btn_galley;
    private Button btn_feature;
    private EditText edt_title, edt_desc;
    private Communication communication;
    private SliderView banner;
    private SliderViewAdapter bannerAdapter;
    private List<String> imageList = new ArrayList<>();
    private SimpleDraweeView iv_feature;
    private ImageView iv_feature_clear;
    private File featureFile;
    private EditText edt_label1, edt_label_1_value;
    private EditText edt_label2, edt_label_2_value;
    private EditText edt_label3, edt_label_3_value;
    private EditText edt_label4, edt_label_4_value;
    private CheckBox cb_venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion);
        communication = new Communication(this, this);


        iniTUI();

        edt_to.setOnClickListener(this);
        edt_from.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_venue.setOnClickListener(this);
        btn_galley.setOnClickListener(this);
        btn_feature.setOnClickListener(this);
        iv_feature_clear.setOnClickListener(this);


        cb_venue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edt_venue.setText("");
                edt_venue.setFocusable(isChecked);
                edt_venue.setFocusableInTouchMode(isChecked);
            }
        });
    }

    private void iniTUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cb_venue = findViewById(R.id.cb_venue);
        edt_label1 = findViewById(R.id.edt_label1);
        edt_label_1_value = findViewById(R.id.edt_label_1_value);
        edt_label2 = findViewById(R.id.edt_label2);
        edt_label_2_value = findViewById(R.id.edt_label_2_value);
        edt_label3 = findViewById(R.id.edt_label3);
        edt_label_3_value = findViewById(R.id.edt_label_3_value);
        edt_label4 = findViewById(R.id.edt_label4);
        edt_label_4_value = findViewById(R.id.edt_label_4_value);
        banner = findViewById(R.id.imageSlider);
        btn_galley = findViewById(R.id.btn_galley);
        btn_feature = findViewById(R.id.btn_feature);
        bannerAdapter = new SliderViewAdapter<BannerViewHolder>() {
            @Override
            public int getCount() {
                return imageList.size();
            }

            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent) {
                return new BannerViewHolder(LayoutInflater.from(AddPromotionActivity.this).inflate(R.layout.banner_item, parent, false));
            }

            @Override
            public void onBindViewHolder(final BannerViewHolder holder, final int position) {
                holder.iv_image.setImageURI(Uri.fromFile(new File(imageList.get(position))));

                holder.iv_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(AddPromotionActivity.this)
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
        banner.setSliderAdapter(bannerAdapter);
        btn_save = findViewById(R.id.btn_save);
        edt_to = findViewById(R.id.edt_to);
        edt_from = findViewById(R.id.edt_from);
        edt_title = findViewById(R.id.edt_title);
        edt_desc = findViewById(R.id.edt_desc);
        edt_venue = findViewById(R.id.edt_venue);
        iv_feature = findViewById(R.id.iv_feature);
        iv_feature_clear = findViewById(R.id.iv_feature_clear);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btn_galley: {
                pickImage(1, true);
            }
            break;

            case R.id.iv_feature_clear: {
                new AlertDialog.Builder(AddPromotionActivity.this)
                        .setMessage("Are you sure you want to delete Feature Image?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                iv_feature.setVisibility(View.GONE);
                                iv_feature_clear.setVisibility(View.GONE);
                                iv_feature.setImageURI("");
                                featureFile = null;
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
            break;

            case R.id.btn_feature: {
                pickImage(2, false);
            }
            break;

            case R.id.edt_venue: {
                SearchDialog dialog = new SearchDialog();
                dialog.setListener(new SearchDialog.OnSearchListener() {
                    @Override
                    public void OnVenueSelected(String id, String title, String venueType) {
                        Log.e(title, id);

                        edt_venue.setText(title);
                        edt_venue.setTag(id);
                    }
                });
                dialog.show(getSupportFragmentManager(), "SEARCH");
            }
            break;

            case R.id.edt_to: {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String s = String.format(Locale.ENGLISH, "%d-%d-%d", dayOfMonth, monthOfYear + 1, year);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat formatShow = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                try {
                                    edt_to.setText(formatShow.format(format.parse(s)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
            break;
            case R.id.edt_from: {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String s = String.format(Locale.ENGLISH, "%d-%d-%d", dayOfMonth, monthOfYear + 1, year);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat formatShow = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                try {
                                    edt_from.setText(formatShow.format(format.parse(s)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
            break;

            case R.id.btn_save: {

                if (Validator.isEmpty(edt_title)) {
                    Validator.setError(edt_title, "Enter Title");
                } else if (Validator.isEmpty(edt_desc)) {
                    Validator.setError(edt_desc, "Enter Description");
                } else if (Validator.isEmpty(edt_venue)) {
                    Toast.makeText(AddPromotionActivity.this, "Select Venue", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(edt_to)) {
                    Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(edt_from)) {
                    Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else if (featureFile == null) {
                    Toast.makeText(this, "Select Feature Image", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "promotion/update");
                    param.put("label1", Validator.getText(edt_label1));
                    param.put("busiest_days", Validator.getText(edt_label_1_value));
                    param.put("label2", Validator.getText(edt_label2));
                    param.put("music_type", Validator.getText(edt_label_2_value));
                    param.put("label3", Validator.getText(edt_label3));
                    param.put("opening_hours", Validator.getText(edt_label_3_value));
                    param.put("label4", Validator.getText(edt_label4));
                    param.put("dress_code", Validator.getText(edt_label_4_value));
                    param.put("venue_id", edt_venue.getTag() != null ? cb_venue.isChecked()?"":edt_venue.getTag().toString() : "");
                    param.put("title", Validator.getText(edt_title));
                    param.put("description", Validator.getText(edt_desc));
                    if(cb_venue.isChecked()){
                        param.put("custom_venue_title",Validator.getText(edt_venue));
                    }
                    SimpleDateFormat format = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                    SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    try {
                        param.put("from_date", finalFormate.format(Validator.getDate(format.parse(Validator.getText(edt_from)))));
                        param.put("to_date", finalFormate.format(Validator.getDate(format.parse(Validator.getText(edt_to)))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // param.put("location",Validator.getText(edt_location));
                    List<PART> partList = new ArrayList<>();
                    for (int i = 0; i < imageList.size(); i++) {
                        Log.e("IMAGE:::", imageList.get(i));
                        partList.add(new PART("image[" + i + "]", new File(imageList.get(i))));
                    }
                    communication.callPOST(param,new PART("feature_image",featureFile), partList);
                }
            }
            break;
        }
    }

    private void pickImage(int id, boolean isMulti) {
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
                if (images != null) {
                    for (Image image :
                            images) {
                        imageList.add(image.getPath());
                    }
                    bannerAdapter.notifyDataSetChanged();
                }


            } else if (requestCode == 2) {
                if (images != null && images.size() == 1) {
                    iv_feature.setVisibility(View.VISIBLE);
                    iv_feature_clear.setVisibility(View.VISIBLE);
                    featureFile = new File(images.get(0).getPath());
                   // iv_feature.setImageURI(Uri.fromFile(featureFile));
                }
            }
        }
        // You MUST have this line to be here
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        Toast.makeText(AddPromotionActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    private class BannerViewHolder extends SliderViewAdapter.ViewHolder {
        private SimpleDraweeView iv_image;
        private ImageView iv_clear;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_clear = itemView.findViewById(R.id.iv_clear);
        }
    }
}
