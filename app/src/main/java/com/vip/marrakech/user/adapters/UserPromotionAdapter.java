package com.vip.marrakech.user.adapters;

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
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.user.models.UserPromotionModel;

import java.util.List;

public class UserPromotionAdapter extends GRecyclerAdapter<UserPromotionModel, UserPromotionAdapter.ViewHolder> {
    public UserPromotionAdapter(Context context, List<UserPromotionModel> list) {
        super(context, list);
    }

    @Override
    public UserPromotionAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_promotion, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final UserPromotionAdapter.ViewHolder viewHolder, int i, final List<UserPromotionModel> mList) {
//        viewHolder.iv_user_promotion.setImageURI(String.format(ApiClient.HOST + "/promotions/%s/feature/%s", mList.get(i).getId(), mList.get(i).getImages()));
        viewHolder.tv_venue.setText(mList.get(i).getVenueTitle());
        viewHolder.tv_user_promotion_desc.setText(String.format("(%s)", mList.get(i).getTitle()));
        viewHolder.skeletonLayout.showSkeleton();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(String.format(ApiClient.HOST + "/promotions/%s/feature/%s", mList.get(i).getId(), mList.get(i).getImages()))
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
        viewHolder.iv_user_promotion.setController(controller);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPromotionClick(mList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SkeletonLayout skeletonLayout;
        SimpleDraweeView iv_user_promotion;
        TextView tv_venue,
                tv_user_promotion_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_user_promotion = itemView.findViewById(R.id.iv_user_promotion);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_user_promotion_desc = itemView.findViewById(R.id.tv_user_promotion_desc);
        }
    }

    private OnPromotionListener listener;

    public void setListener(OnPromotionListener listener) {
        this.listener = listener;
    }

    public interface OnPromotionListener {
        void onPromotionClick(UserPromotionModel model);
    }
}
