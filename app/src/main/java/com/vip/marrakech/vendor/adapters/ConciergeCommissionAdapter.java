package com.vip.marrakech.vendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.vendor.models.ConciergeCommissionModel;

import java.util.List;

public class ConciergeCommissionAdapter extends GRecyclerAdapter<ConciergeCommissionModel, ConciergeCommissionAdapter.ViewHolder> {
    public ConciergeCommissionAdapter(Context context, List<ConciergeCommissionModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_concierge_commissin, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<ConciergeCommissionModel> mList) {
        final ConciergeCommissionModel model = mList.get(i);

        String[] date_time= model.getDate().split(",");
        if (date_time.length == 2){
            viewHolder.tv_date.setText(String.format("%s,\n%s", date_time[0], date_time[1]));
        }else {
            viewHolder.tv_date.setText(model.getDate());

        }
        viewHolder.tv_c_name.setText(model.getConciergeName());
        viewHolder.tv_total_client.setText(model.getTotalClients());
        viewHolder.tv_commission.setText(String.format("%s", model.getTotalSpend()));


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

        private TextView tv_date, tv_c_name, tv_total_client, tv_commission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_c_name = itemView.findViewById(R.id.tv_c_name);
            tv_total_client = itemView.findViewById(R.id.tv_total_client);
            tv_commission = itemView.findViewById(R.id.tv_commission);
        }
    }

    private OnBookingListener listener;

    public void setListener(OnBookingListener listener) {
        this.listener = listener;
    }

    public interface OnBookingListener {
        void onBookingClick(ConciergeCommissionModel model);

    }
}
