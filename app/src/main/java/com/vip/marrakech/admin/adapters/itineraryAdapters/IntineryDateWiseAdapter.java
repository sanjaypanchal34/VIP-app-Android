package com.vip.marrakech.admin.adapters.itineraryAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.models.ItineryDetail.DayDetail;

import java.util.List;
import java.util.Locale;

public class IntineryDateWiseAdapter extends RecyclerView.Adapter<IntineryDateWiseAdapter.ViewHolder> {
    private Context context;
    private List<DayDetail> subList;
    private OnSubDayListener listener;

    public IntineryDateWiseAdapter(Context context, List<DayDetail> subList, OnSubDayListener listener) {
        this.context = context;
        this.subList = subList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary_day_list_sub, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayDetail dayDetail = subList.get(position);
        holder.tv_venue.setText(String.format(Locale.ENGLISH,"%d. Venue : %s",position+1,dayDetail.getVenue_title()));
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubDelete(dayDetail);
            }
        });

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubEdit(dayDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView tv_venue;
        private  ImageView iv_edit,iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnSubDayListener {
        void onSubClick();

        void onSubDelete(DayDetail dayDetail);

        void onSubEdit(DayDetail dayDetail);
    }
}
