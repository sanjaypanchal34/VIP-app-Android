package com.vip.marrakech.vendor.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.CustomBaseAdapter;
import com.vip.marrakech.adapters.SPAdapter;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.dialogs.SorryDialog;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.GoTo;
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditOtherBookingActivity extends AppCompatActivity implements OnCallBackListener, View.OnClickListener {
    private Communication communication;
    private EditText edt_first_name, edt_last_name, edt_email, edt_mobile, edt_spend,edt_table_no, edt_desc;
    private MaterialSpinner sp_pax;
    private MaterialSpinner sp_services;
    private MaterialSpinner sp_time;
    private TextView tv_date, sp_c_name, sp_c_email;
    private Button btn_save;
    private Calendar myCalendar = Calendar.getInstance();
    List<String> paxList = new ArrayList<>();
    List<TimeModel> timeList = new ArrayList<>();
    private String id;
    private String status;
    private TextView rd_arrived, rd_non_arrived;
    private String booking_id;
    private boolean isArrived = false;
    private LinearLayout ln_table;
    private MaterialSpinner sp_group_type;
    private RecyclerView rv_table_option;
    private String table_name;
    private LinearLayout ln_services,ln_sub_service;
    private String fixTime;
    List<TableOptionModel> list = new ArrayList<>();
    private TableOptionSelectAdapter tableAdapter;
    private String service;
    private String time;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_other_booking);
        communication = new Communication(this, this);
        Bundle bundle = GoTo.getIntent(this);
        initUI();
        sp_group_type.setItems(getResources().getStringArray(R.array.select_group_type));
        if (bundle != null) {
            id = bundle.getString("id");

            if (id != null) {
                HashMap<String, String> param = new HashMap<>();
                param.put("action", String.format("vendor/other/booking/detail/%s", id));
                communication.callGET(param);

            }
        }


        for (int i = 1; i <= 30; i++) {
            paxList.add(String.valueOf(i));
        }


        btn_save.setOnClickListener(this);
        sp_pax.setOnClickListener(this);
        sp_c_name.setOnClickListener(this);
        tv_date.setOnClickListener(this);

        sp_pax.setAdapter(new SPAdapter<String>(EditOtherBookingActivity.this, paxList) {
            @Override
            protected String setSpinnerText(String item) {
                return item;
            }
        });

        sp_pax.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(EditOtherBookingActivity.this);
            }
        });
      /*  rd_arrived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rd_non_arrived.setChecked(false);
                }
            }
        });

        rd_non_arrived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rd_arrived.setChecked(false);
                }
            }
        });*/

        rd_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("NA")) {
                    new AlertDialog.Builder(EditOtherBookingActivity.this)
                            .setMessage("Status once selected cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (!status.equals("A"))
                    new AlertDialog.Builder(EditOtherBookingActivity.this)
                            .setMessage("Are you sure want to change the status because later you won't be able to change status.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isArrived = true;
                                    status = "A";
                                    edt_spend.setVisibility(View.VISIBLE);
                                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
            }
        });

        rd_non_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("A")) {
                    new AlertDialog.Builder(EditOtherBookingActivity.this)
                            .setMessage("Status once selected cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (!status.equals("NA")) {
                    new AlertDialog.Builder(EditOtherBookingActivity.this)
                            .setMessage("Are you sure want to change the status because later you won't be able to change status.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isArrived = true;
                                    status = "NA";
                                    edt_spend.setVisibility(View.VISIBLE);
                                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        sp_time.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_services.setText(timeList.get(position).getType());
            }
        });
       /* sp_services.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0) {
//                    sp_time.setItems(getResources().getStringArray(R.array.select_first_services));
                    sp_time.setItems(firstServicetime);
                } else {
                    sp_time.setItems(secondServicetime);
                }
            }
        });
*/
        edt_spend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_spend.removeTextChangedListener(this);

                String value = edt_spend.getText().toString();


                if (!value.equals("")) {

                    if (value.startsWith(".")) {
                        edt_spend.setText("0.");
                    }
                    if (value.startsWith("0") && !value.startsWith("0.")) {
                        edt_spend.setText("");
                    } else {


                        String str = edt_spend.getText().toString().replaceAll(",", "");
                        edt_spend.setText(NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(str)));
                        edt_spend.setSelection(edt_spend.getText().toString().length());
                    }

                }
                edt_spend.addTextChangedListener(this);
            }
        });
    }

    private void getTimeSlot() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "venue/timing/" + SessionManager.getVenueID());
        communication.callGET(param);
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
        rv_table_option.setLayoutManager(new LinearLayoutManager(EditOtherBookingActivity.this));
        sp_group_type = findViewById(R.id.sp_group_type);
        ln_table = findViewById(R.id.ln_table);
        rd_non_arrived = findViewById(R.id.rd_non_arrived);
        rd_arrived = findViewById(R.id.rd_arrived);
        edt_first_name = findViewById(R.id.edt_first_name);
        edt_last_name = findViewById(R.id.edt_last_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        ln_sub_service = findViewById(R.id.ln_sub_service);
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
        Log.e("VENUE TYPE::", SessionManager.getVenue_Type());
        if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
            ln_table.setVisibility(View.VISIBLE);
            ln_services.setVisibility(View.GONE);
            sp_group_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (!sp_pax.getText().toString().isEmpty()) {
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
                    if (!sp_group_type.getText().toString().isEmpty()) {
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
           /* if (SessionManager.getVenue_Type().equals(VenueTypes.ROOF_TOP_BAR.toString()) || SessionManager.getVenue_Type().equals("Shisha Lounge")|| SessionManager.getVenue_Type().equals("Restaurant")) {
            }*/
            ln_sub_service.setVisibility(View.GONE);
            ln_table.setVisibility(View.GONE);

        }
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("vendor/other/booking/detail/%s", id))) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                booking_id = data.getString("id");
                edt_first_name.setText(data.getString("first_name"));
                edt_last_name.setText(data.getString("last_name"));
                edt_email.setText(data.getString("email"));
                edt_mobile.setText(data.getString("phone_no"));
                sp_pax.setText(data.getString("pax"));
                table_name = data.getString("table_name");
                service = data.getString("service");

                SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                date = data.getString("booking_date");
                tv_date.setText(showFormat.format(finalFormate.parse(data.getString("booking_date"))));
                sp_c_name.setText(data.getString("concierge_name"));
                sp_group_type.setText(data.getString("group_type"));
                time = data.getString("booking_time");
                sp_c_name.setTag(data.getString("concierge_id"));
                sp_c_email.setText(data.getString("concierge_email"));
                edt_spend.setText(data.getString("total_spend").equals("0") ? "" : data.getString("total_spend"));
                edt_table_no.setText(data.getString("table_no"));
                edt_desc.setText(data.getString("description"));
                status = data.getString("status");
                rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                if (status.equalsIgnoreCase("A")) {
//                    rd_non_arrived.setEnabled(true);
                    isArrived = true;
                    edt_spend.setVisibility(View.VISIBLE);
//                    rd_arrived.setEnabled(true);
                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
//                    rd_arrived.setEnabled(true);
                } else if (data.getString("status").equalsIgnoreCase("NA")) {
                    isArrived = false;
                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
//                    rd_non_arrived.setEnabled(false);
                }
                getTimeSlot();

                HashMap<String, String> param = new HashMap<>();
                param.put("action", "venue/tableOptions");
                param.put("venue_id", SessionManager.getVenueID());
                param.put("group_type", sp_group_type.getText().toString());
                param.put("group_size", sp_pax.getText().toString());
                communication.callPOST(param);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("vendor/edit/other/booking")) {
            try {
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(EditOtherBookingActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
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
                    if (optionModel.getTable_name().equalsIgnoreCase(table_name)) {
                        optionModel.setSelect(true);
                    }
                    list.add(optionModel);
                }
                tableAdapter = new TableOptionSelectAdapter(EditOtherBookingActivity.this, list);
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
                for (int i = 0; i < time_slots.length(); i++) {
                    timeList.add(new TimeModel(time_slots.getJSONObject(i).getString("time"), time_slots.getJSONObject(i).getString("type")));
                }
                if (timings.getString("is_sitting").equals("1")) {


                    sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(EditOtherBookingActivity.this, timeList) {
                        @Override
                        protected String setSpinnerText(TimeModel item) {
                            return item.getTime();
                        }
                    });


                    sp_time.setSelectedIndex(timeList.indexOf(new TimeModel(time)));

                } else {
                    if (SessionManager.getDepositOption().equalsIgnoreCase("percentage")) {
                        fixTime = timeList.get(0).getTime();
                    } else {

                        sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(EditOtherBookingActivity.this, timeList) {
                            @Override
                            protected String setSpinnerText(TimeModel item) {
                                return item.getTime();
                            }
                        });

                        sp_time.setSelectedIndex(timeList.indexOf(new TimeModel(time)));

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
        if(tag.equalsIgnoreCase("vendor/edit/other/booking")){
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
                    Toast.makeText(EditOtherBookingActivity.this, "Select Group Size", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(tv_date)) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_services.getText().toString().isEmpty()) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Service", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_time.getText().toString().isEmpty()) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                } else if (sp_pax.getText().toString().isEmpty()) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Group Size", Toast.LENGTH_SHORT).show();
                } else if (sp_group_type.getText().toString().isEmpty()) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Group Type", Toast.LENGTH_SHORT).show();
                } else if (ln_table.getVisibility() == View.VISIBLE && !isSelected) {
                    Toast.makeText(EditOtherBookingActivity.this, "Select Table", Toast.LENGTH_SHORT).show();
                } else if (sp_c_name.getTag() == null && Validator.isNotEmail(edt_email)) {
                    Toast.makeText(EditOtherBookingActivity.this, "Enter Email Or Enter Concierge Name", Toast.LENGTH_SHORT).show();
                }  else {
                    try {

                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "vendor/edit/other/booking");
                        param.put("booking_id", booking_id);
                        param.put("group_type", sp_group_type.getText().toString());
                        param.put("venue_type", SessionManager.getVenue_Type());
                        param.put("vendor_id", SessionManager.getID());
                        param.put("concierge_id",sp_c_name.getTag()==null?"": sp_c_name.getTag().toString());
                        param.put("first_name", Validator.getText(edt_first_name));
                        param.put("last_name", Validator.getText(edt_last_name));
                        param.put("venue_id", SessionManager.getVenueID());
                        param.put("email", Validator.getText(edt_email));
                        param.put("phone_no", Validator.getText(edt_mobile));
                        param.put("pax", Validator.getText(sp_pax));
                        SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                        SimpleDateFormat finalFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        param.put("booking_date", date/*finalFormate.format(Validator.getDate(showFormat.parse(Validator.getText(tv_date))))*/);
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
                        param.put("table_spend", Validator.getText(edt_spend));
                        param.put("table_no", Validator.getText(edt_table_no));
                        param.put("description", Validator.getText(edt_desc));
                        param.put("status", status);
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
               /* DatePickerDialog datePickerDialog = new DatePickerDialog(EditOtherBookingActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTime().getTime());
                datePickerDialog.show();*/
            }
            break;
        }
    }
}
