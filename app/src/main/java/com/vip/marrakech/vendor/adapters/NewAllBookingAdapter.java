package com.vip.marrakech.vendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.vendor.fragments.VendorBookingFragment;
import com.vip.marrakech.vendor.models.AllBookingModel;
import com.vip.marrakech.vendor.models.VendorExpandBooking;

import java.util.List;

public class NewAllBookingAdapter extends GRecyclerAdapter<AllBookingModel, NewAllBookingAdapter.ViewHolder> {
    private VendorBookingAdapterSub.OnBookingListener bookingListener;

    public NewAllBookingAdapter(Context context, List<AllBookingModel> list, VendorBookingAdapterSub.OnBookingListener bookingListener) {
        super(context, list);
        this.bookingListener = bookingListener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_vendor_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<AllBookingModel> mList) {
        viewHolder.tv_pax.setText(mList.get(i).getPax());
        viewHolder.tv_title.setText(mList.get(i).getName());
        viewHolder.rv_all.setVisibility(mList.get(i).isVisible() ? View.VISIBLE : View.GONE);
        viewHolder.rv_all.setAdapter(new VendorBookingAdapter(context, mList.get(i).getBookingList()) {
            @Override
            protected VendorBookingAdapterSub setSubAdapter(VendorExpandBooking booking) {
                return new VendorBookingAdapterSub(context, booking.getList()).setListener(bookingListener);
            }

            @Override
            protected void onDateClick(VendorExpandBooking vendorExpandBooking, List<VendorExpandBooking> list) {
                for (VendorExpandBooking booking : list) {
                    if (booking.getDate().equals(vendorExpandBooking.getDate())) {
                        booking.setVisible(true);
                    } else {
                        booking.setVisible(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_pax;
        private RecyclerView rv_all;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_pax = itemView.findViewById(R.id.tv_pax);
            rv_all = itemView.findViewById(R.id.rv_all);
            rv_all.setLayoutManager(new LinearLayoutManager(context));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onHeaderClick(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    OnAllBookingListener listener;

    public void setListener(OnAllBookingListener listener) {
        this.listener = listener;
    }

    public interface OnAllBookingListener {
        void onHeaderClick(AllBookingModel allBookingModel);
    }
}
