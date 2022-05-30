package com.vip.marrakech.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;


import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public abstract class CustomBannerAdapter<T, VH extends SliderViewAdapter.ViewHolder> extends SliderViewAdapter<VH> {
    private Context context;
    private List<T> urlList;

    public CustomBannerAdapter(Context context, List<T> urlList) {
        this.context = context;
        this.urlList = urlList;
    }



    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return setViewHolder(parent, viewType);
    }

    protected abstract VH setViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(VH holder, int position) {
        setBindViewHolder(holder, position);
    }

    protected abstract void setBindViewHolder(VH holder, int position);

    public int getItemCount() {
        return this.urlList != null ? this.urlList.size() : 0;
    }

}