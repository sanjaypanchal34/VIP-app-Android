package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.adapters.itineraryAdapters.IntineryDateWiseAdapter;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public abstract class AdminItineraryDayListAdapter extends GRecyclerAdapter<GetItineraryDayDetail, AdminItineraryDayListAdapter.ViewHolder> {
    public AdminItineraryDayListAdapter(Context context, List<GetItineraryDayDetail> list) {
        super(context, list);
    }

    private OnItineraryDayListener listener;

    public void setListener(OnItineraryDayListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary_day_list, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, List<GetItineraryDayDetail> mList) {
        final GetItineraryDayDetail model = mList.get(i);
        model.getDayDetails().get(0).setDayNo(String.format(Locale.ENGLISH, "DAY - %d", i + 1));
        viewHolder.tv_day.setText(String.format(Locale.ENGLISH, "DAY - %d", i + 1));
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        if (!model.getDayDetails().get(0).getDate().isEmpty()) {
            try {

                viewHolder.tv_date.setText(pickerformat.format(format.parse(model.getDayDetails().get(0).getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            viewHolder.tv_date.setText("");
        }


        viewHolder.tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewHolder.tv_date.getText().toString().isEmpty()) {
                    listener.onGoClick(model);
                } else {
                    Toast.makeText(context, "Select Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateClick(model);
            }
        });

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClick(model, viewHolder.getAdapterPosition());
            }
        });

        viewHolder.rv_sub_day.setAdapter(setSubAdapter(model));
    }

    protected abstract IntineryDateWiseAdapter setSubAdapter(GetItineraryDayDetail model);

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_day, tv_go, tv_date;
        private ImageView iv_delete;
        private RecyclerView rv_sub_day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_go = itemView.findViewById(R.id.tv_go);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            rv_sub_day = itemView.findViewById(R.id.rv_sub_day);
            rv_sub_day.setLayoutManager(new LinearLayoutManager(context));
        }
    }


    public interface OnItineraryDayListener {
        void onGoClick(GetItineraryDayDetail model);

        void onDateClick(GetItineraryDayDetail model);

        void onDeleteClick(GetItineraryDayDetail model, int adapterPosition);
    }
}
