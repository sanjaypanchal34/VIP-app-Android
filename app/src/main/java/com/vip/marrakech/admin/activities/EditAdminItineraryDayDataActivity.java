package com.vip.marrakech.admin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.CustomBaseAdapter;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.TimeModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.models.TableOptionModel;
import com.vip.marrakech.vendor.adapters.TableOptionSelectAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditAdminItineraryDayDataActivity extends AppCompatActivity implements OnCallBackListener {

    private Bundle bundle;
    private DayDetail model;

    private Button btn_save;
    private String itinerary_id;
    private String group;
    private String pax;
    private Communication communication;
    private MaterialSpinner sp_venue_type, sp_venue;
    private MaterialSpinner sp_services, sp_time;
    private LinearLayout ln_services, ln_sub_service, ln_table;
    private List<MasterVenueModel> venueList = new ArrayList<>();
    private List<TimeModel> timeList = new ArrayList<>();
    private String fixTime;
    private RecyclerView rv_table_option;
    private List<TableOptionModel> tableList = new ArrayList<>();
    private TableOptionSelectAdapter tableAdapter;
    private String venueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_itinerary_day_data);
        communication = new Communication(this, this);
        bundle = GoTo.getIntent(this);
        if (bundle != null) {
            if (bundle.getSerializable("model") != null) {
                model = (DayDetail) bundle.getSerializable("model");
                itinerary_id = bundle.getString("id");
                group = bundle.getString("group");
                pax = bundle.getString("pax");
                Toolbar toolBar = findViewById(R.id.toolBar);
                TextView toolbar_title = toolBar.findViewById(R.id.toolbar_title);
                toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                toolbar_title.setText(model.getDayNo());
                ((TextView) toolBar.findViewById(R.id.toolbar_title)).setText(model.getDayNo());

                initUI();
               /* sp_services.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        sp_time.setText("");
                        if (position == 0) {
                            sp_time.setItems(firstServicetime);
                        } else {
                            sp_time.setItems(secondServicetime);
                        }
                    }
                });*/


                sp_time.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        sp_services.setText(timeList.get(position).getType());
                    }
                });


                HashMap<String, String> param = new HashMap<>();
                param.put("action", "day/get/" + model.getEncrypted_day_id());
                communication.callGET(param);

            }
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = false;
                TableOptionModel selectedTable = null;
                for (TableOptionModel optionModel : tableList) {
                    if (optionModel.isSelect()) {
                        isSelected = true;
                        selectedTable = optionModel;
                    }
                }
                if (sp_venue_type.getText().toString().isEmpty()) {
                    Toast.makeText(EditAdminItineraryDayDataActivity.this, "Select Venue Type", Toast.LENGTH_SHORT).show();
                } else if (sp_venue.getText().toString().isEmpty()) {
                    Toast.makeText(EditAdminItineraryDayDataActivity.this, "Select Venue", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_services.getText().toString().isEmpty()) {
                    Toast.makeText(EditAdminItineraryDayDataActivity.this, "Select Service", Toast.LENGTH_SHORT).show();
                } else if (ln_services.getVisibility() == View.VISIBLE && sp_time.getText().toString().isEmpty()) {
                    Toast.makeText(EditAdminItineraryDayDataActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                } else if (ln_table.getVisibility() == View.VISIBLE && !isSelected) {
                    Toast.makeText(EditAdminItineraryDayDataActivity.this, "Select Table", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat pickerformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "itinerary/day/detail/update");
                    param.put("itinerary_id", itinerary_id);
                    param.put("itinerary_day_id", model.getEncrypted_day_id());
                    param.put("venue_id", venueID);
                    try {
                        param.put("date", pickerformat.format(format.parse(model.getDate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    param.put("venue_type", sp_venue_type.getText().toString());
                    if (ln_services.getVisibility() == View.VISIBLE) {
                        param.put("service", sp_services.getText().toString());
                        param.put("time", sp_time.getText().toString());
                    } else {
                        param.put("time", fixTime);
                        param.put("service", "First");

                    }

                    if (sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.DAY_PARTIES.toString()) || sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.NIGHTCLUB.toString()) || sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.EXPERIENCES.toString())) {
                        param.put("time_slot", selectedTable.getTable_name());
                        param.put("bottle_name", selectedTable.getAlcohol_name());
                        param.put("table_price", selectedTable.getPrice_in_MAD());
                    } else {
                        param.put("time_slot", sp_services.getText().toString().equals("First") ? "First Sitting" : "Second Sitting");
                    }
                    communication.callPOST(param);
                }
            }
        });

        sp_venue_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                tableList.clear();
                if (tableAdapter != null) {
                    tableAdapter.notifyDataSetChanged();
                }
                if (!model.getVenueType().equals(item)) {
                    model.setVenueId("");
                    model.setVenue_title("");
                    model.setTime("");
                }
                sp_venue.setText("");
                sp_services.setText("");
                sp_venue.setItems(new ArrayList<>());
                sp_services.setItems(new ArrayList<>());
                sp_time.setItems(new ArrayList<>());
                sp_time.setText("");
                if (item.equals(VenueTypes.DAY_PARTIES.toString()) || item.equals(VenueTypes.NIGHTCLUB.toString())|| item.equals(VenueTypes.EXPERIENCES.toString())) {
                    ln_services.setVisibility(View.GONE);
                    ln_table.setVisibility(View.VISIBLE);
                } else {
                    ln_table.setVisibility(View.GONE);
                    ln_services.setVisibility(View.VISIBLE);
                    ln_sub_service.setVisibility(View.GONE);

                }

                searchVenue(item);
            }
        });


        sp_venue.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (!item.equals(model.getVenue_title())) {
                    model.setVenueId("");
                    model.setVenue_title("");
                    model.setTime("");
                }
                venueID = venueList.get(position).getId();
                getTimeSlot(venueList.get(position).getId());
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "venue/tableOptions");
                param.put("venue_id", venueList.get(position).getId());
                param.put("group_type", group);
                param.put("group_size", pax);
                communication.callPOST(param);
            }
        });


    }

    private void getTimeSlot(String id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "venue/timing/" + id);
        communication.callGET(param);
    }

    private void searchVenue(Object item) {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "search/venue/" + item);
        communication.callGET(param);
    }


    private void initUI() {
        rv_table_option = findViewById(R.id.rv_table_option);
        rv_table_option.setLayoutManager(new LinearLayoutManager(EditAdminItineraryDayDataActivity.this));
        ln_table = findViewById(R.id.ln_table);
        btn_save = findViewById(R.id.btn_save);
        sp_services = findViewById(R.id.sp_services);
        sp_time = findViewById(R.id.sp_start);
        ln_services = findViewById(R.id.ln_services);
        ln_sub_service = findViewById(R.id.ln_sub_service);
        sp_venue_type = findViewById(R.id.sp_venue_type);
        sp_venue = findViewById(R.id.sp_venue);
        sp_venue_type.setItems(getResources().getStringArray(R.array.select_venue_type));

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("itinerary/day/detail/update")) {
            onBackPressed();
        } else if (tag.equals("day/get/" + model.getEncrypted_day_id())) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject day_detail = data.getJSONObject("day_detail");
                String service = day_detail.getString("service");
                model.setService(service);
                sp_venue_type.setSelectedIndex(Arrays.asList(getResources().getStringArray(R.array.select_venue_type)).indexOf(model.getVenueType()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.contains("search/venue/")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray venues = data.getJSONArray("venues");
                venueList.clear();
                for (int i = 0; i < venues.length(); i++) {
                    JSONObject venue = venues.getJSONObject(i);
                    MasterVenueModel model = new Gson().fromJson(venue.toString(), MasterVenueModel.class);
                    venueList.add(model);
                }
                sp_venue.setAdapter(new CustomBaseAdapter<MasterVenueModel>(EditAdminItineraryDayDataActivity.this, venueList) {
                    @Override
                    protected String setSpinnerText(MasterVenueModel item) {
                        return item.getTitle();
                    }
                });

                int pos = venueList.indexOf(new MasterVenueModel(model.getVenueId(), model.getVenue_title()));
                sp_venue.setSelectedIndex(pos);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.contains("venue/timing/")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject timings = data.getJSONObject("timings");
                timeList.clear();
                JSONArray time_slots = timings.getJSONArray("time_slots");
                for (int i = 0; i < time_slots.length(); i++) {
                    timeList.add(new TimeModel(time_slots.getJSONObject(i).getString("time"), time_slots.getJSONObject(i).getString("type")));
                }
                if (timings.getString("is_sitting").equals("1")) {


                    sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(EditAdminItineraryDayDataActivity.this, timeList) {
                        @Override
                        protected String setSpinnerText(TimeModel item) {
                            return item.getTime();
                        }
                    });


                    sp_time.setSelectedIndex(timeList.indexOf(new TimeModel(model.getTime())));

                } else {
                    if (sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.NIGHTCLUB.toString()) || sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.DAY_PARTIES.toString())|| sp_venue_type.getText().toString().equalsIgnoreCase(VenueTypes.EXPERIENCES.toString())) {
                        fixTime = timeList.get(0).getTime();
                    } else {

                        sp_time.setAdapter(new CustomBaseAdapter<TimeModel>(EditAdminItineraryDayDataActivity.this, timeList) {
                            @Override
                            protected String setSpinnerText(TimeModel item) {
                                return item.getTime();
                            }
                        });

                        sp_time.setSelectedIndex(timeList.indexOf(new TimeModel(model.getTime())));

                    }
                }
               /* if (timings.getString("is_sitting").equals("1")) {
                    sp_services.setItems(getResources().getStringArray(R.array.select_services));
                    firstServicetime = timings.getString("first").split(",");
                    secondServicetime = timings.getString("second").split(",");
                } else {
                    sp_services.setText(getResources().getStringArray(R.array.select_services)[0]);
                    sp_services.setHideArrow(true);
                    firstServicetime = timings.getString("first").split(",");
                    sp_time.setItems(firstServicetime);
                    if (sp_venue_type.getText().toString().equalsIgnoreCase("NightClub") || sp_venue_type.getText().toString().equalsIgnoreCase("Day Party")) {
                        fixTime = timings.getString("first");
                    }
                }*/
//                sp_services.setSelectedIndex(0);
//                sp_time.setItems(firstServicetime);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("venue/tableOptions")) {

            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray table_options = data.getJSONArray("table_options");
                tableList.clear();
                for (int i = 0; i < table_options.length(); i++) {
                    TableOptionModel optionModel = new Gson().fromJson(table_options.getJSONObject(i).toString(), TableOptionModel.class);
                    if (optionModel.getTable_name().equals(model.getTableName())) {
                        optionModel.setSelect(true);
                    }
                    tableList.add(optionModel);
                }
                tableAdapter = new TableOptionSelectAdapter(EditAdminItineraryDayDataActivity.this, tableList);
                rv_table_option.setAdapter(tableAdapter);
                tableAdapter.setListener(new TableOptionSelectAdapter.OnTableBookListener() {
                    @Override
                    public void onBookTable(TableOptionModel model) {
                        for (TableOptionModel optionModel : tableList) {
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
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
