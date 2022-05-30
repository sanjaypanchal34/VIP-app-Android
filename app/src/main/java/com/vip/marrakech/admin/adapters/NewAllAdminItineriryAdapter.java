package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.util.Log;
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
import com.vip.marrakech.admin.models.AdminExpandedIntenariry;
import com.vip.marrakech.admin.models.NewAdminBookingModel;
import com.vip.marrakech.admin.models.NewAdminItinareryModel;

import java.util.List;

public class NewAllAdminItineriryAdapter extends GRecyclerAdapter<NewAdminItinareryModel, NewAllAdminItineriryAdapter.ViewHolder> {
    private AdminItineraryAdapter.OnItineraryListener bookingListener;

    public NewAllAdminItineriryAdapter(Context context, List<NewAdminItinareryModel> list, AdminItineraryAdapter.OnItineraryListener bookingListener) {
        super(context, list);
        this.bookingListener = bookingListener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_vendor_booking, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<NewAdminItinareryModel> mList) {
        viewHolder.tv_pax.setText(mList.get(i).getPax());
        viewHolder.tv_title.setText(mList.get(i).getName());
        viewHolder.rv_all.setVisibility(mList.get(i).isVisible() ? View.VISIBLE : View.GONE);
        viewHolder.rv_all.setAdapter(new ItineraryAdapter(context, mList.get(i).getBookingList()) {
            @Override
            protected AdminItineraryAdapter setSubAdapter(AdminExpandedIntenariry booking) {
                return new AdminItineraryAdapter(context,booking.getList(),bookingListener);
            }

            @Override
            protected void onDateClick(AdminExpandedIntenariry vendorExpandBooking, List<AdminExpandedIntenariry> list) {

                for (AdminExpandedIntenariry booking : list) {
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
        void onHeaderClick(NewAdminItinareryModel allBookingModel);
    }
}
