package com.vip.marrakech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.user.models.UserItinararyModel;

import java.util.List;
import java.util.Locale;

public class ItineraryDetailAdapterUser extends GRecyclerAdapter<UserItinararyModel, ItineraryDetailAdapterUser.
        ViewHolder> {
    public ItineraryDetailAdapterUser(Context context, List<UserItinararyModel> list) {
        super(context, list);
    }

    private OnItineraryDetailCLickListener listener;


    public void setListener(OnItineraryDetailCLickListener listener) {
        this.listener = listener;
    }

    @Override
    public ItineraryDetailAdapterUser.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_itinerary_detail_new, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ItineraryDetailAdapterUser.ViewHolder viewHolder, int i, List<UserItinararyModel> mList) {
        viewHolder.tv_day.setText(String.format(Locale.ENGLISH, "DAY - %d", i + 1));
        viewHolder.tv_date.setText(mList.get(i).getStartDateTime());
        //viewHolder.rv_day_detail.setAdapter(new ItinerarySubDetailAdapter(context, mList.get(i).getDayDetails()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_day, tv_date;
        private RecyclerView rv_day_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_date = itemView.findViewById(R.id.tv_date);
            rv_day_detail = itemView.findViewById(R.id.rv_day_detail);
            rv_day_detail.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public class ItinerarySubDetailAdapter extends GRecyclerAdapter<DayDetail, ItinerarySubDetailAdapter.
            ViewHolder> {
        ItinerarySubDetailAdapter(Context context, List<DayDetail> list) {
            super(context, list);
        }

        @Override
        public ItinerarySubDetailAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
            return new ItinerarySubDetailAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_itinerary_detail_sub, viewGroup, false));
        }

        @Override
        public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<DayDetail> mList) {
            viewHolder.tv_day_detail.setText(String.format("%s - %s - %s%s", Validator.getAmPM(mList.get(i).getTime()), mList.get(i).getVenue_title(), mList.get(i).getVenueType(), mList.get(i).getTableName().isEmpty() ? "" : String.format(" - %s", mList.get(i).getTableName())));

            viewHolder.tv_day_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItineraryDayDataClick(mList.get(viewHolder.getAdapterPosition()).getEncrypted_day_id(), mList.get(viewHolder.getAdapterPosition()).getScan_code(), mList.get(viewHolder.getAdapterPosition()).getStatus());
                    }
                }
            });

            System.out.println("DAY ID:::" + mList.get(viewHolder.getAdapterPosition()).getEncrypted_day_id());
            viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItineraryCancelClick(mList.get(viewHolder.getAdapterPosition()).getEncrypted_day_id());
                    }
                }
            });

            if (mList.get(viewHolder.getAdapterPosition()).getStatus().equals("A") || mList.get(viewHolder.getAdapterPosition()).getStatus().equals("UC") || mList.get(viewHolder.getAdapterPosition()).getStatus().equals("C")) {
                viewHolder.btn_cancel.setVisibility(View.GONE);
            } else {
                viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_day_detail;
            private Button btn_cancel;
            MaterialSpinner sp_currency;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_day_detail = itemView.findViewById(R.id.tv_day_detail);
                btn_cancel = itemView.findViewById(R.id.btn_cancel);
                sp_currency = itemView.findViewById(R.id.sp_currency);
                sp_currency.setItems("MAD","GBP","EUR","USD");
            }
        }
    }


    public interface OnItineraryDetailCLickListener {
        void onItineraryDayDataClick(String uniqueId, String scan_code, String status);

        void onItineraryCancelClick(String encrypted_day_id);
    }
}
