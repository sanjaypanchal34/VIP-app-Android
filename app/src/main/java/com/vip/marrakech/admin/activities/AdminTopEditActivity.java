package com.vip.marrakech.admin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.VideoPlayerActivity;
import com.vip.marrakech.admin.models.AdminRecommendationDetail;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.dialogs.SearchDialog;
import com.vip.marrakech.helpers.GoTo;
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

public class AdminTopEditActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private TextView edt_location;
    private Button btn_save;
    private EditText edt_title, edt_desc;
    private Communication communication;
    private Button btn_menu, btn_feature, btn_galley;
    private SimpleDraweeView iv_menu, iv_feature;
    private ImageView iv_menu_clear, iv_feature_clear;
    private SliderView imageSlider;
    private File menuFile, featutreFile;
    private ArrayList<com.vip.marrakech.admin.models.Image> imageList = new ArrayList<>();
    private SliderViewAdapter<BannerViewHolder> bannerAdapter;
    private AdminTopModel model;
    private AdminRecommendationDetail detail;
    private EditText edt_label1, edt_label_1_value;
    private EditText edt_label2, edt_label_2_value;
    private EditText edt_label3, edt_label_3_value;
    private EditText edt_label4, edt_label_4_value;
    private CheckBox cb_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_add);
        communication = new Communication(this, this);

        Bundle bundle = GoTo.getIntent(this);
        model = (AdminTopModel) bundle.getSerializable("data");
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
                return new BannerViewHolder(LayoutInflater.from(AdminTopEditActivity.this).inflate(R.layout.banner_item, parent, false));
            }

            @Override
            public void onBindViewHolder(final BannerViewHolder holder, final int position) {
                com.vip.marrakech.admin.models.Image image = imageList.get(position);
                if (image.isLocal()) {
                    File file = new File(image.getImageName());
                    if (!file.getName().endsWith(".jpg") && !file.getName().endsWith(".png")) {
                        holder.iv_play.setVisibility(View.VISIBLE);
                    } else {
                        holder.iv_play.setVisibility(View.GONE);
                    }
                    holder.iv_image.setImageURI(Uri.fromFile(file));
                } else {
                    Log.e("RECOM GALLERY:::", String.format("%s/recommandation/%s/%s", ApiClient.HOST, model.getId(), image.getImageName()));
                    if (image.getType().equalsIgnoreCase("video")) {
                        //holder.iv_image.setImageURI(String.format("%s/recommandation/%s/%s", ApiClient.HOST, model.getId(), image.getThumb_name()));
                        holder.iv_play.setVisibility(View.VISIBLE);
                        holder.skeletonLayout.showSkeleton();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri( image.getThumb_name())
                                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                                    @Override
                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                                        holder.skeletonLayout.showOriginal();
                                    }

                                    @Override
                                    public void onFailure(String id, Throwable throwable) {
                                        //Action on failure
                                    }

                                })
                                .build();
                        holder.iv_image.setController(controller);
                    } else {
                        holder.iv_play.setVisibility(View.GONE);
                        //holder.iv_image.setImageURI(String.format("%s/recommandation/%s/%s", ApiClient.HOST, model.getId(), image.getImageName()));
                        holder.skeletonLayout.showSkeleton();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(String.format("%s/recommandation/%s/%s", ApiClient.HOST, model.getId(), image.getImageName()))
                                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                                    @Override
                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                                        holder.skeletonLayout.showOriginal();
                                    }

                                    @Override
                                    public void onFailure(String id, Throwable throwable) {
                                        //Action on failure
                                    }

                                })
                                .build();
                        holder.iv_image.setController(controller);
                    }

                }

                holder.iv_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminTopEditActivity.this, VideoPlayerActivity.class);
                        if (image.isLocal()) {
                            intent.putExtra("link", image.getImageName());
                        } else {
                            intent.putExtra("link", String.format("%s/recommandation/%s/%s", ApiClient.HOST, model.getId(), image.getImageName()));
                        }
                        startActivity(intent);
                    }
                });

                holder.iv_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(AdminTopEditActivity.this)
                                .setMessage("Are you sure you want to delete this Image?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (imageList.get(position).isLocal()) {
                                            imageList.remove(position);
                                            bannerAdapter.notifyDataSetChanged();
                                            // imageSlider.setSliderAdapter(bannerAdapter);

                                        } else {
                                            deleteImage(imageList.get(position).getEncryptedId());
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                });
            }
        };
        imageSlider.setSliderAdapter(bannerAdapter);


        getDetail();
    }


    private class BannerViewHolder extends SliderViewAdapter.ViewHolder {
        private  SkeletonLayout skeletonLayout;
        private SimpleDraweeView iv_image;
        private ImageView iv_clear, iv_play;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_clear = itemView.findViewById(R.id.iv_clear);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }


    private void getDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "recommandation/detail/" + model.getEncrypted_id());
        communication.callGET(param);
    }
/*
    private void setData() {
        edt_location.setText(model.getVenue_title());
        edt_location.setTag(model.getId());
        edt_title.setText(model.getTitle());
        edt_desc.setText(model.getDescription());
        iv_5.setTag(R.id.iv_5, null);
        iv_remove_5.setVisibility(View.GONE);
        iv_5.setImageURI("");
        iv_4.setTag(R.id.iv_4, null);
        iv_remove_4.setVisibility(View.GONE);
        iv_4.setImageURI("");
        iv_3.setTag(R.id.iv_3, null);
        iv_remove_3.setVisibility(View.GONE);
        iv_3.setImageURI("");
        iv_2.setTag(R.id.iv_2, null);
        iv_remove_2.setVisibility(View.GONE);
        iv_2.setImageURI("");
        iv_1.setTag(R.id.iv_1, null);
        iv_remove_1.setVisibility(View.GONE);
        iv_1.setImageURI("");
        switch (model.getImages().size()) {
            case 0:
                break;
            case 5: {
                iv_5.setImageURI(model.getPath() + model.getId() + "/" + model.getImages().get(4).getImageName());
                iv_5.setTag(R.id.iv_5, model.getPath() + model.getId() + "/" + model.getImages().get(4).getImageName());
                iv_remove_5.setVisibility(View.VISIBLE);

            }
            case 4: {
                iv_4.setImageURI(model.getPath() + model.getId() + "/" + model.getImages().get(3).getImageName());
                iv_4.setTag(R.id.iv_4, model.getPath() + model.getId() + "/" + model.getImages().get(3).getImageName());
                iv_remove_4.setVisibility(View.VISIBLE);

            }
            case 3: {
                iv_3.setImageURI(model.getPath() + model.getId() + "/" + model.getImages().get(2).getImageName());
                iv_3.setTag(R.id.iv_3, model.getPath() + model.getId() + "/" + model.getImages().get(2).getImageName());
                iv_remove_3.setVisibility(View.VISIBLE);

            }
            case 2: {
                iv_2.setImageURI(model.getPath() + model.getId() + "/" + model.getImages().get(1).getImageName());
                iv_2.setTag(R.id.iv_2, model.getPath() + model.getId() + "/" + model.getImages().get(1).getImageName());
                iv_remove_2.setVisibility(View.VISIBLE);

            }
            case 1: {
                iv_1.setImageURI(model.getPath() + model.getId() + "/" + model.getImages().get(0).getImageName());
                iv_1.setTag(R.id.iv_1, model.getPath() + model.getId() + "/" + model.getImages().get(0).getImageName());
                iv_remove_1.setVisibility(View.VISIBLE);

            }
        }
    }*/

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
                    btn_feature.setEnabled(false);
                }
            } else if (requestCode == 2) {
                File gallery = FilePickerUriHelper.getFile(AdminTopEditActivity.this, data);
                if (gallery != null) {
                    imageList.add(new com.vip.marrakech.admin.models.Image(true, gallery.getPath()));
                    bannerAdapter.notifyDataSetChanged();
                    Log.e("MENu:::", gallery.getAbsolutePath());
                }
              /*  if (images != null && images.size() > 0) {
                    for (Image image :
                            images) {
                        imageList.add(new com.vip.marrakech.admin.models.Image(true, image.getPath()));
                    }

                }*/
            } else if (requestCode == 3) {
                menuFile = FilePickerUriHelper.getFile(AdminTopEditActivity.this, data);
                Log.e("MENu:::", menuFile.getAbsolutePath());
                iv_menu_clear.setVisibility(View.VISIBLE);
                btn_menu.setEnabled(false);

            }
        }
        // You MUST have this line to be here
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_menu: {
                Intent intent = new Intent(AdminTopEditActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerConstants.CHOOSER_TEXT, "Select Menu");
                intent.putExtra(FilePickerConstants.FILE, true);
                intent.putExtra(FilePickerConstants.MULTIPLE_TYPES, new String[]{FilePickerConstants.MIME_IMAGE, FilePickerConstants.MIME_PDF});
                startActivityForResult(intent, 3);
            }
            break;
            case R.id.iv_menu_clear: {
                new AlertDialog.Builder(AdminTopEditActivity.this)
                        .setMessage("Are you sure you want to delete Menu?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                iv_menu_clear.setVisibility(View.GONE);
                                iv_menu.setImageURI("");
                                btn_menu.setEnabled(true);
                                menuFile = null;
                                deleteImage(detail.getMenu_encrypted_id());
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
                // pickImage(2, false);
                Intent intent = new Intent(AdminTopEditActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerConstants.CHOOSER_TEXT, "Select File");
                intent.putExtra(FilePickerConstants.FILE, true);
                intent.putExtra(FilePickerConstants.MULTIPLE_TYPES, new String[]{FilePickerConstants.MIME_IMAGE, FilePickerConstants.MIME_VIDEO});
                startActivityForResult(intent, 2);
            }
            break;
            case R.id.iv_feature_clear: {
                new AlertDialog.Builder(AdminTopEditActivity.this)
                        .setMessage("Are you sure you want to delete Feature Image?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                iv_feature_clear.setVisibility(View.GONE);
                                iv_feature.setImageURI("");
                                featutreFile = null;
                                btn_feature.setEnabled(true);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
            break;

            case R.id.edt_location: {
                //openAddressSearch();

                SearchDialog dialog = new SearchDialog();
                dialog.setVenue_type(model.getVenue_type());
                dialog.setListener(new SearchDialog.OnSearchListener() {
                    @Override
                    public void OnVenueSelected(String id, String title, String venueType) {
                        Log.e(title, id);

                        edt_location.setText(title);
                        edt_location.setTag(id);
                        detail.setVenue_id(id);
                        detail.setVenue_title(title);
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
                    Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else if (featutreFile == null) {
                    Toast.makeText(this, "Select Feature Image", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "recommandation/update");
                    param.put("id", model.getEncrypted_id());
                    param.put("venue_id", detail.getVenue_id());
                    param.put("title", Validator.getText(edt_title));
                    param.put("description", Validator.getText(edt_desc));
                    param.put("type_id", detail.getType());
                    param.put("label1", Validator.getText(edt_label1));
                    param.put("busiest_days", Validator.getText(edt_label_1_value));
                    param.put("label2", Validator.getText(edt_label2));
                    param.put("music_type", Validator.getText(edt_label_2_value));
                    param.put("label3", Validator.getText(edt_label3));
                    param.put("opening_hours", Validator.getText(edt_label_3_value));
                    param.put("label4", Validator.getText(edt_label4));
                    param.put("dress_code", Validator.getText(edt_label_4_value));
                    param.put("coming_soon",cb_check.isChecked() ? "1" : "0");
                    List<PART> partList = new ArrayList<>();
                    List<PART> partListVideo = new ArrayList<>();
                    int vid = 0;
                    int img = 0;
                    for (int i = 0; i < imageList.size(); i++) {
                        if (imageList.get(i).isLocal()) {
                            File file = new File(imageList.get(i).getImageName());
                            if (!file.getName().endsWith(".jpg") && !file.getName().endsWith(".png")) {
                                partList.add(new PART("video[" + vid + "]", file));
                                vid++;
                            } else {
                                partList.add(new PART("image[" + img + "]", file));
                                img++;
                            }
                        }
                    }
                    Log.e("PARAM:::", param.toString());
                    Log.e("Video:::", String.valueOf(partListVideo.size()));
                    Log.e("image:::", String.valueOf(partList.size()));
                    if (menuFile != null) {
                        if (menuFile.exists()) {
                            if (featutreFile.exists()) {
                                communication.callPOST(param, new PART("feature_image", featutreFile), new PART("menu_image", menuFile), partList, partListVideo);
                            } else {
                                communication.callPOST(param, new PART("menu_image", menuFile), partList, partListVideo);
                            }
                        }
                    } else {
                        if (featutreFile.exists()) {
                            communication.callPOST(param, new PART("feature_image", featutreFile), partList, partListVideo);
                        } else {
                            communication.callPOST(param, partList, partListVideo);
                        }

                    }
                }
            }
            break;
        }
    }

    private void deleteImage(String encryptedId) {

        HashMap<String, String> param = new HashMap<>();
        param.put("action", "recommandation/image/delete/" + encryptedId);
        communication.callDelete(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("recommandation/update")) {
            try {
                Toast.makeText(AdminTopEditActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("recommandation/detail/" + model.getEncrypted_id())) {
            try {
                detail = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), AdminRecommendationDetail.class);
                iv_menu_clear.setVisibility(detail.getMenu_image().isEmpty() ? View.GONE : View.VISIBLE);
                iv_feature_clear.setVisibility(detail.getFeature_image().isEmpty() ? View.GONE : View.VISIBLE);
                if (iv_menu_clear.getVisibility() == View.VISIBLE) {
                    btn_menu.setEnabled(false);
                }
                if (iv_feature_clear.getVisibility() == View.VISIBLE) {
                    btn_feature.setEnabled(false);
                }
                featutreFile = new File(detail.getFeature_image());
                edt_location.setText(detail.getVenue_title());
                edt_title.setText(detail.getTitle());
                edt_desc.setText(detail.getDescription());
                edt_label1.setText(detail.getLabel1());
                        edt_label_1_value.setText(detail.getBusiest_days());
                edt_label2.setText(detail.getLabel2());
                        edt_label_2_value.setText(detail.getMusic_type());
                edt_label3.setText(detail.getLabel3());
                        edt_label_3_value.setText(detail.getOpening_hours());
                edt_label4.setText(detail.getLabel4());
                        edt_label_4_value.setText(detail.getDress_code());
                cb_check.setChecked(detail.getComing_soon().equals("1"));
                imageList.clear();
                imageList.addAll(detail.getImageList());
                bannerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.contains("recommandation/image/delete/")) {
            try {
                Toast.makeText(AdminTopEditActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                String encryptedId = jsonObject.getString("encrypted_id");
                Log.e("ID:::", encryptedId);
                List<com.vip.marrakech.admin.models.Image> images = imageList;
                for (int i = 0; i < images.size(); i++) {
                    com.vip.marrakech.admin.models.Image image = images.get(i);
                    if (image.getEncryptedId().equals(encryptedId)) {
                        detail.getImageList().remove(i);
                        imageList.remove(i);
                        break;
                    }
                }
                bannerAdapter.notifyDataSetChanged();
                //imageSlider.setSliderAdapter(bannerAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
