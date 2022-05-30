package com.vip.marrakech.user.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.PaymentActivity;
import com.vip.marrakech.adapters.ItineraryDetailAdapterUser;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.user.dialogs.BookingSummaryDialog;
import com.vip.marrakech.user.dialogs.RestaurentBookingSummaryDialog;
import com.vip.marrakech.user.models.UserItinararyModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class IternityDetailActivity extends AppCompatActivity implements OnCallBackListener {

    private ItineraryDetailAdapterUser adapter;
    private Communication communication;
    private UserItinararyModel model;
    private TextView tv_client, tv_group, tv_pax, tv_table_no, tv_holding, tv_total_spend;
    private TextView tv_day_detail, tvQrCode;
    private TextView label_cost;
    private TextView label_balance_to_pay;
    private TextView tv_balance_to_pay;
    private Button btn_cancel, btn_pay_now;
    private TextView tv_date;
    private String postData;
    private boolean iaPast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iternity_detail_user);

        communication = new Communication(this, this);

        model = (UserItinararyModel) getIntent().getSerializableExtra("data");
        iaPast = getIntent().getBooleanExtra("iaPast", false);
        initUI();
        if (!iaPast) {
            if (model.getIsPayment().equals("1") || model.getStatus().equals("UC") || model.getStatus().equals("C")) {
                btn_pay_now.setVisibility(View.GONE);
            } else {
                btn_pay_now.setVisibility(View.VISIBLE);
            }

         /*   if (model.getIsPayment().equals("1") || model.getStatus().equals("UC") || model.getStatus().equals("C")) {
                ln_currency.setVisibility(View.GONE);
            } else {
                ln_currency.setVisibility(View.VISIBLE);
            }*/
        }


        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(model.getDeposit_option().equalsIgnoreCase("fixed")){

                    RestaurentBookingSummaryDialog summaryDialog = new RestaurentBookingSummaryDialog();
                    summaryDialog.setData( model.getTime()+" "+model.getStartDateTime(), model.getPax(), model.getDeposit_percentage(),  model.getGroupType().equalsIgnoreCase("Females"),model.getDeposit_percentage());
                    summaryDialog.setLickListener(new RestaurentBookingSummaryDialog.OnPaymentCLickListener() {
                        @Override
                        public void onPayment_click(String currency) {
                            postData = "currency="+currency+"&venue_type="+model.getVenueType()+"&deposit_percentage="+""+"&price=" + model.getDeposit_percentage() + "&user_id=" + SessionManager.getID() + "&venue_name=" + model.getVenueTitle() + "&pax=" + model.getPax() +
                                    "&group_type=" + model.getGroupType() + "&itinerary_day_id=" + model.getId()  + "&time_slot=" + model.getTableName();
                            Intent intent = new Intent(IternityDetailActivity.this, PaymentActivity.class);
                            intent.putExtra("url", ApiClient.USER_ITINARIRY_PAYMET);
                            intent.putExtra("post_data", postData);
                            startActivityForResult(intent, 2022);
                        }
                    });

                    summaryDialog.show(getSupportFragmentManager(),"summary");
                }else{
                    BookingSummaryDialog summaryDialog = new BookingSummaryDialog();
                    summaryDialog.setData(model.getVenueType(),model.getTime()+" "+model.getStartDateTime(), model.getPax(),model.getTableName(),model.getTableName(), model.getDay_no(), model.getTotalSpend(),  model.getGroupType().equalsIgnoreCase("Females"),model.getDeposit_percentage());
                    summaryDialog.setLickListener(new BookingSummaryDialog.OnPaymentCLickListener() {
                        @Override
                        public void onPayment_click(String currency) {
                            postData = "currency="+currency+"&venue_type="+model.getVenueType()+"&deposit_percentage="+model.getDeposit_percentage()+"&price=" + model.getTotalSpend() + "&user_id=" + SessionManager.getID() + "&venue_name=" + model.getVenueTitle() + "&pax=" + model.getPax() +
                                    "&group_type=" + model.getGroupType() + "&itinerary_day_id=" + model.getId()  + "&time_slot=" + model.getTableName();
                            Intent intent = new Intent(IternityDetailActivity.this, PaymentActivity.class);
                            intent.putExtra("url", ApiClient.USER_ITINARIRY_PAYMET);
                            intent.putExtra("post_data", postData);
                            startActivityForResult(intent, 2022);
                        }
                    });
                    summaryDialog.show(getSupportFragmentManager(), "summary");
                }

            }
        });

        TextView label_holding = findViewById(R.id.label_holding);
        if (model.getVenueType().equalsIgnoreCase(VenueTypes.NIGHTCLUB.toString()) || model.getVenueType().equalsIgnoreCase(VenueTypes.DAY_PARTIES.toString()) ||model.getVenueType().equalsIgnoreCase(VenueTypes.EXPERIENCES.toString()) || (model.getVenueType().equalsIgnoreCase(VenueTypes.RESTAURANT.toString()) && Integer.parseInt(model.getPax()) >= SessionManager.getMinPax())) {
            label_holding.setVisibility(View.VISIBLE);
            tv_holding.setVisibility(View.VISIBLE);
            label_cost.setText(R.string.total_cost);
            label_holding.setText(R.string.deposit);
            if(model.getDeposit_option().equalsIgnoreCase("percentage")){
                tv_total_spend.setText(String.format("%s", model.getTotalSpend().isEmpty()?"0":model.getTotalSpend()));
                tv_holding.setText(model.getDeposite_MAD().equals("0")?"0":model.getDeposite_MAD());
                tv_balance_to_pay.setText(model.getBalance().equals("0")?"N/A":model.getBalance());
            }else if(model.getDeposit_option().equalsIgnoreCase("fixed")){
                tv_total_spend.setText("N/A");
                tv_balance_to_pay.setText("N/A");
                tv_holding.setText(model.getDeposite_MAD().equals("0")?"N/A":model.getDeposite_MAD().isEmpty()?"N/A":model.getDeposite_MAD());
            }else{
                tv_total_spend.setText(String.format("%s", model.getTotalSpend().isEmpty()?"N/A":model.getTotalSpend()));
            }

        } else {
            label_holding.setVisibility(View.VISIBLE);
            tv_holding.setVisibility(View.VISIBLE);
            tv_total_spend.setText(String.format("%s", model.getTotalSpend().equals("0")?"N/A":model.getTotalSpend()));
            tv_holding.setText(model.getDeposite_MAD().equals("0")?"N/A":model.getDeposite_MAD());
            tv_balance_to_pay.setText(model.getBalance().equals("0")?"N/A":model.getBalance());
        }


        tv_client.setText(getResources().getString(R.string.cline_name_s, model.getClientName()));
        tv_group.setText(getResources().getString(R.string.group_type_s, model.getGroupType()));
        tv_pax.setText(getResources().getString(R.string.pax_s, model.getPax()));
        tv_table_no.setText(String.format("%s : %s", getResources().getString(R.string.table_number), model.getTableNo()));
        tv_date.setText(model.getStartDateTime().isEmpty()?"0":model.getStartDateTime());




        tv_day_detail.setText(String.format("%s - %s - %s%s%s", model.getTime(), model.getVenueTitle(), model.getVenueType(), model.getTableName().isEmpty() ? "" : String.format(" - %s", model.getTableName()), model.getBottleName().isEmpty() ? "" : String.format(" - %s", model.getBottleName()), model.getTableNo().isEmpty() ? "" : String.format(" - %s", model.getTableNo())));

        tv_day_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCodeScreen();
            }
        });

        tvQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQRCodeScreen();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getStatus().equals("UC") && !model.getStatus().equals("C")) {
                    new AlertDialog.Builder(IternityDetailActivity.this)
                            .setMessage(R.string.you_are_going_to_cancel_booking)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("action", "user/cancel/booking");
                                    param.put("itinerary_id", model.getEncryptedItineraryId());
                                    param.put("itinerary_day_id", model.getEncryptedDayId());
                                    communication.callPOST(param);
                                }
                            }).setNegativeButton("NO", null)
                            .show();
                }
            }
        });

        if (model.getStatus().equals("A")) {
            btn_cancel.setVisibility(View.GONE);
        } else if (model.getStatus().equals("UC") || model.getStatus().equals("C")) {
            btn_cancel.setText("C");
            btn_cancel.setVisibility(View.VISIBLE);
        } else if (iaPast) {
            btn_cancel.setText(R.string.cancel);
            btn_cancel.setVisibility(View.GONE);
        } else {
            btn_cancel.setText(R.string.cancel);
            btn_cancel.setVisibility(View.VISIBLE);
        }


        // getList();
    }

    private void openQRCodeScreen() {
        if (model.getStatus().equals("C") || model.getStatus().equals("UC")) {
            new AlertDialog.Builder(IternityDetailActivity.this)
                    .setMessage(R.string.qr_code_can_not_generate)
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        } else {
            if (model.getScanCode().equals("1")) {
                File qrcode = QRCode.from("itinerary_" + model.getEncryptedDayId() + "_" + model.getEncryptedItineraryId() + "_" + SessionManager.getEncryptedID()).withSize(700, 700).to(ImageType.JPG).file();
                Intent intent = new Intent(IternityDetailActivity.this, QRCodeActivity.class);
                intent.putExtra("code", qrcode.getAbsolutePath());
                startActivity(intent);
            } else {
                new AlertDialog.Builder(IternityDetailActivity.this)
                        .setMessage(R.string.qr_code_will_generate_once)
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2022) {
            if (resultCode == Activity.RESULT_OK) {
                onBackPressed();
            }

        }
    }

    private void getList() {
      /*  HashMap<String,String> param = new HashMap<>();
        param.put("action", String.format("itinerary/%s ", id));
        communication.callGET(param);*/
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_pay_now = findViewById(R.id.btn_pay_now);
        tv_day_detail = findViewById(R.id.tv_day_detail);
        tvQrCode = findViewById(R.id.tvQrCode);
        tv_date = findViewById(R.id.tv_date);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_holding = findViewById(R.id.tv_holding);
        tv_client = findViewById(R.id.tv_client);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        tv_table_no = findViewById(R.id.tv_table_no);
        label_cost = findViewById(R.id.label_cost);
        label_balance_to_pay = findViewById(R.id.label_balance_to_pay);
        tv_balance_to_pay = findViewById(R.id.tv_balance_to_pay);

        /*adapter = new ItineraryDetailAdapterUser(IternityDetailActivity.this, list);
        adapter.setListener(new ItineraryDetailAdapterUser.OnItineraryDetailCLickListener() {
            @Override
            public void onItineraryDayDataClick(String uniqueId, String scan_code, String status) {
                if (status.equals("C") || status.equals("UC")) {
                    new AlertDialog.Builder(IternityDetailActivity.this)
                            .setMessage("QR code cannot be generated, because this booking has been cancelled.")
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                } else {
                    if (scan_code.equals("1")) {
                        File qrcode = QRCode.from("itinerary_" + uniqueId + "_" + model.getEncryptedId() + "_" + SessionManager.getEncryptedID()).withSize(700, 700).to(ImageType.JPG).file();
                        Intent intent = new Intent(IternityDetailActivity.this, QRCodeActivity.class);
                        intent.putExtra("code", qrcode.getAbsolutePath());
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(IternityDetailActivity.this)
                                .setMessage("QR code will generate once the booking is confirmed.")
                                .setPositiveButton(android.R.string.yes, null)
                                .show();
                    }
                }
            }

            @Override
            public void onItineraryCancelClick(String encrypted_day_id) {
                new AlertDialog.Builder(IternityDetailActivity.this)
                        .setMessage("You are going to cancel the booking, is this ok?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("action", "user/cancel/booking");
                                param.put("itinerary_id", model.getEncryptedId());
                                param.put("itinerary_day_id", encrypted_day_id);
                                communication.callPOST(param);
                            }
                        }).setNegativeButton("NO", null)
                        .show();


            }
        });
        rv_itinerary_detail.setAdapter(adapter);*/
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
      /*  if (tag.equals(String.format("itinerary/%s", id))){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                 detailModel = new Gson().fromJson(data.toString(),UserItinararyModel.class);
                Log.e("ID::::",detailModel.getPax());
                list.clear();
                list.addAll(detailModel.getGetItineraryDayDetail());
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        if (tag.equals("user/cancel/booking")) {
            Toast.makeText(IternityDetailActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
