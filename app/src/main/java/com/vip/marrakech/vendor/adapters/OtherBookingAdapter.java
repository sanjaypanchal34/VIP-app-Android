package com.vip.marrakech.vendor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.vendor.models.OtherBookingModel;

import java.util.List;

public class OtherBookingAdapter extends GRecyclerAdapter<OtherBookingModel, OtherBookingAdapter.ViewHolder> {
    public OtherBookingAdapter(Context context, List<OtherBookingModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_other_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<OtherBookingModel> mList) {
        final OtherBookingModel model = mList.get(i);


        String[] date_time= model.getDateTime().split(",");
        if (date_time.length == 2){
            viewHolder.tv_date.setText(String.format("%s,\n%s", date_time[0], date_time[1]));
        }else {
            viewHolder.tv_date.setText(model.getDateTime());

        }
        //viewHolder.tv_date.setText(model.getDateTime());
        viewHolder.tv_client.setText(model.getClientName());
        viewHolder.tv_total_spend.setText(model.getTotalSpend());
        viewHolder.tv_concierge.setText(model.getConciergeName());
        viewHolder.tv_pax.setText(model.getPax());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onBookingClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date, tv_client, tv_total_spend, tv_pax, tv_concierge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_client = itemView.findViewById(R.id.tv_client);
            tv_pax = itemView.findViewById(R.id.tv_pax);
            tv_total_spend = itemView.findViewById(R.id.tv_total_spend);
            tv_concierge = itemView.findViewById(R.id.tv_concierge);
        }
    }

    private OnBookingListener listener;

    public void setListener(OnBookingListener listener) {
        this.listener = listener;
    }

    public interface OnBookingListener {
        void onBookingClick(OtherBookingModel model);

    }
}
