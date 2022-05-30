package com.vip.marrakech.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.user.models.UserItinararyModel;

import java.util.List;

public class UserItineraryAdapter extends GRecyclerAdapter<UserItinararyModel, UserItineraryAdapter.ViewHolder> {
    public UserItineraryAdapter(Context context, List<UserItinararyModel> list) {
        super(context, list);
    }

    private OnItineraryListener listener;

    @Override
    public UserItineraryAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_itinerary, viewGroup, false));
    }

    public void setListener(OnItineraryListener listener) {
        this.listener = listener;
    }

    @Override
    public void myBindViewHolder(final UserItineraryAdapter.ViewHolder viewHolder, int i, final List<UserItinararyModel> mList) {
        viewHolder.tv_user_iti_date.setText(String.format("%s", mList.get(i).getStartDateTime()));
        viewHolder.tv_user_iti_pax.setText(String.format(context.getResources().getString(R.string.group_size_s), mList.get(i).getPax()));
        viewHolder.tv_time.setText(String.format("%s", mList.get(i).getTime()));
        viewHolder.tv_venue.setText(String.format("%s", mList.get(i).getVenueTitle()));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null)
                    listener.OnItineraryClick(mList.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_venue;
        TextView tv_user_iti_date;
        TextView tv_user_iti_pax;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_iti_date = itemView.findViewById(R.id.tv_user_iti_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_user_iti_pax = itemView.findViewById(R.id.tv_user_iti_pax);
        }
    }


    public interface OnItineraryListener {
        void OnItineraryClick(UserItinararyModel model);
    }
}
