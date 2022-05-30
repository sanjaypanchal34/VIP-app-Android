package com.vip.marrakech.user.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.user.activities.LatestMixesActivity;
import com.vip.marrakech.user.models.MixesModel;

import java.io.File;
import java.util.List;

public class MixesAdapter extends RecyclerView.Adapter<MixesAdapter.ViewHolder> {
    private Context context;
    private List<MixesModel> list;
    private OnMixesListener listener;

    public MixesAdapter(Context context, List<MixesModel> list, OnMixesListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mixes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //holder.iv_music.setImageURI(String.format("%s/audio/%s", ApiClient.HOST,list.get(position).getImage()));
        holder.skeletonLayout.showSkeleton();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(String.format("%s/audio/%s", ApiClient.HOST,list.get(position).getImage()))
                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        holder.skeletonLayout.showOriginal();
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        //Action on failure
                    }

                })
                .build();
        holder.iv_music.setController(controller);
        holder.tv_mixes.setText(list.get(position).getTitle());
        File file = new File(context.getFilesDir(), new File(list.get(position).getUrl()).getName());
        if (!file.exists()){
            holder.iv_play.setImageResource(R.drawable.ic_download_mixes);
        }else {
            holder.iv_play.setImageResource(R.drawable.ic_play);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMixesClick(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  SkeletonLayout skeletonLayout;
        SimpleDraweeView iv_music;
        TextView tv_mixes;
        ImageView iv_play;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_music = itemView.findViewById(R.id.iv_music);
            tv_mixes = itemView.findViewById(R.id.tv_mixes);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }


    public interface OnMixesListener {
        void onMixesClick(MixesModel model);
    }
}
