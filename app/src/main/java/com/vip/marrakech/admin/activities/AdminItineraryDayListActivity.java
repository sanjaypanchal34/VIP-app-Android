package com.vip.marrakech.admin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminItineraryDayListAdapter;
import com.vip.marrakech.admin.adapters.itineraryAdapters.IntineryDateWiseAdapter;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.models.ItineryDetail.ItineriDetailModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminItineraryDayListActivity extends AppCompatActivity implements AdminItineraryDayListAdapter.OnItineraryDayListener, OnCallBackListener {

    private RecyclerView rv_day_list;
    private List<GetItineraryDayDetail> list = new ArrayList<>();
    private AdminItineraryDayListAdapter adapter;
    private Bundle bundle;
    private String id;
    private Communication communication;
    private ItineriDetailModel detailModel;
    private Button btn_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_itinerary_day_list);
        communication = new Communication(this, this);
        bundle = GoTo.getIntent(this);
        if (bundle != null) {
            if (bundle.getSerializable("id") != null) {
                id = bundle.getString("id");
                Toolbar toolBar = findViewById(R.id.toolBar);
                toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.opt_add) {


                            GetItineraryDayDetail detail = new GetItineraryDayDetail();
                            DayDetail dayDetail = new DayDetail();

//                                    dayDetail.setDate(format.format(pickerformat.parse(detailModel.getArrivalDate())));
                            dayDetail.setDate("");
                            dayDetail.setVenueId("0001");
                            dayDetail.setGetItineraryDayBottleDetail(new ArrayList<GetItineraryDayBottleDetail>());
                            dayDetail.setDayNo(String.valueOf(list.size() + 1));
                            List<DayDetail> dayDetailList = new ArrayList<>();
                            dayDetailList.add(dayDetail);
                            detail.setDayDetails(dayDetailList);
                            list.add(detail);
                            adapter.notifyDataSetChanged();



                        }
                        return false;
                    }
                });

                initUI();


                btn_preview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",id);
                        GoTo.startWithExtra(AdminItineraryDayListActivity.this,AdminItineraryPreviewActivity.class,bundle);
                    }

                });



            }
        }
    }

    public Date incrementDateByOne(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private void getItineraryDetail() {

        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("itinerary/%s", id));
        communication.callGET(param);
    }

    private void initUI() {
        btn_preview = findViewById(R.id.btn_preview);
        rv_day_list = findViewById(R.id.rv_day_list);
        rv_day_list.setLayoutManager(new LinearLayoutManager(AdminItineraryDayListActivity.this));
        adapter = new AdminItineraryDayListAdapter(AdminItineraryDayListActivity.this, list) {
            @Override
            protected IntineryDateWiseAdapter setSubAdapter(GetItineraryDayDetail model) {
                return new IntineryDateWiseAdapter(context, model.getDayDetails().get(0).getVenueId().equals("0001")?new ArrayList<>():model.getDayDetails(), new IntineryDateWiseAdapter.OnSubDayListener() {
                    @Override
                    public void onSubClick() {

                    }

                    @Override
                    public void onSubDelete(DayDetail dayDetail) {
                        new AlertDialog.Builder(AdminItineraryDayListActivity.this)
                                .setMessage("Are you sure you want to delete this Venue data?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        HashMap<String,String> param = new HashMap<>();
                                        param.put("action","day/delete/"+dayDetail.getEncrypted_day_id());
                                        communication.callDelete(param);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();

                    }

                    @Override
                    public void onSubEdit(DayDetail dayDetail) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", dayDetail);
                        bundle.putSerializable("id", id);
                        bundle.putSerializable("group", detailModel.getGroup());
                        bundle.putSerializable("pax", detailModel.getPax());
                        GoTo.startWithExtra(AdminItineraryDayListActivity.this, EditAdminItineraryDayDataActivity.class, bundle);
                    }
                });
            }
        };
        adapter.setListener(this);
        rv_day_list.setAdapter(adapter);
    }

    @Override
    public void onGoClick(GetItineraryDayDetail model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putSerializable("id", id);
        bundle.putSerializable("group", detailModel.getGroup());
        bundle.putSerializable("pax", detailModel.getPax());
        GoTo.startWithExtra(AdminItineraryDayListActivity.this, AdminItineraryDayDataActivity.class, bundle);
    }

    @Override
    public void onDateClick(final GetItineraryDayDetail model) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminItineraryDayListActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        String date = String.format(Locale.ENGLISH, "%s-%s-%d", formatter.format(day), formatter.format(month + 1), year);
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat pickerformat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        boolean isAdded = false;
                        try {
                            for (GetItineraryDayDetail dayDetail :
                                    list) {
                                if (dayDetail.getDayDetails().get(0).getDate().equals(format.format(pickerformat.parse(date)))) {
                                    isAdded = true;
                                }
                            }

                            if (!isAdded) {

                                for (DayDetail detail :
                                        model.getDayDetails()) {
                                    detail.setDate(format.format(pickerformat.parse(date)));
                                    detail.setStartDateTime(detail.getTime().isEmpty()?date+" 00:00":date+" "+detail.getTime());
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(AdminItineraryDayListActivity.this, "Date Already Added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, dayOfMonth);
        try {
            SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String minDate = detailModel.getArrivalDate();
            String maxDate = detailModel.getDepartureDate();
            if (!minDate.isEmpty()) {
                Date minTime = tf.parse(minDate);
                Date maxTime = tf.parse(maxDate);
                datePickerDialog.getDatePicker().setMinDate(minTime.getTime());
                datePickerDialog.getDatePicker().setMaxDate(maxTime.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // datePickerDialog.show();
    }

    @Override
    public void onDeleteClick(final GetItineraryDayDetail model, int adapterPosition) {

        new AlertDialog.Builder(AdminItineraryDayListActivity.this)
                .setMessage("Are you sure you want to delete this date data?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("action", "itinerary/day/delete");
                        param.put("itinerary_id", id);
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat pickerformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        try {
                            param.put("day_date", pickerformat.format(format.parse(model.getDayDetails().get(0).getDate())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        communication.callPOST(param);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
      /*  if (!model.getDayDetails().get(0).getDate().isEmpty()){
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "itinerary/day/delete");
            param.put("itinerary_id", id);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
            SimpleDateFormat pickerformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                param.put("day_date", pickerformat.format(format.parse(model.getDayDetails().get(0).getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            communication.callPOST(param);
        }else {
            list.remove(adapterPosition);
            adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("itinerary/%s", id))) {
            try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    detailModel = new Gson().fromJson(data.toString(), ItineriDetailModel.class);
                    list.clear();
//                    list.addAll(detailModel.getGetItineraryDayDetail());
                    HashMap<String,GetItineraryDayDetail> stringListHashMap = new HashMap<>();
                    for (GetItineraryDayDetail detail:detailModel.getGetItineraryDayDetail()){
                        String date  = detail.getDayDetails().get(0).getDate();
                        if (!stringListHashMap.containsKey(date)){
                            stringListHashMap.put(date,detail);
                        }
                    }

                    setDates(stringListHashMap);

                    adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("itinerary/day/delete")) {
            Toast.makeText(AdminItineraryDayListActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            detailModel.getGetItineraryDayDetail().clear();
            list.clear();
            adapter.notifyDataSetChanged();
            getItineraryDetail();
        }else if (tag.contains("day/delete/")) {
            Toast.makeText(AdminItineraryDayListActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            detailModel.getGetItineraryDayDetail().clear();
            list.clear();
            adapter.notifyDataSetChanged();
            getItineraryDetail();
        }
    }

    private void setDates(HashMap<String, GetItineraryDayDetail> stringListHashMap) {

        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String minDate = detailModel.getArrivalDate();
        String maxDate = detailModel.getDepartureDate();
        if (!minDate.isEmpty()) {
            try {
                Date minTime  = tf.parse(minDate);
                Date maxTime = tf.parse(maxDate);
                List<String>futureDate =  getDates(minTime, maxTime);
                for (String date :
                        getDates(minTime, maxTime)) {


                   /* for (GetItineraryDayDetail day_detail :
                            list) {
                        Log.e(day_detail.getDayDetails().get(0).getDate(),date);
                        if (day_detail.getDayDetails().get(0).getDate().equals(date)) {
                           futureDate.remove(date);
                        }
                    }*/
                    Log.e("DATE::::::>>>",date);
                   if (stringListHashMap.containsKey(date)){
                       Log.e("Constain::::::>>>",date);
                       list.add(stringListHashMap.get(date));
                       futureDate.remove(date);
                   }
                }
                for (String dt:
                        futureDate) {
                    Log.e("DATE::;",dt);
                    GetItineraryDayDetail detail = new GetItineraryDayDetail();
                    DayDetail dayDetail = new DayDetail();
                    dayDetail.setDate(dt);
                    dayDetail.setVenueId("0001");
                    dayDetail.setGetItineraryDayBottleDetail(new ArrayList<GetItineraryDayBottleDetail>());
                    dayDetail.setDayNo(String.valueOf(list.size() + 1));
                    List<DayDetail> dayDetailList = new ArrayList<>();
                    dayDetailList.add(dayDetail);
                    detail.setDayDetails(dayDetailList);
                    list.add(detail);
                }
                Collections.sort(list, new Comparator<GetItineraryDayDetail>() {
                    @Override
                    public int compare(GetItineraryDayDetail o1, GetItineraryDayDetail o2) {
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                        try {
                            Date a = format.parse(o1.getDayDetails().get(0).getDate());
                            Date b = format.parse(o2.getDayDetails().get(0).getDate());
                           if (a.after(b)){
                               return 1;
                           }else if (a.before(b)){
                               return -1;
                           }else {
                               return 0;
                           }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                       return 0;
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    private static List<String> getDates(Date date1, Date date2)
    {
        ArrayList<String> dates = new ArrayList<String>();


        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
            dates.add(format.format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    protected void onResume() {
        getItineraryDetail();
        super.onResume();
    }
}
