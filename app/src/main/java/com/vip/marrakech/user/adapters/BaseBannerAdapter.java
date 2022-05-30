package com.vip.marrakech.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.moeidbannerlibrary.banner.BannerLayout.OnBannerItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;

import java.util.List;

public class BaseBannerAdapter extends Adapter<BaseBannerAdapter.MzViewHolder> {
    private Context context;
    private List<String> urlList;
    private OnBannerItemClickListener onBannerItemClickListener;

    public BaseBannerAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @NonNull
    public BaseBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseBannerAdapter.MzViewHolder(LayoutInflater.from(context).inflate(R.layout.banner_list_item_custom, parent, false));
    }

    public void onBindViewHolder(BaseBannerAdapter.MzViewHolder holder, int position) {
        if (this.urlList != null && !this.urlList.isEmpty()) {
            final int P = position % this.urlList.size();
            String url = (String)this.urlList.get(P);
            SimpleDraweeView img = holder.imageView;
                img.setImageURI(url);
            img.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (BaseBannerAdapter.this.onBannerItemClickListener != null) {
                        BaseBannerAdapter.this.onBannerItemClickListener.onItemClick(P);
                    }

                }
            });
        }
    }

    public int getItemCount() {
        return this.urlList != null ? this.urlList.size() : 0;
    }

    class MzViewHolder extends ViewHolder {
        SimpleDraweeView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
        }
    }
}