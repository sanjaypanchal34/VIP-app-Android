package com.vip.marrakech.admin.adapters.itineraryAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.adapters.SPAdapter;
import com.vip.marrakech.admin.adapters.AdminItineraryDayDataBottleAdapter;
import com.vip.marrakech.customs.CustomSpinner;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class ItineraryDayDetailAdapter extends GRecyclerAdapter<DayDetail, ItineraryDayDetailAdapter.ViewHolder> {
    private List<String> tableType = new ArrayList<>();
    private List<String> venueType;
    private OnItineraryDataDetailListener listener;

    public ItineraryDayDetailAdapter(Context context, List<DayDetail> list, List<String> venueTypeType) {
        super(context, list);
        this.venueType = venueTypeType;
        tableType.addAll(Arrays.asList(context.getResources().getStringArray(R.array.select_table_type)));
    }

    public void setListener(OnItineraryDataDetailListener listener) {
        this.listener = listener;
    }

    @Override
    public ItineraryDayDetailAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary_day_data, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, List<DayDetail> mList) {
        final DayDetail model = mList.get(i);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        if (!model.getDate().isEmpty()) {
            try {

                viewHolder.tv_date.setText(pickerformat.format(format.parse(model.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            viewHolder.tv_date.setText("");
        }
        viewHolder.tv_start.setText(Validator.getAmPM(model.getTime().equals("00:00")?"":model.getTime()));
        viewHolder.sp_venue_type.setText(model.getVenueType());
        viewHolder.sp_table_type.setText(model.getTableName());
        viewHolder.sp_venue.setText(model.getVenue_title());
        viewHolder.sp_table_type.setText(model.getTableName());

        if (model.getVenueType().equalsIgnoreCase("Nightclub") || model.getVenueType().equalsIgnoreCase("Day Party")){
            viewHolder.sp_table_type.setVisibility(View.VISIBLE);
        }else {
            viewHolder.sp_table_type.setVisibility(View.INVISIBLE);
        }



        viewHolder.rv_bottle.setAdapter(setBottlesAdapter(i));

        viewHolder.sp_venue.setAdapter(setVenueAdapter(viewHolder.sp_venue_type.getText().toString(), viewHolder.sp_venue, viewHolder.getAdapterPosition()));
        int total = 0;

        for (GetItineraryDayBottleDetail dayBottleDetail :
                list.get(i).getGetItineraryDayBottleDetail()) {
            if (!dayBottleDetail.getBottlePrice().isEmpty())
                total = total + Integer.parseInt(dayBottleDetail.getBottlePrice().replaceAll(",", ""));
        }
        viewHolder.tv_total_spend.setText(String.format(Locale.ENGLISH, "Spend : %s", Validator.getFormattedNumber(String.valueOf(total))));


        viewHolder.iv_add_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBottleAdd(list.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }

            }
        });


        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDelete(list.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTime(list.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
            }
        });


        if (model.getVenueType().equals("Day Party") || model.getVenueType().equals("Nightclub")){
            viewHolder.ln_bottle.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ln_bottle.setVisibility(View.GONE);
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv_bottle;
        private TextView tv_date, tv_start, tv_total_spend;
        private CustomSpinner sp_venue_type, sp_table_type, sp_venue;
        private ImageView iv_add_bottle, iv_delete;
        private LinearLayout ln_bottle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ln_bottle = itemView.findViewById(R.id.ln_bottle);
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

            sp_venue_type.setAdapter(new SPAdapter<String>(context, venueType, new SPAdapter.onSpinnerSelectListener<String>() {
                @Override
                public void onSpinnerItemClick(String item, int pos) {
                    sp_venue_type.dismiss();
                    sp_venue.setAdapter(setVenueAdapter(item, sp_venue, getAdapterPosition()));
                    if (listener != null) {
                        listener.onVenueType(item, getAdapterPosition(), pos);
                     /*   if (pos != 0) {

                        } else {
                            listener.onVenueType("", getAdapterPosition(), pos);
                        }*/
                    }

                }
            }) {
                @Override
                protected String setSpinnerText(String item) {
                    return item;
                }
            });

            sp_table_type.setAdapter(new SPAdapter<String>(context, Arrays.asList(context.getResources().getStringArray(R.array.select_table_type)), new SPAdapter.onSpinnerSelectListener<String>() {
                @Override
                public void onSpinnerItemClick(String item, int pos) {
                    sp_table_type.dismiss();
                    if (listener != null) {
                        listener.onSelectTableType(item, getAdapterPosition(), pos);
                      /*  if (pos != 0) {
                            listener.onSelectTableType(item, getAdapterPosition(), pos);
                        }else {
                            listener.onSelectTableType("",getAdapterPosition(),pos);
                        }*/
                    }

                }
            }) {
                @Override
                protected String setSpinnerText(String item) {
                    return item;
                }
            });


            sp_venue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp_venue.select();
                }
            });
            sp_table_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp_table_type.select();
                }
            });
            sp_venue_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp_venue_type.select();
                }
            });




        }
    }

    protected abstract BaseAdapter setVenueAdapter(String venueType, CustomSpinner spinner, int adapterPosition);
    protected abstract AdminItineraryDayDataBottleAdapter setBottlesAdapter( int adapterPosition);


    public interface OnItineraryDataDetailListener {
        void onBottleAdd(DayDetail detail, int pos);

        void onDelete(DayDetail detail, int pos);

        void onTime(DayDetail detail, int pos);

        void onVenueType(String venueType, int adapterPosition, int pos);

        void onSelectTableType(String tableType, int adapterPosition, int pos);
    }
}
