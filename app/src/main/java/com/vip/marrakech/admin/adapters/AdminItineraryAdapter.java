package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.models.AdminItineraryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminItineraryAdapter extends GRecyclerAdapter<AdminItineraryModel, AdminItineraryAdapter.ViewHolder> {
    public AdminItineraryAdapter(Context context, List<AdminItineraryModel> list,OnItineraryListener listener) {
        super(context, list);
        this.listener=listener;
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<AdminItineraryModel> mList) {
        final AdminItineraryModel model = mList.get(i);
        if (model.getStatus().equalsIgnoreCase("Y")){
            viewHolder.ln_itinerary.setBackgroundColor(Color.parseColor("#156620"));
        }else {
            viewHolder.ln_itinerary.setBackgroundColor(Color.parseColor("#AB8810"));
        }
        viewHolder.tv_name.setText(model.getClientName());
        viewHolder.tv_group.setText(model.getGroup());
        viewHolder.tv_pax.setText(model.getPax());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat pickerformat = new SimpleDateFormat("EEE dd/MM", Locale.ENGLISH);
        try {
            viewHolder.tv_date.setText(String.format("%s\n%s", pickerformat.format(format.parse(model.getArrivalDate())), pickerformat.format(format.parse(model.getDepartureDate()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.tv_status.setText(model.getStatus());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null){
                    listener.onItineraryClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        viewHolder.iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null){
                    listener.onItineraryCopyClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null){
                    listener.onItineraryDeleteClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private  LinearLayout ln_itinerary;
        private TextView tv_date,tv_name,tv_group,tv_pax,tv_status;
        private ImageView iv_copy;
        private ImageView iv_delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ln_itinerary = itemView.findViewById(R.id.ln_itinerary);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_group = itemView.findViewById(R.id.tv_group);
            tv_pax = itemView.findViewById(R.id.tv_pax);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_status = itemView.findViewById(R.id.tv_status);
            iv_copy = itemView.findViewById(R.id.iv_copy);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private OnItineraryListener listener;

    public void setListener(OnItineraryListener listener) {
        this.listener = listener;
    }

    public interface OnItineraryListener {
        void onItineraryClick(AdminItineraryModel model);
        void onItineraryCopyClick(AdminItineraryModel model);
        void onItineraryDeleteClick(AdminItineraryModel model);

    }
}
