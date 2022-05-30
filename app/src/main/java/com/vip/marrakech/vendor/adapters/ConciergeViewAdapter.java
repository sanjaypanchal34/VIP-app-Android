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
import com.vip.marrakech.vendor.models.ConciergeClientModel;

import java.util.List;

public class ConciergeViewAdapter extends GRecyclerAdapter<ConciergeClientModel, ConciergeViewAdapter.ViewHolder> {
    public ConciergeViewAdapter(Context context, List<ConciergeClientModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_concierge_detail, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<ConciergeClientModel> mList) {
        final ConciergeClientModel model = mList.get(i);

        viewHolder.tv_client_name.setText(model.getClientName());
        viewHolder.tv_date.setText(model.getClientEmail());
        viewHolder.tv_total_spend.setText(String.format("%s", model.getTotalSpend()));
        viewHolder.tv_commission.setText(String.format("%s", model.getComission()));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConciergeClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_client_name;
        private TextView tv_date;
        private TextView tv_total_spend;
        private TextView tv_commission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_client_name = itemView.findViewById(R.id.tv_client_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_total_spend = itemView.findViewById(R.id.tv_total_spend);
            tv_commission = itemView.findViewById(R.id.tv_commission);
        }
    }

    private OnConciergeListener listener;

    public void setListener(OnConciergeListener listener) {
        this.listener = listener;
    }

    public interface OnConciergeListener {
        void onConciergeClick(ConciergeClientModel model);

    }
}
