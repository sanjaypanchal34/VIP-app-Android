package com.vip.marrakech.vendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.vendor.models.ConciergeModel;

import java.util.List;

public class ConciergeAdapter extends GRecyclerAdapter<ConciergeModel, ConciergeAdapter.ViewHolder> {
    public ConciergeAdapter(Context context, List<ConciergeModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor_concierge, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<ConciergeModel> mList) {
        final ConciergeModel model = mList.get(i);

        viewHolder.tv_email.setText(model.getEmail());
        viewHolder.tv_c_name.setText(model.getName());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConciergeClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });

        viewHolder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConciergeEdit(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConciergeDelete(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_email, tv_c_name;
        private ImageView iv_edit;
        private ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_c_name = itemView.findViewById(R.id.tv_c_name);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private OnConciergeListener listener;

    public void setListener(OnConciergeListener listener) {
        this.listener = listener;
    }

    public interface OnConciergeListener {
        void onConciergeClick(ConciergeModel model);
        void onConciergeEdit(ConciergeModel model);
        void onConciergeDelete(ConciergeModel model);

    }
}
