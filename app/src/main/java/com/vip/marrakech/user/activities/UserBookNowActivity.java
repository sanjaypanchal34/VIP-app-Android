package com.vip.marrakech.user.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.PaymentActivity;
import com.vip.marrakech.adapters.CustomBaseAdapter;
import com.vip.marrakech.admin.adapters.AdminItineraryDayDataBottleAdapter;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.dialogs.ReasonDialog;
import com.vip.marrakech.dialogs.SearchDialog;
import com.vip.marrakech.dialogs.SorryDialog;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;
import com.vip.marrakech.models.TimeModel;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.TableOptionAdapter;
import com.vip.marrakech.user.dialogs.BookingSummaryDialog;
import com.vip.marrakech.user.dialogs.RestaurentBookingSummaryDialog;
import com.vip.marrakech.user.models.MyDate;
import com.vip.marrakech.user.models.TableOptionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserBookNowActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener, MaterialSpinner.OnExpandListener {

    private Toolbar toolBar;
    private TextView tv_venue;
    TextView tv_date;
    private MaterialSpinner sp_pax, sp_group_type;
    private MaterialSpinner sp_services, sp_time, sp_table_type;
    private Button btn_book_now;
    private boolean isVenue = false;
    private String venueID;
    private final ArrayList<String> paxList = new ArrayList<>();
    private final Calendar myCalendar = Calendar.getInstance();
    private String venueName = "";
    private DatabaseHelper databaseHelper;
    private LinearLayout ln_bottle, ln_table, ln_sub_service;
    private String venueType = "";
    private Communication communication;
    private AdminItineraryDayDataBottleAdapter bottleAdapter;
    private final List<GetItineraryDayBottleDetail> bottleList = new ArrayList<>();
    private ImageView iv_add_bottle;
    private RecyclerView rv_bottle;
    private RelativeLayout rl_bottle;
    private TextView tv_total_spend;
    private LinearLayout ln_services;
    private String type = "";
    private String type_id = "";
    //    private String[] firstServicetime;
//    private String[] secondServicetime;
    private RecyclerView rv_table_option;
    private String postData = "";
    private String fixTime;
    private TextView tv_table_option;
    private ImageView iv_location;
    private String latitude;
    private String longitude;
    //    private LinearLayout ln_info;
    //private TextView tv_imp;
    private final List<TimeModel> timeList = new ArrayList<>();
    private String date;
    private TableOptionModel tableOption;
    private String deposit_option;
    private String deposit_percentage;
    /*    private LinearLayout ln_currency;*/
    /*  private MaterialSpinner sp_currency;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_now);
        communication = new Communication(this, this);
        databaseHelper = new DatabaseHelper(this);
        Bundle bundle = GoTo.getIntent(this);
        /* ln_currency = findViewById(R.id.ln_currency);*/
        initUI();

        if (bundle != null) {
            isVenue = bundle.getBoolean("is_venue");
            venueID = bundle.getString("venueID");
            venueName = bundle.getString("venueName");
            venueType = bundle.getString("venueType");
            latitude = bundle.getString("latitude");
            longitude = bundle.getString("longitude");
            type = bundle.getString("type");
            type_id = bundle.getString("type_id");
            deposit_option = bundle.getString("deposit_option");
            tv_venue.setText(venueName);
            bottleAdapter.notifyDataSetChanged();

            ln_sub_service.setVisibility(View.GONE);
            if (deposit_option.equalsIgnoreCase("percentage")) {
                tv_table_option.setVisibility(View.VISIBLE);
//                ln_info.setBackgroundColor(getResources().getColor(R.color.book_btn));
//                tv_info.setTextColor(getResources().getColor(R.color.white));
                //tv_imp.setVisibility(View.VISIBLE);
//                tv_info.setVisibility(View.VISIBLE);
//                tv_info.setText(getResources().getString(R.string.night_club_des));
                ln_services.setVisibility(View.GONE);
                btn_book_now.setVisibility(View.GONE);
            } else {
                ln_services.setVisibility(View.VISIBLE);
                btn_book_now.setVisibility(View.VISIBLE);
                tv_table_option.setVisibility(View.GONE);
            }
            getTimeSlot();
            setAdapter();
        } else {
            setAdapter();

        }

        setBottleVisiblity();
        setTableVisiblity();


        sp_pax.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (deposit_option.equalsIgnoreCase("percentage")) {
                    if (sp_group_type.getSelectedIndex() != -1) {

                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "venue/tableOptions");
                        param.put("venue_id", venueID);
                        param.put("group_type", sp_group_type.getText().toString());
                        param.put("group_size", sp_pax.getText().toString());
                        communication.callPOST(param);
                    }
                }
            }
        });

        for (int i = 1; i <= 30; i++) {
            paxList.add(String.valueOf(i));
        }

        tv_venue.setText(venueName);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != null && longitude != null && venueName != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("latitude", latitude);
                    bundle.putString("longitude", longitude);
                    bundle.putString("venueName", venueName);
                    GoTo.startWithExtra(UserBookNowActivity.this, UserMapActivity.class, bundle);
                }
            }
        });

        if (!isVenue) {
            tv_venue.setVisibility(View.VISIBLE);
            tv_venue.setOnClickListener(this);
        }

        sp_pax.setItems(paxList);
        sp_group_type.setItems(getResources().getStringArray(R.array.select_group_type));
        sp_table_type.setItems(getResources().getStringArray(R.array.select_table_type));


        sp_time.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                fixTime = timeList.get(0).getTime();
                sp_services.setText(timeList.get(position).getType());
            }
        });
      /*  sp_services.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0) {

                } else {
                    sp_time.setItems(secondServicetime);
                }
            }
        });*/

    /*    sp_bottle_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                bottleNameList = databaseHelper.getAllBottle(bottleTypeList.get(position));
                sp_bottle_name.setAdapter(new MaterialSpinnerAdapter<MasterBottelModel>(UserBookNowActivity.this, bottleNameList) {
                    @Override
                    public String getItemText(int position) {
                        return bottleNameList.get(position).getBottle_name();
                    }
                });


            }
        });


        sp_bottle_name.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<MasterBottelModel>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, MasterBottelModel item) {
                sp_bottle_name.setText(item.getBottle_name());
                sp_bottle_name.setTag(item.getId());
            }
        });*/
        tv_date.setOnClickListener(this);

        iv_add_bottle.setOnClickListener(this);
        btn_book_now.setOnClickListener(this);

        Log.e("SELECTED :::", String.valueOf(sp_group_type.getSelectedIndex()));

        sp_pax.setOnExpandListener(this);
        sp_group_type.setOnExpandListener(this);
        sp_services.setOnExpandListener(this);
        sp_time.setOnExpandListener(this);
        sp_table_type.setOnExpandListener(this);
    }

    private void setAdapter() {
        bottleAdapter = new AdminItineraryDayDataBottleAdapter(UserBookNowActivity.this, bottleList, venueID, new AdminItineraryDayDataBottleAdapter.OnBottleSelectListener() {
            @Override
            public void onBottleSelect(MasterBottelModel item, int pos) {
                int total = 0;

                for (GetItineraryDayBottleDetail dayBottleDetail :
                        bottleList) {
                    if (!dayBottleDetail.getBottlePrice().isEmpty())
                        total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
                }
                tv_total_spend.setText(String.format(Locale.ENGLISH, getString(R.string.bottle_spend_s), Validator.getFormattedNumber(String.valueOf(total))));
                bottleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBottleDelete(int adapterPosition) {
                bottleAdapter.notifyDataSetChanged();
                int total = 0;

                for (GetItineraryDayBottleDetail dayBottleDetail :
                        bottleList) {
                    if (!dayBottleDetail.getBottlePrice().isEmpty())
                        total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
                }
                tv_total_spend.setText(String.format(Locale.ENGLISH, getString(R.string.bottle_spend_s), Validator.getFormattedNumber(String.valueOf(total))));
            }
        });
        rv_bottle.setAdapter(bottleAdapter);
    }

    private void setTableVisiblity() {
        if (deposit_option.equalsIgnoreCase("percentage")) {
            /*ln_currency.setVisibility(View.VISIBLE);*/
            ln_table.setVisibility(View.GONE);
            sp_group_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (sp_pax.getSelectedIndex() != -1) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "venue/tableOptions");
                        param.put("venue_id", venueID);
                        param.put("from_user", "1");
                        param.put("group_type", String.valueOf(sp_group_type.getSelectedIndex()));
                        param.put("group_size", sp_pax.getText().toString());
                        communication.callPOST(param);
                    }
                }
            });


        } else {
            ln_table.setVisibility(View.GONE);
            /*ln_currency.setVisibility(View.GONE);*/
        }
    }

    private void initUI() {
       /* sp_currency = findViewById(R.id.sp_currency);
        sp_currency.setItems("MAD", "GBP", "EUR", "USD");*/
        tv_table_option = findViewById(R.id.tv_table_option);
//        ln_info = findViewById(R.id.ln_info);
        //tv_imp = findViewById(R.id.tv_imp);
        //tv_info = findViewById(R.id.tv_info);
        rv_table_option = findViewById(R.id.rv_table_option);
        rv_table_option.setLayoutManager(new LinearLayoutManager(UserBookNowActivity.this));
        ln_services = findViewById(R.id.ln_services);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        ln_sub_service = findViewById(R.id.ln_sub_service);
        ln_table = findViewById(R.id.ln_table);
        ln_bottle = findViewById(R.id.ln_bottle);
        toolBar = findViewById(R.id.toolBar);
        tv_date = findViewById(R.id.tv_date);
        tv_venue = findViewById(R.id.tv_venue);
        iv_location = findViewById(R.id.iv_location);
        sp_pax = findViewById(R.id.sp_pax);
        sp_group_type = findViewById(R.id.sp_group_type);
        sp_services = findViewById(R.id.sp_services);
        sp_time = findViewById(R.id.sp_time);
        sp_table_type = findViewById(R.id.sp_table_type);
//        sp_bottle_type = findViewById(R.id.sp_bottle_type);
//        sp_bottle_name = findViewById(R.id.sp_bottle_name);
        btn_book_now = findViewById(R.id.btn_book_now);

        iv_add_bottle = findViewById(R.id.iv_add_bottle);
        rl_bottle = findViewById(R.id.rl_bottle);
        rv_bottle = findViewById(R.id.rv_bottle);
        rv_bottle.setNestedScrollingEnabled(false);
        rv_bottle.setLayoutManager(new LinearLayoutManager(UserBookNowActivity.this));
        tv_total_spend.setText(String.format(Locale.ENGLISH, getString(R.string.bottle_spend_s), Validator.getFormattedNumber(String.valueOf(0))));
        bottleAdapter = new AdminItineraryDayDataBottleAdapter(UserBookNowActivity.this, bottleList, venueID, new AdminItineraryDayDataBottleAdapter.OnBottleSelectListener() {
            @Override
            public void onBottleSelect(MasterBottelModel item, int pos) {
                int total = 0;

                for (GetItineraryDayBottleDetail dayBottleDetail :
                        bottleList) {
                    if (!dayBottleDetail.getBottlePrice().isEmpty())
                        total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
                }
                tv_total_spend.setText(String.format(Locale.ENGLISH, getString(R.string.bottle_spend_s), Validator.getFormattedNumber(String.valueOf(total))));
                bottleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBottleDelete(int adapterPosition) {

                bottleAdapter.notifyDataSetChanged();

                int total = 0;

                for (GetItineraryDayBottleDetail dayBottleDetail :
                        bottleList) {
                    if (!dayBottleDetail.getBottlePrice().isEmpty())
                        total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
                }
                tv_total_spend.setText(String.format(Locale.ENGLISH, getString(R.string.bottle_spend_s), Validator.getFormattedNumber(String.valueOf(total))));
            }
        });
        rv_bottle.setAdapter(bottleAdapter);


    }

    private void getTimeSlot() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "venue/timing/" + venueID);
        communication.callGET(param);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date) {
            CalenderDialog dialog = new CalenderDialog();
            dialog.setMinDate(new Date());
            dialog.setListener(new OnCalenderListener() {
                @Override
                public void onDateSelect(MyDate mydate) {
                    SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                    SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    date = yyyy_mm_dd.format(mydate.getDate());
                    tv_date.setText(showFormat.format(mydate.getDate()));
                }
            });
            dialog.show(getSupportFragmentManager(), "date");
        /*    DatePickerDialog datePickerDialog = new DatePickerDialog(UserBookNowActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                    SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    date = yyyy_mm_dd.format(myCalendar.getTime());
                    tv_date.setText(showFormat.format(myCalendar.getTime()));
                }
            }, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();*/
        } else if (v.getId() == R.id.tv_venue) {
            SearchDialog dialog = new SearchDialog();
            dialog.setListener(new SearchDialog.OnSearchListener() {
                @Override
                public void OnVenueSelected(String id, String title, String venue_type) {
                    Log.e(title, id);
                    tv_venue.setText(title);
                    venueID = id;
                    venueType = venue_type;
                   /* bottleTypeList.clear();
                    bottleTypeList.addAll(databaseHelper.getBottleType(venueID));
                    sp_bottle_type.setItems(bottleTypeList);*/
                    bottleList.clear();
                    bottleAdapter.notifyDataSetChanged();
                    setBottleVisiblity();
                    setTableVisiblity();
                    setAdapter();
                }
            });
            dialog.show(getSupportFragmentManager(), "SEARCH");
        } else if (v.getId() == R.id.iv_add_bottle) {
            if (bottleList.size() == 0) {
                bottleList.add(new GetItineraryDayBottleDetail());
            } else {
                GetItineraryDayBottleDetail data = bottleList.get(bottleList.size() - 1);
                if (data.getBottleType().isEmpty() || data.getBottleName().isEmpty()) {
                    Toast.makeText(UserBookNowActivity.this, getString(R.string.select_bottle_or_name), Toast.LENGTH_SHORT).show();
                } else {
                    bottleList.add(new GetItineraryDayBottleDetail());
                }
            }
            bottleAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.btn_book_now) {
            Log.e("SELECTED :::", String.valueOf(sp_group_type.getSelectedIndex()));
            if (venueID == null) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_venue), Toast.LENGTH_SHORT).show();
            } else if (sp_pax.getSelectedIndex() == -1) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_group_size), Toast.LENGTH_SHORT).show();
            } else if (sp_group_type.getSelectedIndex() == -1) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_group_type), Toast.LENGTH_SHORT).show();
            } else if (Validator.getText(tv_date).isEmpty()) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_date), Toast.LENGTH_SHORT).show();
            } else if (ln_services.getVisibility() == View.VISIBLE && sp_services.getText().toString().isEmpty()) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_service), Toast.LENGTH_SHORT).show();
            } else if (ln_services.getVisibility() == View.VISIBLE && sp_time.getText().toString().isEmpty()) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_time), Toast.LENGTH_SHORT).show();
            } else if (ln_table.getVisibility() == View.VISIBLE && sp_table_type.getText().toString().isEmpty()) {
                Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_table_type), Toast.LENGTH_SHORT).show();
            } else if (venueType.equalsIgnoreCase(VenueTypes.RESTAURANT.toString())) {
                int i = Integer.parseInt("0" + sp_pax.getText().toString());

                if (i < SessionManager.getMinPax() || deposit_percentage.equalsIgnoreCase("0")) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "user/book/now");
                    param.put("user_id", SessionManager.getID());
                    param.put("venue_id", venueID);
                    param.put("group_type", String.valueOf(sp_group_type.getSelectedIndex()));
                    param.put("pax", sp_pax.getText().toString());
                    param.put("type", type);
                    param.put("type_id", type_id);
                    if (ln_services.getVisibility() == View.VISIBLE) {
                        param.put("total_spend_MAD", "0");
                        param.put("alcohol_name", "");
                        param.put("service", sp_services.getText().toString());
                        param.put("time", sp_time.getText().toString());
                        param.put("time_slot", sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting");
                    }
                    if (deposit_option.equalsIgnoreCase("percentage")) {
                        param.put("total_spend_MAD", tableOption.getPrice_in_MAD());
                        param.put("alcohol_name", tableOption.getAlcohol_name());
                    }
                    //param.put("booking_from", booking_from);
                    SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                    SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    param.put("date", date);

                    communication.callPOST(param);
                } else {
                    checkAvailability((sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting"));
                }
            } else {
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "user/book/now");
                param.put("user_id", SessionManager.getID());
                param.put("venue_id", venueID);
                param.put("group_type", String.valueOf(sp_group_type.getSelectedIndex()));
                param.put("pax", sp_pax.getText().toString());
                param.put("type", type);
                param.put("type_id", type_id);
                //param.put("booking_from", booking_from);
                SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                param.put("date", date);
                if (ln_services.getVisibility() == View.VISIBLE) {
                    param.put("total_spend_MAD", "0");
                    param.put("alcohol_name", "");
                    param.put("service", sp_services.getText().toString());
                    param.put("time", sp_time.getText().toString());
                    param.put("time_slot", sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting");
                }
                if (!deposit_percentage.equalsIgnoreCase("0")) {
                    checkAvailability((sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting"));
                } else {
                    communication.callPOST(param);
                }

            }
        }
    }

    private void setBottleVisiblity() {
        ln_bottle.setVisibility(View.GONE);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("user/book/now")) {
            ReasonDialog reasonDialog = new ReasonDialog();
            reasonDialog.setCancelable(false);
            reasonDialog.setListener(jsonObject.optString("msg"), new ReasonDialog.OnClickListener() {
                @Override
                public void onOkClick() {
                    GoTo.startWithClearTop(UserBookNowActivity.this, UserMainActivity.class);
                }
            });
            reasonDialog.show(getSupportFragmentManager(), "reason");
        } else if (tag.equalsIgnoreCase("vendor/check/availability")) {

            if (deposit_option.equalsIgnoreCase("fixed")) {
                int pax = Integer.parseInt(sp_pax.getText().toString());
                RestaurentBookingSummaryDialog summaryDialog = new RestaurentBookingSummaryDialog();
                try {
                    summaryDialog.setData(new SimpleDateFormat("hh:mm a EEE dd/MM").format(new SimpleDateFormat("HH:mm yyyy-MM-dd").parse((sp_time.getText().toString().equals("24:00") ? "00:00" : sp_time.getText().toString()) + " " + date)), sp_pax.getText().toString(), deposit_percentage, sp_group_type.getSelectedIndex() == 2, "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                summaryDialog.setLickListener(new RestaurentBookingSummaryDialog.OnPaymentCLickListener() {
                    @Override
                    public void onPayment_click(String currency) {

                        postData = "currency=" + currency + "&price=" + deposit_percentage + "&venue_id=" + venueID + "&venue_name=" + venueName + "&venue_type=" + venueType + "&user_id=" + SessionManager.getID() + "&group_type=" + sp_group_type.getSelectedIndex() + "&service=" + sp_services.getText().toString() + "&pax=" + pax + "&date=" + date + "&time=" + sp_time.getText().toString() + "&type=" + type + "&type_id=" + type_id + "&deposit_percentage=" + "" + "&time_slot=" + (sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting");

                        Intent intent = new Intent(UserBookNowActivity.this, PaymentActivity.class);
                        intent.putExtra("url", ApiClient.USER_PAYMET);
                        intent.putExtra("post_data", postData);

                        startActivityForResult(intent, 2022);
                    }
                });

                summaryDialog.show(getSupportFragmentManager(), "summary");
            } else if (deposit_option.equalsIgnoreCase("percentage")) {
                int pax = Integer.parseInt(sp_pax.getText().toString());
                BookingSummaryDialog summaryDialog = new BookingSummaryDialog();
                try {
                    summaryDialog.setData(venueType,new SimpleDateFormat("hh:mm a EEE dd/MM").format(new SimpleDateFormat("HH:mm yyyy-MM-dd").parse((fixTime.equals("24:00") ? "00:00" : fixTime) + " " + date)), sp_pax.getText().toString(), tableOption.getTable_name(), tableOption.getDisplay_name(), tableOption.getNumber_of_table(), tableOption.getPrice_in_MAD(), sp_group_type.getSelectedIndex() == 2,deposit_percentage);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                summaryDialog.setLickListener(new BookingSummaryDialog.OnPaymentCLickListener() {
                    @Override
                    public void onPayment_click(String currency) {
                        postData = "currency=" + currency + "&price=" + tableOption.getPrice_in_MAD() + "&bottle_name=" + tableOption.getAlcohol_name() + "&venue_id=" + venueID + "&venue_name=" + venueName + "&venue_type=" + venueType + "&user_id=" + SessionManager.getID() + "&group_type=" + sp_group_type.getSelectedIndex() + "&service=" + sp_services.getText().toString() + "&pax=" + pax + "&date=" + date + "&time=" + fixTime + "&type=" + type + "&type_id=" + type_id + "&deposit_percentage=" + deposit_percentage + "&time_slot=" + tableOption.getTable_name();

                        Intent intent = new Intent(UserBookNowActivity.this, PaymentActivity.class);
                        intent.putExtra("url", ApiClient.USER_PAYMET);
                        intent.putExtra("post_data", postData);

                        startActivityForResult(intent, 2022);
                    }
                });
                summaryDialog.show(getSupportFragmentManager(), "summary");
            }

        } else if (tag.equalsIgnoreCase("venue/timing/" + venueID)) {

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                deposit_percentage = data.getString("deposit_percentage");
//                String advanced_hours = data.getString("advanced_hours");
//                String deposit_option = data.getString("deposit_option");
                JSONObject timings = data.getJSONObject("timings");
                timeList.clear();
                JSONArray time_slots = timings.getJSONArray("time_slots");
                for (int i = 0; i < time_slots.length(); i++) {
                    timeList.add(new TimeModel(time_slots.getJSONObject(i).getString("time"), time_slots.getJSONObject(i).getString("type")));
                }
                if (timings.getString("is_sitting").equals("1")) {


                    sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(UserBookNowActivity.this, timeList) {
                        @Override
                        protected String setSpinnerText(TimeModel item) {
                            return item.getTime();
                        }
                    });


                } else {
                    if (deposit_option.equalsIgnoreCase("percentage")) {
                        fixTime = timeList.get(0).getTime();
                    } else {

                        sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(UserBookNowActivity.this, timeList) {
                            @Override
                            protected String setSpinnerText(TimeModel item) {
                                return item.getTime();
                            }
                        });

                    }
                }
//                sp_services.setSelectedIndex(0);
//                sp_time.setItems(firstServicetime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("venue/tableOptions")) {

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray table_options = data.getJSONArray("table_options");
                List<TableOptionModel> list = new ArrayList<>();
                for (int i = 0; i < table_options.length(); i++) {
                    TableOptionModel optionModel = new Gson().fromJson(table_options.getJSONObject(i).toString(), TableOptionModel.class);
                    list.add(optionModel);
                }
                TableOptionAdapter tableAdapter = new TableOptionAdapter(UserBookNowActivity.this, list);
                rv_table_option.setAdapter(tableAdapter);
                tableAdapter.setListener(new TableOptionAdapter.OnTableBookListener() {
                    @Override
                    public void onBookTable(TableOptionModel model) {
                        tableOption = model;
                        if (tv_date.getText().toString().isEmpty()) {
                            Toast.makeText(UserBookNowActivity.this, getResources().getString(R.string.select_date), Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("action", "user/book/now");
                            param.put("user_id", SessionManager.getID());
                            param.put("venue_id", venueID);
                            param.put("group_type", String.valueOf(sp_group_type.getSelectedIndex()));
                            param.put("pax", sp_pax.getText().toString());
                            param.put("type", type);
                            param.put("type_id", type_id);
                            //param.put("booking_from", booking_from);
                            SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            param.put("date", date);
                            param.put("service", "First");
                            param.put("total_spend_MAD",model.getPrice_in_MAD());
                            param.put("alcohol_name",model.getAlcohol_name());
                            param.put("time", fixTime);
                            param.put("time_slot",model.getTable_name());

                            if (!deposit_percentage.equalsIgnoreCase("0")) {
                                checkAvailability((sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting"));
                            } else {
                                communication.callPOST(param);
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {
        Log.e("ERROR::::", tag);
        Log.e("ERROR MSG::::", error);
        if (tag.equalsIgnoreCase("vendor/check/availability")) {
            SorryDialog sorryDialog = new SorryDialog();
            List<String> listTs1 = new ArrayList<>();
            List<String> listTs2 = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(error);

                JSONArray ts1 = object.getJSONArray("ts1");
                JSONArray ts2 = object.getJSONArray("ts2");

                for (int i = 0; i < ts1.length(); i++) {
                    listTs1.add(ts1.getString(i));
                }
                for (int i = 0; i < ts2.length(); i++) {
                    listTs2.add(ts2.getString(i));
                }
                sorryDialog.setData(object.getString("msg"), listTs1, listTs2);
                sorryDialog.show(getSupportFragmentManager(), "sorry");
            } catch (JSONException e) {
                Log.e("ERROR:::", e.getMessage());
            }
        } else if (tag.equalsIgnoreCase("user/book/now")) {
            SorryDialog sorryDialog = new SorryDialog();
            List<String> listTs1 = new ArrayList<>();
            List<String> listTs2 = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(error);

                JSONArray ts1 = object.getJSONArray("ts1");
                JSONArray ts2 = object.getJSONArray("ts2");

                for (int i = 0; i < ts1.length(); i++) {
                    listTs1.add(ts1.getString(i));
                }
                for (int i = 0; i < ts2.length(); i++) {
                    listTs2.add(ts2.getString(i));
                }
                sorryDialog.setData(object.getString("msg"), listTs1, listTs2);
                sorryDialog.show(getSupportFragmentManager(), "sorry");
            } catch (JSONException e) {
                Log.e("ERROR:::", e.getMessage());
            }
        }
    }

    @Override
    public void onExpand() {
        hideKeyboard(UserBookNowActivity.this);
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2022) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(UserBookNowActivity.this, UserMainActivity.class);
                intent.putExtra("page", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        }
    }


    private void checkAvailability(String time_slot) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "vendor/check/availability");
            param.put("user_id", SessionManager.getID());
            param.put("venue_id", venueID);
            param.put("pax", sp_pax.getText().toString());

            if (deposit_option.equalsIgnoreCase("percentage")) {
                param.put("time", fixTime);
                param.put("time_slot", tableOption.getTable_name());
            } else {
                param.put("time", sp_time.getText().toString());
                param.put("time_slot", time_slot);
            }


            param.put("date", date/* format.format(Validator.getDate(pickerformat.parse(tv_date.getText().toString())))*/);

            communication.callPOST(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
