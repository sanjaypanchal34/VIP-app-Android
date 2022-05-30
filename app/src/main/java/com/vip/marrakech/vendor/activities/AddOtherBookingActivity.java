package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.CustomBaseAdapter;
import com.vip.marrakech.adapters.SPAdapter;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.dialogs.ReasonDialog;
import com.vip.marrakech.dialogs.SorryDialog;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.TimeModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.MyDate;
import com.vip.marrakech.user.models.TableOptionModel;
import com.vip.marrakech.vendor.adapters.TableOptionSelectAdapter;
import com.vip.marrakech.vendor.dialog.SearchConciergeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddOtherBookingActivity extends AppCompatActivity implements OnCallBackListener, View.OnClickListener, MaterialSpinner.OnExpandListener {
    private Communication communication;
    private EditText edt_first_name, edt_last_name, edt_email, edt_mobile, edt_spend, edt_desc;
    private MaterialSpinner sp_pax;
    private TextView tv_date, sp_c_name, sp_c_email;
    private Button btn_save;
    private Calendar myCalendar = Calendar.getInstance();
    List<String> paxList = new ArrayList<>();
    private LinearLayout ln_table, ln_sub_service;
    private MaterialSpinner sp_group_type;
    private RecyclerView rv_table_option;
    private LinearLayout ln_services;
    private String fixTime;
    List<TableOptionModel> list = new ArrayList<>();
    private MaterialSpinner sp_services;
    private MaterialSpinner sp_time;
    private TableOptionSelectAdapter tableAdapter;
    private List<TimeModel> timeList = new ArrayList<>();
    private String date;
    private EditText edt_table_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_booking);

        communication = new Communication(this, this);

        for (int i = 1; i <= 30; i++) {
            paxList.add(String.valueOf(i));
        }


        initUI();
        sp_group_type.setItems(getResources().getStringArray(R.array.select_group_type));


        sp_pax.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(AddOtherBookingActivity.this);
            }
        });

        btn_save.setOnClickListener(this);
        sp_pax.setOnClickListener(this);
        sp_c_name.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        sp_group_type.setOnExpandListener(this);

        sp_pax.setAdapter(new SPAdapter<String>(AddOtherBookingActivity.this, paxList) {
            @Override
            protected String setSpinnerText(String item) {
                return item;
            }
        });

        sp_time.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_services.setText(timeList.get(position).getType());
            }
        });
        /*sp_services.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0) {
//                    sp_time.setItems(getResources().getStringArray(R.array.select_first_services));
                    sp_time.setItems(firstServicetime);
                } else {
                    sp_time.setItems(secondServicetime);
                }
            }
        });*/

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

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rv_table_option = findViewById(R.id.rv_table_option);
        rv_table_option.setLayoutManager(new LinearLayoutManager(AddOtherBookingActivity.this));
        sp_group_type = findViewById(R.id.sp_group_type);
        ln_table = findViewById(R.id.ln_table);
        ln_sub_service = findViewById(R.id.ln_sub_service);
        edt_first_name = findViewById(R.id.edt_first_name);
        edt_last_name = findViewById(R.id.edt_last_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        ln_services = findViewById(R.id.ln_services);
        sp_services = findViewById(R.id.sp_services);
        sp_time = findViewById(R.id.sp_time);
        sp_pax = findViewById(R.id.sp_pax);
        tv_date = findViewById(R.id.tv_date);
        sp_c_name = findViewById(R.id.sp_c_name);
        sp_c_email = findViewById(R.id.sp_c_email);
        edt_spend = findViewById(R.id.edt_spend);
        edt_table_no = findViewById(R.id.edt_table_no);
        edt_desc = findViewById(R.id.edt_desc);
        btn_save = findViewById(R.id.btn_save);

        if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
            ln_table.setVisibility(View.VISIBLE);
            ln_services.setVisibility(View.GONE);

            sp_group_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (sp_pax.getSelectedIndex() != -1) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "venue/tableOptions");
                        param.put("venue_id", SessionManager.getVenueID());
                        param.put("group_type", sp_group_type.getText().toString());
                        param.put("group_size", sp_pax.getText().toString());
                        communication.callPOST(param);
                    }
                }
            });

            sp_pax.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (sp_group_type.getSelectedIndex() != -1) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "venue/tableOptions");
                        param.put("venue_id", SessionManager.getVenueID());
                        param.put("group_type", sp_group_type.getText().toString());
                        param.put("group_size", sp_pax.getText().toString());
                        communication.callPOST(param);
                    }
                }
            });
        } else {
            ln_sub_service.setVisibility(View.GONE);
            ln_table.setVisibility(View.GONE);

        }

        getTimeSlot();
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/book/now")) {
            try {
                if (jsonObject.getInt("status") == 1) {
                    ReasonDialog reasonDialog = new ReasonDialog();
                    reasonDialog.setCancelable(false);
                    reasonDialog.setListener( jsonObject.optString("msg"), new ReasonDialog.OnClickListener() {
                        @Override
                        public void onOkClick() {
                            onBackPressed();
                        }
                    });
                    reasonDialog.show(getSupportFragmentManager(),"reason");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("venue/tableOptions")) {

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray table_options = data.getJSONArray("table_options");
                list.clear();
                for (int i = 0; i < table_options.length(); i++) {
                    TableOptionModel optionModel = new Gson().fromJson(table_options.getJSONObject(i).toString(), TableOptionModel.class);
                    list.add(optionModel);
                }
                tableAdapter = new TableOptionSelectAdapter(AddOtherBookingActivity.this, list);
                rv_table_option.setAdapter(tableAdapter);
                tableAdapter.setListener(new TableOptionSelectAdapter.OnTableBookListener() {
                    @Override
                    public void onBookTable(TableOptionModel model) {
                        for (TableOptionModel optionModel : list) {
                            if (optionModel.getTable_name().equals(model.getTable_name())) {
                                optionModel.setSelect(true);
                            } else {
                                optionModel.setSelect(false);
                            }
                        }


                        tableAdapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("venue/timing/" + SessionManager.getVenueID())) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject timings = data.getJSONObject("timings");
                timeList.clear();
                JSONArray time_slots = timings.getJSONArray("time_slots");
                for (int i=0;i<time_slots.length();i++){
                    timeList.add(new TimeModel(time_slots.getJSONObject(i).getString("time"),time_slots.getJSONObject(i).getString("type")));
                }
                if (timings.getString("is_sitting").equals("1")) {


                    sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(AddOtherBookingActivity.this, timeList) {
                        @Override
                        protected String setSpinnerText(TimeModel item) {
                            return item.getTime();
                        }
                    });



                } else {
                    if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
                        fixTime = timeList.get(0).getTime();
                    }else {

                        sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(AddOtherBookingActivity.this, timeList) {
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

        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {
        Log.e("ERROR::::",tag);
        Log.e("ERROR MSG::::",error);
        if(tag.equalsIgnoreCase("vendor/book/now")){
            SorryDialog sorryDialog = new SorryDialog();
            List<String> listTs1 = new ArrayList<>();
            List<String> listTs2 = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(error);

                JSONArray ts1 = object.getJSONArray("ts1");
                JSONArray ts2 = object.getJSONArray("ts2");

                for(int i=0;i<ts1.length();i++){
                    listTs1.add(ts1.getString(i));
                }
                for(int i=0;i<ts2.length();i++){
                    listTs2.add(ts2.getString(i));
                }
                sorryDialog.setData(object.getString("msg"),listTs1,listTs2);
                sorryDialog.show(getSupportFragmentManager(),"sorry");
            } catch (JSONException e) {
                Log.e("ERROR:::",e.getMessage());
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: {
                boolean isSelected = false;
                TableOptionModel selectedTable = null;
                for (TableOptionModel optionModel : list) {
                    if (optionModel.isSelect()) {
                        isSelected = true;
                        selectedTable = optionModel;
                    }
                }
                if (Validator.isEmpty(edt_first_name)) {
                    Validator.setError(edt_first_name, "Enter First Name");
                } else if (Validator.isEmpty(edt_last_name)) {
                    Validator.setError(edt_last_name, "Enter Last Name");
                } /*else if (Validator.isNotEmail(edt_email)) {
                    Validator.setError(edt_email, "Enter Valid Email");
                } *//*else if (Validator.isNotMinLength(edt_mobile, 11)) {
                    Validator.setError(edt_mobile, "Enter Valid Mobile");
                } */else if (Validator.isEmpty(sp_pax)) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Group Size", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(sp_group_type)) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Group Type", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_services.getText().toString().isEmpty()) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Sitting", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_time.getText().toString().isEmpty()) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(tv_date)) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else if (ln_table.getVisibility() == View.VISIBLE && !isSelected) {
                    Toast.makeText(AddOtherBookingActivity.this, "Select Table", Toast.LENGTH_SHORT).show();
                } else if (sp_c_name.getTag() == null && Validator.isNotEmail(edt_email)) {
                    Toast.makeText(AddOtherBookingActivity.this, "Enter Email Or Enter Concierge Name", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "vendor/book/now");
                        param.put("venue_type", SessionManager.getVenue_Type());
                        param.put("vendor_id", SessionManager.getID());
                        param.put("concierge_id", sp_c_name.getTag()==null?"":sp_c_name.getTag().toString());
                        param.put("first_name", Validator.getText(edt_first_name));
                        param.put("last_name", Validator.getText(edt_last_name));
                        param.put("email", Validator.getText(edt_email));
                        param.put("phone_no", Validator.getText(edt_mobile));
                        param.put("pax", Validator.getText(sp_pax));
                        param.put("venue_id", SessionManager.getVenueID());
                        param.put("table_no", Validator.getText(edt_table_no));
                        param.put("group_type", Validator.getText(sp_group_type));
                        SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                        SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        param.put("booking_date", date/*finalFormate.format(Validator.getDate(showFormat.parse(Validator.getText(tv_date))))*/);
                        param.put("table_spend", Validator.getText(edt_spend).isEmpty() ? "0" : Validator.getText(edt_spend));
                        param.put("description", Validator.getText(edt_desc));
                        if (ln_services.getVisibility() == View.VISIBLE) {
                            param.put("service", sp_services.getText().toString());
                            param.put("booking_time", sp_time.getText().toString());
                        } else {
                            param.put("booking_time", fixTime);
                            param.put("service", "First");

                        }

                        if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
                            param.put("table_name", selectedTable.getTable_name());
                            param.put("alcohol_name", selectedTable.getAlcohol_name());
                            param.put("price", selectedTable.getPrice_in_MAD());
                        }
                        communication.callPOST(param);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
          /*  case R.id.sp_pax: {
                sp_pax.select();
            }
            break;
            case R.id.sp_table: {

                sp_table.select();
            }
            break;*/
            case R.id.sp_c_name: {
                SearchConciergeDialog dialog = new SearchConciergeDialog();
                dialog.setListener(new SearchConciergeDialog.OnSearchListener() {
                    @Override
                    public void onConciergeSelect(String id, String title, String email) {
                        Log.e(title, id);
                        sp_c_email.setText(email);
                        sp_c_name.setText(title);
                        sp_c_name.setTag(id);
                    }
                });
                dialog.show(getSupportFragmentManager(), "Concierge");
            }
            break;
            case R.id.tv_date: {
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
               /* DatePickerDialog datePickerDialog = new DatePickerDialog(AddOtherBookingActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat apiDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        date = yyyy_mm_dd.format(myCalendar.getTime());
                        tv_date.setText(showFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTime().getTime());
                datePickerDialog.show();*/
            }
            break;
        }
    }

    @Override
    public void onExpand() {

    }

    private void getTimeSlot() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "venue/timing/" + SessionManager.getVenueID());
        communication.callGET(param);
    }
}
