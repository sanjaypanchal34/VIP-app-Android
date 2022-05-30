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
import com.vip.marrakech.vendor.models.VendorAllBookingModel;

import java.util.List;

public class VendorAllBookingAdapter extends GRecyclerAdapter<VendorAllBookingModel, VendorAllBookingAdapter.ViewHolder> {
    public VendorAllBookingAdapter(Context context, List<VendorAllBookingModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_all_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<VendorAllBookingModel> mList) {
        final VendorAllBookingModel model = mList.get(i);

        String[] date_time= model.getDateTime().split(",");
        if (date_time.length == 2){
            viewHolder.tv_date.setText(String.format("%s,\n%s", date_time[0], date_time[1]));
        }else {
            viewHolder.tv_date.setText(model.getDateTime());

        }
        viewHolder.tv_type.setText(model.getType());
                viewHolder.tv_client.setText(model.getClientName());
        viewHolder.tv_vendor.setText(model.getTotalSpend());
        viewHolder.tv_pax.setText(model.getPax());
        switch (model.getStatus()){
            case "NC":{
                viewHolder.iv_status.setColorFilter(Color.parseColor("#AA8A0F"), PorterDuff.Mode.SRC_IN);
            }break;
            case "CNF":{
                viewHolder.iv_status.setColorFilter(Color.parseColor("#00b519"), PorterDuff.Mode.SRC_IN);
            }break;
            case "A": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#14681D"), PorterDuff.Mode.SRC_IN);
            }break;

            case "NA":
            case "C": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#9E0C0C"), PorterDuff.Mode.SRC_IN);
            }break;

            default: viewHolder.iv_status.setColorFilter(Color.parseColor("#00000000"), PorterDuff.Mode.SRC_IN);
        }
        viewHolder.tv_status.setText(model.getStatus());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null){
                    listener.onBookingClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date,tv_client,tv_vendor,tv_pax,tv_status,tv_type;
        private ImageView iv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_client = itemView.findViewById(R.id.tv_client);
            tv_pax = itemView.findViewById(R.id.tv_pax);
            tv_vendor = itemView.findViewById(R.id.tv_vendor);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }

    private OnBookingListener listener;

    public void setListener(OnBookingListener listener) {
        this.listener = listener;
    }

    public interface OnBookingListener {
        void onBookingClick(VendorAllBookingModel model);

    }
}
