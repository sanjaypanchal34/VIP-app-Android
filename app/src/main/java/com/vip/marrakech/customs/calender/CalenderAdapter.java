package com.vip.marrakech.customs.calender;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vip.marrakech.R;
import com.vip.marrakech.user.models.MyDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder> {
    private Calendar calMin;
    private Calendar calMax;
    private Calendar currentDate;
    private Context context;
    private List<MyDate> list;

    public CalenderAdapter(Context context, List<MyDate> list, Calendar currentDate, Calendar calMin, Calendar calMax) {
        this.context = context;
        this.list = list;
        this.currentDate = currentDate;
        this.calMin = calMin;
        this.calMax = calMax;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_calender, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(list.get(position).getDate());
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        holder.tv_date.setText(String.format(Locale.ENGLISH, "%d", dayValue));

       /* if (calMax != null && calMin != null) {
            calMin.add(Calendar.DAY_OF_MONTH, 1);
            calMax.add(Calendar.DAY_OF_MONTH, 1);
            if (displayMonth == currentMonth && displayYear == currentYear && list.get(position).after(calMin.getTime()) && list.get(position).before(calMax.getTime())) {
                holder.tv_date.setTextColor(Color.parseColor("#000000"));

            } else {
                holder.tv_date.setTextColor(Color.parseColor("#cccccc"));
            }
        } else if (calMin != null) {


            if (displayMonth == currentMonth && displayYear == currentYear && list.get(position).after(calMin.getTime())) {
                holder.tv_date.setTextColor(Color.parseColor("#000000"));

            } else {
                holder.tv_date.setTextColor(Color.parseColor("#cccccc"));
            }
        } else if (calMax != null) {
            if (displayMonth == currentMonth && displayYear == currentYear && list.get(position).before(calMax.getTime())) {
                holder.tv_date.setTextColor(Color.parseColor("#000000"));

            } else {
                holder.tv_date.setTextColor(Color.parseColor("#cccccc"));
            }
        } else if (displayMonth == currentMonth && displayYear == currentYear) {
            holder.tv_date.setTextColor(Color.parseColor("#000000"));

        } else {
            holder.tv_date.setTextColor(Color.parseColor("#cccccc"));
        }*/

        if (isValidDate(currentMonth, displayMonth, displayYear, currentYear, position)) {
            holder.tv_date.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.tv_date.setTextColor(Color.parseColor("#cccccc"));
        }

        if (list.get(position).isSelected()) {
            holder.tv_date.setBackgroundResource(R.drawable.selected_date_blue_circle);
        } else {
            holder.tv_date.setBackground(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && isValidDate(currentMonth, displayMonth, displayYear, currentYear, position)) {

                    for (MyDate date :
                            list) {
                        if (date.getDate().equals(list.get(holder.getAdapterPosition()).getDate())) {
                            date.setSelected(true);
                        } else {
                            date.setSelected(false);
                        }
                    }

                    listener.onDateSelect(list.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);


        }
    }

    public OnCalenderListener listener;

    public void setListener(OnCalenderListener listener) {
        this.listener = listener;
    }


    private boolean isValidDate(int currentMonth, int displayMonth, int displayYear, int currentYear, int position) {
        if (calMax != null && calMin != null) {
            calMin.add(Calendar.DAY_OF_MONTH, 1);
            calMax.add(Calendar.DAY_OF_MONTH, 1);
            return displayMonth == currentMonth && displayYear == currentYear && list.get(position).getDate().after(calMin.getTime()) && list.get(position).getDate().before(calMax.getTime());
        } else if (calMin != null) {
            return displayMonth == currentMonth && displayYear == currentYear && list.get(position).getDate().after(calMin.getTime());
        } else if (calMax != null) {
            return displayMonth == currentMonth && displayYear == currentYear && list.get(position).getDate().before(calMax.getTime());
        } else return displayMonth == currentMonth && displayYear == currentYear;
    }
}
