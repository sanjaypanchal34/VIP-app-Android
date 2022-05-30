package com.vip.marrakech.vendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.vendor.models.DateAvailabilityModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AvailabilityAdapter extends GRecyclerAdapter<DateAvailabilityModel, AvailabilityAdapter.ViewHolder> {
    public AvailabilityAdapter(Context context, List<DateAvailabilityModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_availability_dates, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<DateAvailabilityModel> mList) {
        final DateAvailabilityModel model = mList.get(i);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat showFormat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        try {
            viewHolder.tv_date.setText(showFormat.format(apiDateFormat.parse(model.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.tv_time.setText(model.getTime());


        viewHolder.tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDateClick(mList.get(viewHolder.getAdapterPosition()),viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTimeClick(mList.get(viewHolder.getAdapterPosition()),viewHolder.getAdapterPosition());
                }
            }
        });


        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConciergeDelete(mList.get(viewHolder.getAdapterPosition()),viewHolder.getAdapterPosition());
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date, tv_time;
        private ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private onAvailabilityListener listener;

    public void setListener(onAvailabilityListener listener) {
        this.listener = listener;
    }

    public interface onAvailabilityListener {
        void onDateClick(DateAvailabilityModel model, int adapterPosition);
        void onTimeClick(DateAvailabilityModel model, int adapterPosition);
        void onConciergeDelete(DateAvailabilityModel model, int adapterPosition);

    }
}
