package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.models.AdminRecommendation;
import com.vip.marrakech.retrofit.ApiClient;

import java.util.List;

public class AdminRecommendationAdapter extends GRecyclerAdapter<AdminRecommendation, AdminRecommendationAdapter.ViewHolder> {
    public AdminRecommendationAdapter(Context context, List<AdminRecommendation> list) {
        super(context, list);
    }


    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_recommendation, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<AdminRecommendation> mList) {
//        viewHolder.iv_admin_recommendation.setImageURI(String.format("%s/recommandation_types/%s", ApiClient.HOST,mList.get(i).getImage_name()));
        viewHolder.tv_recommendation.setText(mList.get(i).getDisplay_name());
        viewHolder.skeletonLayout.showSkeleton();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(String.format("%s/recommandation_types/%s", ApiClient.HOST, mList.get(i).getImage_name()))
                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        viewHolder.skeletonLayout.showOriginal();
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        //Action on failure
                    }

                })
                .build();
        viewHolder.iv_admin_recommendation.setController(controller);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView iv_admin_recommendation;
        private final TextView tv_recommendation;
        private final SkeletonLayout skeletonLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_admin_recommendation = itemView.findViewById(R.id.iv_admin_recommendation);
            tv_recommendation = itemView.findViewById(R.id.tv_recommendation);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRecommendationClick(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    private OnRecommendationListener listener;

    public void setListener(OnRecommendationListener listener) {
        this.listener = listener;
    }

    public interface OnRecommendationListener {
        void onRecommendationClick(AdminRecommendation model);

    }
}
