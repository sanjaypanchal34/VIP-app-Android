package com.vip.marrakech.admin.adapters;

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
import com.vip.marrakech.admin.models.AdminExpandBooking;
import com.vip.marrakech.admin.models.NewAdminBookingModel;
import com.vip.marrakech.vendor.adapters.VendorBookingAdapterSub;
import com.vip.marrakech.vendor.models.VendorExpandBooking;

import java.util.List;

public class NewAllAdminBookingAdapter extends GRecyclerAdapter<NewAdminBookingModel, NewAllAdminBookingAdapter.ViewHolder> {
    private BookingAdapter.OnBookingListener bookingListener;

    public NewAllAdminBookingAdapter(Context context, List<NewAdminBookingModel> list, BookingAdapter.OnBookingListener bookingListener) {
        super(context, list);
        this.bookingListener = bookingListener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_vendor_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<NewAdminBookingModel> mList) {
        viewHolder.tv_pax.setText(mList.get(i).getPax());
        viewHolder.tv_title.setText(mList.get(i).getName());
        viewHolder.rv_all.setVisibility(mList.get(i).isVisible() ? View.VISIBLE : View.GONE);
        viewHolder.rv_all.setAdapter(new AdminBookingAdapter(context, mList.get(i).getBookingList()) {
            @Override
            protected BookingAdapter setSubAdapter(AdminExpandBooking booking) {
                return new BookingAdapter(context,booking.getList(),bookingListener);
            }

            @Override
            protected void onDateClick(AdminExpandBooking vendorExpandBooking, List<AdminExpandBooking> list) {

                for (AdminExpandBooking booking : list) {
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
        void onHeaderClick(NewAdminBookingModel allBookingModel);
    }
}
