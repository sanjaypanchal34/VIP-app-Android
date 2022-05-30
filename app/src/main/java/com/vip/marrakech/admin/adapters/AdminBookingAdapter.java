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
import com.vip.marrakech.vendor.adapters.VendorBookingAdapterSub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public  abstract class AdminBookingAdapter extends GRecyclerAdapter<AdminExpandBooking, AdminBookingAdapter.ViewHolder> {


    public AdminBookingAdapter(Context context, List<AdminExpandBooking> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<AdminExpandBooking> mList) {
        AdminExpandBooking booking = mList.get(i);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat displayFormate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        try {
            Date date = dateFormat.parse(booking.getDate());
            if (booking.getDate().equals(dateFormat.format(new Date()))) {

                viewHolder.tv_date.setText(String.format(Locale.ENGLISH,"%s - %d", context.getResources().getString(R.string.today), booking.getList().size()));
            }else {
                viewHolder.tv_date.setText(String.format(Locale.ENGLISH,"%s - %d", displayFormate.format(date), booking.getList().size()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.rv_bookings_sub.setAdapter(setSubAdapter(booking));

        if (booking.isVisible()){
            viewHolder.rv_bookings_sub.setVisibility(View.VISIBLE);
        }else {
            viewHolder.rv_bookings_sub.setVisibility(View.GONE);
        }
    }

    protected abstract BookingAdapter setSubAdapter(AdminExpandBooking booking);

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv_bookings_sub;
        private TextView tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            rv_bookings_sub = itemView.findViewById(R.id.rv_bookings_sub);
            rv_bookings_sub.setLayoutManager(new LinearLayoutManager(context));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onDateClick(list.get(getAdapterPosition()),list);
                }
            });
        }
    }

    protected abstract void onDateClick(AdminExpandBooking vendorExpandBooking, List<AdminExpandBooking> list);
}
