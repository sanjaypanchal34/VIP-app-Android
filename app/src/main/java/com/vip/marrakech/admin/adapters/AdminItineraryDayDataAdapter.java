package com.vip.marrakech.admin.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.admin.models.MasterVenueModel;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdminItineraryDayDataAdapter extends GRecyclerAdapter<DayDetail, AdminItineraryDayDataAdapter.ViewHolder> {

    private List<String> venueType;
    private List<String> tableType = new ArrayList<>();
    private List<MasterVenueModel> venueList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    public AdminItineraryDayDataAdapter(Context context, List<DayDetail> list, List<String> venueTypeType) {
        super(context, list);
        tableType.clear();
        databaseHelper = new DatabaseHelper(context);
        venueType = venueTypeType;
//        venueType.addAll(Arrays.asList(context.getResources().getStringArray(R.array.select_venue_type)));
        tableType.addAll(Arrays.asList(context.getResources().getStringArray(R.array.select_table_type)));
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary_day_data, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<DayDetail> mList) {
        final DayDetail model = mList.get(i);
        viewHolder.tv_date.setText(model.getDate());
        viewHolder.tv_start.setText(model.getTime());
        // viewHolder.tv_start.setText(model.getTime().equals("00:00")?"":model.getTime());

        viewHolder.sp_venue_type.setSelection(venueType.indexOf(model.getVenueType()));
        viewHolder.sp_table_type.setSelection(tableType.indexOf(model.getTableName()));

        viewHolder.iv_add_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.sp_venue_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Select Venue Type", Toast.LENGTH_SHORT).show();
                } else if (viewHolder.sp_venue.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Select Venue", Toast.LENGTH_SHORT).show();
                } else if (viewHolder.sp_table_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Select Table", Toast.LENGTH_SHORT).show();
                } else {
                    model.getGetItineraryDayBottleDetail().add(new GetItineraryDayBottleDetail(databaseHelper.getBottleType(list.get(viewHolder.getAdapterPosition()).getVenueId()).get(0)));

                    System.out.println(model.getGetItineraryDayBottleDetail());
                    if (viewHolder.rv_bottle.getAdapter() != null) {
                        viewHolder.rv_bottle.getAdapter().notifyDataSetChanged();
                    }

                }

            }
        });


        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() > 1) {
                    mList.remove(viewHolder.getAdapterPosition());
                    notifyDataSetChanged();

                } else {
                    Toast.makeText(context, "Minimum required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        int total = 0;

        for (GetItineraryDayBottleDetail dayBottleDetail :
                model.getGetItineraryDayBottleDetail()) {
            if (!dayBottleDetail.getBottlePrice().isEmpty())
                total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
        }

        viewHolder.tv_total_spend.setText(String.format(Locale.ENGLISH, "Spend : %d", total));


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SpinAdapter<MasterVenueModel> venueAdapter;
        private RecyclerView rv_bottle;
        private TextView tv_date, tv_start, tv_total_spend;
        private Spinner sp_venue_type, sp_table_type, sp_venue;
        private ImageView iv_add_bottle, iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_add_bottle = itemView.findViewById(R.id.iv_add_bottle);
            sp_venue = itemView.findViewById(R.id.sp_venue);
            sp_table_type = itemView.findViewById(R.id.sp_table_type);
            sp_venue_type = itemView.findViewById(R.id.sp_venue_type);
            tv_start = itemView.findViewById(R.id.tv_start);
            tv_total_spend = itemView.findViewById(R.id.tv_total_spend);
            tv_date = itemView.findViewById(R.id.tv_date);
            rv_bottle = itemView.findViewById(R.id.rv_bottle);
            rv_bottle.setLayoutManager(new LinearLayoutManager(context));


            String[] venueTypeArray = new String[venueType.size()];
            venueType.toArray(venueTypeArray);
            sp_venue_type.setAdapter(new ArrayAdapter<String>(context, R.layout.item_spinner, venueTypeArray));

            sp_table_type.setAdapter(new ArrayAdapter<String>(context, R.layout.item_spinner, context.getResources().getStringArray(R.array.select_table_type)));


            tv_start.setOnClickListener(this);

            sp_venue_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (rv_bottle.getAdapter() != null && !sp_venue_type.getSelectedItem().equals(list.get(getAdapterPosition()).getVenueType())) {
                        list.get(getAdapterPosition()).getGetItineraryDayBottleDetail().clear();
                        rv_bottle.getAdapter().notifyDataSetChanged();
                    }


                    list.get(getAdapterPosition()).setVenueType(sp_venue_type.getSelectedItem().toString());
                    venueList.clear();
                    venueList.addAll(databaseHelper.getAllVenue(sp_venue_type.getSelectedItem().toString()));
                    MasterVenueModel[] venueArray = new MasterVenueModel[venueList.size()];
                    venueList.toArray(venueArray);
                    sp_venue.setAdapter(new SpinAdapter<MasterVenueModel>(context, R.layout.item_spinner, venueArray) {
                        @Override
                        protected String setValue(MasterVenueModel model) {
                            return model.getTitle();
                        }
                    });

                    sp_venue.setSelection(venueList.indexOf(new MasterVenueModel(list.get(getAdapterPosition()).getVenueId())));


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp_table_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    list.get(getAdapterPosition()).setTableName(sp_table_type.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp_venue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, final long l) {
                    //sp_venue.getSelectedItemPosition() != 0 &&
                    Log.e(list.get(getAdapterPosition()).getVenue_title(), sp_venue.getSelectedItem().toString());
                    if (rv_bottle.getAdapter() != null && !venueList.get(sp_venue.getSelectedItemPosition()).getTitle().equals(list.get(getAdapterPosition()).getVenue_title())) {
                        list.get(getAdapterPosition()).getGetItineraryDayBottleDetail().clear();
                        rv_bottle.getAdapter().notifyDataSetChanged();
                        list.get(getAdapterPosition()).setVenueId(venueList.get(sp_venue.getSelectedItemPosition()).getId());
                        list.get(getAdapterPosition()).setVenue_title(venueList.get(sp_venue.getSelectedItemPosition()).getTitle());
                        Log.e(list.get(getAdapterPosition()).getVenue_title(), sp_venue.getSelectedItem().toString());
                    }
                  rv_bottle.setAdapter(new AdminItineraryDayDataBottleAdapter(context, list.get(getAdapterPosition()).getGetItineraryDayBottleDetail(), list.get(getAdapterPosition()).getVenueId(), new AdminItineraryDayDataBottleAdapter.OnBottleSelectListener() {
                        @Override
                        public void onBottleSelect(MasterBottelModel item, int pos) {
                            int total = 0;
                            for (GetItineraryDayBottleDetail dayBottleDetail :
                                    list.get(getAdapterPosition()).getGetItineraryDayBottleDetail()) {
                                if (!dayBottleDetail.getBottlePrice().isEmpty())
                                    total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
                            }

                            tv_total_spend.setText(String.format(Locale.ENGLISH, "Spend : %d", total));

                        }

                      @Override
                      public void onBottleDelete(int adapterPosition) {

                      }
                  }));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_start) {

                int hour = Integer.parseInt(list.get(getAdapterPosition()).getTime().split(":")[0]);
                int minute = Integer.parseInt(list.get(getAdapterPosition()).getTime().split(":")[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                try {
                                    list.get(getAdapterPosition()).setTime(hourOfDay + ":" + minute);
                                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                                    SimpleDateFormat pickerformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    String date =  pickerformat.format(format.parse(  list.get(getAdapterPosition()).getDate()));
                                    list.get(getAdapterPosition()).setStartDateTime(String.format("%s %d:%d", date, hourOfDay, minute));
                                    notifyItemChanged(getAdapterPosition());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        }
    }


}
