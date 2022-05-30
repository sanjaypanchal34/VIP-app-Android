package com.vip.marrakech.admin.adapters;

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
import com.vip.marrakech.admin.models.BookingModel;

import java.util.List;

public class BookingAdapter extends GRecyclerAdapter<BookingModel, BookingAdapter.ViewHolder> {
    public BookingAdapter(Context context, List<BookingModel> list,OnBookingListener listener) {

        super(context, list);
        this.listener = listener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<BookingModel> mList) {
        final BookingModel model = mList.get(i);

        String[] date_time= model.getStartDateTime().split(",");
        if (date_time.length == 2){
                viewHolder.tv_date.setText(String.format("%s,\n%s", date_time[0], date_time[1]));
        }else {
            viewHolder.tv_date.setText(model.getStartDateTime());

        }
        viewHolder.tv_client.setText(model.getClientName());
        viewHolder.tv_vendor.setText(model.getVendorName());
        switch (model.getStatus()) {
            case "NB": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#AD890E"), PorterDuff.Mode.SRC_IN);
            }
            break;

            case "B": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#00b519"), PorterDuff.Mode.SRC_IN);
            }
            break;
            case "A": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#156822"), PorterDuff.Mode.SRC_IN);
            }
            break;

            case "C": {
                viewHolder.iv_status.setColorFilter(Color.parseColor("#9E0C0C"), PorterDuff.Mode.SRC_IN);
            }break;

            default: viewHolder.iv_status.setColorFilter(Color.parseColor("#00000000"), PorterDuff.Mode.SRC_IN);
        }
        viewHolder.tv_status.setText(model.getStatus());


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

        private TextView tv_date, tv_client, tv_vendor, tv_status;
        private ImageView iv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_client = itemView.findViewById(R.id.tv_client);
            tv_vendor = itemView.findViewById(R.id.tv_vendor);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }

    private OnBookingListener listener;



    public interface OnBookingListener {
        void onBookingClick(BookingModel model);

    }
}
