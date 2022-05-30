package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminItineraryAdapter;
import com.vip.marrakech.admin.models.AdminItineraryModel;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.dialogs.SearchEmailDialog;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.MyDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CopyIteniraryActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, OnCallBackListener, View.OnClickListener {

    private Communication communication;
    private EditText edt_user_name,
            edt_mobile;
    private TextView arrival_date, dep_date;
    private MaterialSpinner grp_type, sp_pax;
    private Button btn_next;
    private Toolbar toolBar;
    private TextView toolbar_title, edt_email;
    private ImageView iv_notification;
    private TextView tv_notification_count;
    private AdminItineraryModel model;
    private String notification_count;
    private String encryptedId;
    private String arrivalDate, depatureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_itenirary);
        Bundle bundle = GoTo.getIntent(this);
        model = (AdminItineraryModel) bundle.getSerializable("data");
        communication = new Communication(CopyIteniraryActivity.this, this);
        initUI();
        arrival_date.setOnClickListener(this);
        dep_date.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        grp_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CopyIteniraryActivity.this);
            }
        });

        sp_pax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CopyIteniraryActivity.this);
            }
        });

        grp_type.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(CopyIteniraryActivity.this);
            }
        });
        sp_pax.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(CopyIteniraryActivity.this);
            }
        });

     /*   RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        ImageView gift = (ImageView) toolBar.getMenu().findItem(R.id.opt_gift).getActionView();
        ImageView recomndation = (ImageView) toolBar.getMenu().findItem(R.id.opt_recommandation).getActionView();
        notification.setOnClickListener(customMenuListener);
        gift.setOnClickListener(customMenuListener);
        recomndation.setOnClickListener(customMenuListener);*/

        if (model != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
            try {
                arrivalDate = model.getArrivalDate();
                depatureDate = model.getDepartureDate();
                arrival_date.setText(pickerformat.format(format.parse(model.getArrivalDate())));
                dep_date.setText(pickerformat.format(format.parse(model.getDepartureDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            grp_type.setText(model.getGroup());
            sp_pax.setText(model.getPax());
        }


        edt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEmailDialog emailDialog = new SearchEmailDialog();
                emailDialog.setListener(new SearchEmailDialog.OnSearchListener() {
                    @Override
                    public void onEmailSelect(String email) {
                        edt_email.setText(email);
                    }
                });
                emailDialog.show(getSupportFragmentManager(), "emails");
            }
        });
    }

    private View.OnClickListener customMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMenuItemClick(toolBar.getMenu().findItem(v.getId()));
        }
    };

    public void hideKeyboard(Activity activity) {
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
        toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.copy_itinerary));
        toolBar.setOnMenuItemClickListener(this);
        edt_user_name = findViewById(R.id.edt_user_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        arrival_date = findViewById(R.id.tv_arrival_date);
        dep_date = findViewById(R.id.tv_dep_date);
        grp_type = findViewById(R.id.sp_grp_type);
        btn_next = findViewById(R.id.btn_next);
        grp_type.setItems("Group Type", "Family", "Males", "Females", "Couples");
       /* grp_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });*/

        sp_pax = findViewById(R.id.sp_pax);
        List<String> paxList = new ArrayList<>();
        paxList.add("Group Size");
        for (int i = 1; i <= 30; i++) {
            paxList.add(String.valueOf(i));
        }
        sp_pax.setItems(paxList);
       /* sp_pax.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });*/
    }


    private void addItinerary() {

        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "itinerary/copy");
            param.put("copy_itinerary_id", model.getId());
            param.put("client_name", edt_user_name.getText().toString());
            param.put("email", edt_email.getText().toString());
            param.put("contact_number", edt_mobile.getText().toString());
            param.put("group", grp_type.getText().toString());
            param.put("pax", sp_pax.getText().toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
            param.put("arrival_date", arrivalDate /*format.format(Validator.getDate(pickerformat.parse(Validator.getText(arrival_date))))*/);
            param.put("departure_date", depatureDate/*format.format(Validator.getDate(pickerformat.parse(Validator.getText(dep_date))))*/);
            // param.put("status", "N");
            if (encryptedId != null) {
                param.put("action", "itinerary/update");
                param.put("itinerary_id", encryptedId);
            } else {
                param.put("action", "itinerary/copy");
            }
            communication.callPOST(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_arrival_date: {

                hideKeyboard(CopyIteniraryActivity.this);
/*
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CopyIteniraryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DecimalFormat formatter = new DecimalFormat("00");
                                String d = String.format(Locale.ENGLISH, "%s-%s-%d", formatter.format(day), formatter.format(month + 1), year);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {

                                    arrivalDate = yyyy_mm_dd.format(format.parse(d));
                                    arrival_date.setText(pickerformat.format(format.parse(d)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                try {
                    SimpleDateFormat tf = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);

                    String time = dep_date.getText().toString();
                    if (!time.isEmpty()) {
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date parseTime =yyyy_mm_dd.parse(depatureDate);
                        datePickerDialog.getDatePicker().setMaxDate(parseTime.getTime());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();*/
                CalenderDialog dialog = new CalenderDialog();
                // dialog.setMinDate(new Date());
                try {

                    String time = dep_date.getText().toString();
                    if (!time.isEmpty()) {
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date parseTime =yyyy_mm_dd.parse(depatureDate);
                        assert parseTime != null;
                        dialog.setMaxDate(parseTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.setListener(new OnCalenderListener() {
                    @Override
                    public void onDateSelect(MyDate mydate) {
                        SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        arrivalDate = yyyy_mm_dd.format(mydate.getDate());
                        arrival_date.setText(pickerformat.format(mydate.getDate()));
                    }
                });
                dialog.show(getSupportFragmentManager(),"date");
            }
            break;
            case R.id.tv_dep_date: {
                hideKeyboard(CopyIteniraryActivity.this);
                if (arrival_date.getText().toString().isEmpty()) {
                    Toast.makeText(CopyIteniraryActivity.this, "Select Arrival Date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CopyIteniraryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DecimalFormat formatter = new DecimalFormat("00");
                                String d = String.format(Locale.ENGLISH, "%s-%s-%d", formatter.format(day), formatter.format(month + 1), year);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    depatureDate = yyyy_mm_dd.format(format.parse(d));
                                    dep_date.setText(pickerformat.format(format.parse(d)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                try {
                    SimpleDateFormat tf = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                    String time = arrival_date.getText().toString();
                    if (!time.isEmpty()) {
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date parseTime =yyyy_mm_dd.parse(arrivalDate);
                        datePickerDialog.getDatePicker().setMinDate(parseTime.getTime());
                    } else {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();*/
                CalenderDialog dialog = new CalenderDialog();
                try {
                    String time = arrival_date.getText().toString();
                    if (!time.isEmpty()) {
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date parseTime =yyyy_mm_dd.parse(arrivalDate);
                        dialog.setMinDate(parseTime);
                    } else {
                        dialog.setMinDate(new Date());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.setListener(new OnCalenderListener() {
                    @Override
                    public void onDateSelect(MyDate mydate) {
                        SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        depatureDate = yyyy_mm_dd.format(mydate.getDate());
                        dep_date.setText(pickerformat.format(mydate.getDate()));
                    }
                });
                dialog.show(getSupportFragmentManager(),"date");
            }
            break;


            case R.id.btn_next: {
                if (Validator.isEmpty(edt_user_name)) {
                    Validator.setError(edt_user_name, "Enter Client Name");
                } else if (Validator.isEmpty(edt_email)) {
                    Validator.setError(edt_email, "Enter Email");
                } else if (Validator.isEmpty(edt_mobile)) {
                    Validator.setError(edt_mobile, "Enter Contact Number");
                } else if (Validator.isNotMinLength(edt_mobile, 11)) {
                    Validator.setError(edt_mobile, "Enter Valid Contact Number");
                } else if (grp_type.getSelectedIndex() == 0) {
                    Toast.makeText(CopyIteniraryActivity.this, "Select Group Type", Toast.LENGTH_SHORT).show();
                } else if (sp_pax.getSelectedIndex() == 0) {
                    Toast.makeText(CopyIteniraryActivity.this, "Select Group Size", Toast.LENGTH_SHORT).show();
                } else {
                    addItinerary();
                }
            }
            break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(CopyIteniraryActivity.this, AdminRecommandationActivity.class);
        } else if (item.getItemId() == R.id.opt_gift) {
            GoTo.start(CopyIteniraryActivity.this, AdminPromotionLIst.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(CopyIteniraryActivity.this, AdminNotificationActivity.class);
        }
        return false;
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("notification/counts")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                notification_count = data.getString("notification_count");
                if (tv_notification_count != null && !notification_count.equals("0")) {
                    tv_notification_count.setVisibility(View.VISIBLE);
                    tv_notification_count.setText(notification_count);
                } else if (tv_notification_count != null) {
                    tv_notification_count.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("itinerary/copy")) {

            encryptedId = jsonObject.optString("encryptedId");
            Bundle bundle = new Bundle();
            bundle.putString("id", encryptedId);
            GoTo.startWithExtra(CopyIteniraryActivity.this, AdminItineraryDayListActivity.class, bundle);
        } else if (tag.equals("itinerary/update")) {

            Toast.makeText(CopyIteniraryActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("id", encryptedId);
            GoTo.startWithExtra(CopyIteniraryActivity.this, AdminItineraryDayListActivity.class, bundle);
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    protected void onResume() {
        getNotificationCount();
        super.onResume();
    }

    private void getNotificationCount() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "notification/counts");
        param.put("type", "Admin");
        param.put("user_id", SessionManager.getEncryptedID());
        communication.callPOST(param);
    }
}
