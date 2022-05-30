package com.vip.marrakech.admin.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminItineraryDayListActivity;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.admin.activities.AdminNotificationActivity;
import com.vip.marrakech.admin.activities.AdminPromotionLIst;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.models.UserModel;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.dialogs.SearchEmailDialog;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.MyDate;

import org.json.JSONArray;
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
import java.util.Objects;

public class AdminAddItineraryFragment extends Fragment implements OnCallBackListener, AdminMainActivity.OnFragmentViewPagerChangeListener, View.OnClickListener, TextWatcher, Toolbar.OnMenuItemClickListener, View.OnFocusChangeListener {

    private Communication communication;
    private EditText edt_user_name,
            edt_mobile;
    private TextView arrival_date, dep_date;
    private MaterialSpinner grp_type, sp_pax;
    private Button btn_next;
    private Toolbar toolBar;
    private TextView toolbar_title,edt_email;
    private ImageView iv_notification;
    private TextView tv_notification_count;
    private String encryptedId;
    private String arrivalDate;
    private String depatureDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_add_itinerary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);
        edt_user_name.addTextChangedListener(this);
        arrival_date.setOnClickListener(this);
        dep_date.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        grp_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
            }
        });

        sp_pax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
            }
        });

        grp_type.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(getActivity());
            }
        });
        sp_pax.setOnExpandListener(new MaterialSpinner.OnExpandListener() {
            @Override
            public void onExpand() {
                hideKeyboard(getActivity());
            }
        });
        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        ImageView gift = (ImageView) toolBar.getMenu().findItem(R.id.opt_gift).getActionView();
        ImageView recomndation = (ImageView) toolBar.getMenu().findItem(R.id.opt_recommandation).getActionView();
        notification.setOnClickListener(customMenuListener);
        gift.setOnClickListener(customMenuListener);
        recomndation.setOnClickListener(customMenuListener);


        edt_user_name.setOnFocusChangeListener(this);
        edt_email.setOnFocusChangeListener(this);
        edt_mobile.setOnFocusChangeListener(this);


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
                emailDialog.show(getChildFragmentManager(),"emails");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    private View.OnClickListener customMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMenuItemClick(toolBar.getMenu().findItem(v.getId()));
        }
    };

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

    private void initUI(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        toolbar_title = toolBar.findViewById(R.id.toolbar_title);
        toolBar.setOnMenuItemClickListener(this);
        edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_mobile = view.findViewById(R.id.edt_mobile);
        arrival_date = view.findViewById(R.id.tv_arrival_date);
        dep_date = view.findViewById(R.id.tv_dep_date);
        grp_type = view.findViewById(R.id.sp_grp_type);
        btn_next = view.findViewById(R.id.btn_next);
        grp_type.setItems("Group Type", "Family", "Males", "Females", "Couples");
       /* grp_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                
            }
        });*/

        sp_pax = view.findViewById(R.id.sp_pax);
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

            param.put("client_name", edt_user_name.getText().toString());
            param.put("email", edt_email.getText().toString());
            param.put("contact_number", edt_mobile.getText().toString());
            param.put("group", grp_type.getText().toString());
            param.put("pax", sp_pax.getText().toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
            param.put("arrival_date", arrivalDate);
            param.put("departure_date", depatureDate);
            param.put("status", "N");
            if (encryptedId != null) {
                param.put("action", "itinerary/update");
                param.put("itinerary_id", encryptedId);
            } else {
                param.put("action", "itinerary/add");
            }
            communication.callPOST(param);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        try {
            if (tag.equals("itinerary/add")) {
               /* listener.AddItinerarySuccess();
                edt_user_name.setText("");
                edt_email.setText("");
                edt_mobile.setText("");
                arrival_date.setText("");
                dep_date.setText("");
                grp_type.setSelectedIndex(0);
                sp_pax.setSelectedIndex(0);*/
                encryptedId = jsonObject.getString("encryptedId");
                Bundle bundle = new Bundle();
                bundle.putString("id", encryptedId);
                GoTo.startWithExtra(getActivity(), AdminItineraryDayListActivity.class, bundle);


            } else if (tag.equals("itinerary/update")) {

                Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("id", encryptedId);
                GoTo.startWithExtra(getActivity(), AdminItineraryDayListActivity.class, bundle);
            } else if (tag.contains("search/client/")) {

                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray client = data.getJSONArray("client");
                List<UserModel> userList = new ArrayList<>();
                for (int i = 0; i < client.length(); i++) {
                    UserModel model = new Gson().fromJson(client.getJSONObject(i).toString(), UserModel.class);
                    userList.add(model);
                }
               /* edt_user_name.setAdapter(new AutoCompleteAdapter(getActivity(), userList, new AutoCompleteAdapter.OnAutoSelectListener() {
                    @Override
                    public void onSelectListener(UserModel data, int pos) {
                        edt_user_name.clearListSelection();
                        edt_user_name.setText(data.getName());
                        edt_user_name.setTag(data.getId());
                        edt_user_name.performCompletion();
                        edt_mobile.setText(data.getContactNumber());
                        edt_email.setText(data.getEmail());
                        edt_user_name.setSelection(edt_user_name.getText().length());
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edt_user_name.getWindowToken(), 0);
                        edt_user_name.clearFocus();
                        edt_user_name.dismissDropDown();
                        edt_user_name.post(new Runnable() {
                            public void run() {
                                edt_user_name.dismissDropDown();
                            }
                        });

                    }
                }));
                edt_user_name.showDropDown();*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }

        if (b) {
            edt_user_name.setText("");
            edt_email.setText("");
            edt_mobile.setText("");
            arrival_date.setText("");
            dep_date.setText("");
            grp_type.setText("");
            sp_pax.setText("");
            grp_type.setSelectedIndex(0);
            sp_pax.setSelectedIndex(0);
            encryptedId = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_arrival_date: {

                hideKeyboard(getActivity());

           /*     Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DecimalFormat formatter = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                String d = String.format(Locale.ENGLISH, "%s-%s-%d", formatter.format(day), formatter.format(month + 1), year);
                                SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    arrivalDate = yyyy_mm_dd.format(format.parse(d));
                                    arrival_date.setText(pickerformat.format(Validator.getDate(format.parse(d))));
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
                dialog.show(getChildFragmentManager(),"date");
            }
            break;
            case R.id.tv_dep_date: {
                hideKeyboard(getActivity());
                if (arrival_date.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Arrival Date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DecimalFormat formatter = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
                                String d = String.format(Locale.ENGLISH, "%s-%s-%d", formatter.format(day), formatter.format(month + 1), year);
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
                dialog.show(getChildFragmentManager(),"date");
            }
            break;


            case R.id.btn_next: {
                if (Validator.isEmpty(edt_user_name)) {
                    Validator.setError(edt_user_name, "Enter Client Name");
                } else if (Validator.isEmpty(edt_email)) {
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (Validator.isNotEmail(edt_email)) {
                    Toast.makeText(getActivity(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (Validator.isEmpty(edt_mobile)) {
                    Validator.setError(edt_mobile, "Enter Contact Number");
                } else if (Validator.isNotMinLength(edt_mobile, 11)) {
                    Validator.setError(edt_mobile, "Enter Valid Contact Number");
                } else if (grp_type.getSelectedIndex() == 0) {
                    Toast.makeText(getActivity(), "Select Group Type", Toast.LENGTH_SHORT).show();
                } else if (sp_pax.getSelectedIndex() == 0) {
                    Toast.makeText(getActivity(), "Select Group Size", Toast.LENGTH_SHORT).show();
                }else if (arrival_date.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Arrival Date", Toast.LENGTH_SHORT).show();
                } else if (dep_date.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Departure Date", Toast.LENGTH_SHORT).show();
                } else {
                    addItinerary();

                }
            }
            break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
      /*  if (!edt_user_name.isPerformingCompletion()) {
            if (editable.length() > 0) {
                communication.cancelAll();
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "search/client/" + editable.toString());
                communication.callGET(param);
            }
        }
*/
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(getActivity(), AdminRecommandationActivity.class);
        } else if (item.getItemId() == R.id.opt_gift) {
            GoTo.start(getActivity(), AdminPromotionLIst.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), AdminNotificationActivity.class);
        }
        return false;
    }

    private OnAddItineraryListener listener;

    public void setListener(OnAddItineraryListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
    }

    public interface OnAddItineraryListener {
        void AddItinerarySuccess();
    }
}
