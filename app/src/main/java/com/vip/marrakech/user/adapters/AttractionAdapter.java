package com.vip.marrakech.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.user.models.AttractionModel;

import java.util.List;

public class AttractionAdapter extends GRecyclerAdapter<AttractionModel, AttractionAdapter.ViewHolder> {
    public AttractionAdapter(Context context, List<AttractionModel> list) {
        super(context, list);
    }


    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_recommendation,viewGroup,false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<AttractionModel> mList) {
            viewHolder.iv_admin_recommendation.setImageURI(String.format("%s/recommandation_types/%s", ApiClient.HOST,mList.get(i).getImage()));
            viewHolder.tv_recommendation.setText(mList.get(i).getTitle());


    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView iv_admin_recommendation;
        private final TextView tv_recommendation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_admin_recommendation = itemView.findViewById(R.id.iv_admin_recommendation);
            tv_recommendation = itemView.findViewById(R.id.tv_recommendation);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
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

    public interface OnRecommendationListener{
        void onRecommendationClick(AttractionModel model);

    }
}
