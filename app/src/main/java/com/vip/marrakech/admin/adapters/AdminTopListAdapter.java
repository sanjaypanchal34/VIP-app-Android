package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.Log;
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
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.models.AdminTopModel;
import com.vip.marrakech.retrofit.ApiClient;

import java.util.List;

public class AdminTopListAdapter extends GRecyclerAdapter<AdminTopModel,AdminTopListAdapter.ViewHolder> {
    public AdminTopListAdapter(Context context, List<AdminTopModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_top,viewGroup,false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<AdminTopModel> mList) {
        final AdminTopModel model = mList.get(i);
//        viewHolder.iv_admin_promotion.setImageURI(String.format("%s/recommandation/%s/feature/%s", ApiClient.HOST, mList.get(i).getId(), mList.get(i).getFeature_image()));
        viewHolder.tv_venue.setText(mList.get(i).getVenue_title());
        viewHolder.tv_admin_promotion_desc.setText(String.format("(%s)", mList.get(i).getTitle()));
        Log.e("Top List::::",String.format("%s/recommandation/%s/feature/%s", ApiClient.HOST, mList.get(i).getId(), mList.get(i).getFeature_image()));
        viewHolder.skeletonLayout.showSkeleton();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(String.format("%s/recommandation/%s/feature/%s", ApiClient.HOST, mList.get(i).getId(), mList.get(i).getFeature_image()))
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
        viewHolder.iv_admin_promotion.setController(controller);
        viewHolder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEdit(model);
            }
        });

        viewHolder.iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(model);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  SkeletonLayout skeletonLayout;
        SimpleDraweeView iv_admin_promotion;
        TextView tv_venue, tv_admin_promotion_desc;
        ImageView iv_edit;
        ImageView iv_clear;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skeletonLayout = itemView.findViewById(R.id.skeletonLayout);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_clear = itemView.findViewById(R.id.iv_clear);
            iv_admin_promotion = itemView.findViewById(R.id.iv_admin_promotion);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_admin_promotion_desc = itemView.findViewById(R.id.tv_admin_promotion_desc);
        }
    }
    private OnTopListener listener;

    public void setListener(OnTopListener listener) {
        this.listener = listener;
    }

    public interface OnTopListener{
        void onDelete(AdminTopModel model);
        void onEdit(AdminTopModel model);
    }
}
