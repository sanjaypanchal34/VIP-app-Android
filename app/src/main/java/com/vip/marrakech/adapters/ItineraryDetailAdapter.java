package com.vip.marrakech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;

import java.util.List;
import java.util.Locale;

public class ItineraryDetailAdapter extends GRecyclerAdapter<GetItineraryDayDetail, ItineraryDetailAdapter.
        ViewHolder> {
    public ItineraryDetailAdapter(Context context, List<GetItineraryDayDetail> list) {
        super(context, list);
    }

    private OnItineraryDetailCLickListener listener;


    public void setListener(OnItineraryDetailCLickListener listener) {
        this.listener = listener;
    }

    @Override
    public ItineraryDetailAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_itinerary_detail, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ItineraryDetailAdapter.ViewHolder viewHolder, int i, List<GetItineraryDayDetail> mList) {
        viewHolder.tv_day.setText(String.format(Locale.ENGLISH,"DAY - %d", i+1));
        viewHolder.tv_date.setText(mList.get(i).getDayDetails().get(0).getDate());
        viewHolder.rv_day_detail.setAdapter(new ItinerarySubDetailAdapter(context,mList.get(i).getDayDetails()));
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
        public ItinerarySubDetailAdapter(Context context, List<DayDetail> list) {
            super(context, list);
        }

        @Override
        public ItinerarySubDetailAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
            return new ItinerarySubDetailAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_itinerary_detail_sub, viewGroup, false));
        }

        @Override
        public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<DayDetail> mList) {
        viewHolder.tv_day_detail.setText(String.format("%s - %s - %s%s%s", Validator.getAmPM(mList.get(i).getTime()), mList.get(i).getVenue_title(), mList.get(i).getVenueType(), mList.get(i).getTableName().isEmpty()?"": String.format(" - %s", mList.get(i).getTableName()), mList.get(i).getTable_no().isEmpty()?"": String.format(" - %s", mList.get(i).getTable_no())));

        String s = viewHolder.tv_day_detail.getText().toString();
            if (!mList.get(i).getDeposite_MAD().isEmpty() && !mList.get(i).getDeposite_MAD().equals("0")) {
                s = String.format("%s - Deposit : %s %s", s, mList.get(i).getCurrency(), mList.get(i).getDeposite_MAD());
                viewHolder.tv_day_detail.setText(s);
            }
            viewHolder.tv_day_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onItineraryDayDataClick(mList.get(viewHolder.getAdapterPosition()).getEncrypted_day_id(),mList.get(viewHolder.getAdapterPosition()).getScan_code());
                    }
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_day_detail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_day_detail = itemView.findViewById(R.id.tv_day_detail);
            }
        }
    }


    public interface OnItineraryDetailCLickListener{
        void onItineraryDayDataClick(String uniqueId, String scan_code);
    }
}
