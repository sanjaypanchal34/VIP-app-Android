package com.vip.marrakech.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TopDetailInfoAdapter extends RecyclerView.Adapter<TopDetailInfoAdapter.ViewHolder> {
    private Context context;
    List<Map<String, String>> map;

    public TopDetailInfoAdapter(Context context, List<Map<String, String>> map) {
        this.context = context;
        this.map = map;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_top_detail_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object key = map.get(position).keySet().toArray()[0];
        holder.tv_lable.setText(key.toString());
        holder.tv_value.setText(map.get(position).get(key));
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_lable,tv_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_lable = itemView.findViewById(R.id.tv_lable);
            tv_value = itemView.findViewById(R.id.tv_value);
        }
    }
}
