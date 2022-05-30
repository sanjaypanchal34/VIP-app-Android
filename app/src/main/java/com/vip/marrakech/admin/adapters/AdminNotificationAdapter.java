package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.admin.models.AdminNotificationModel;

import java.util.List;
import java.util.Locale;

public class AdminNotificationAdapter extends GRecyclerAdapter<AdminNotificationModel, AdminNotificationAdapter.ViewHolder> {
    public AdminNotificationAdapter(Context context, List<AdminNotificationModel> list) {
        super(context, list);
    }

    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_notification, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final ViewHolder viewHolder, int i, final List<AdminNotificationModel> mList) {
        final AdminNotificationModel model = mList.get(i);
        viewHolder.tv_time.setText(model.getNotification_time());
        viewHolder.tv_title.setText(model.getNotification_content());
        if (model.getIs_read().equals("1")) {
            viewHolder.ln_notification.setBackgroundColor(Color.parseColor("#D0D0D0"));
        } else {
            viewHolder.ln_notification.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotificationClick(model);
            }
        });

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(String.format(Locale.ENGLISH, "android.resource://%s/%d", context.getPackageName(), R.raw.bell)))
                .setAutoPlayAnimations(true)
                .build();
        viewHolder.iv_bell.setController(controller);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ln_notification;
        private TextView tv_title, tv_time;
        private SimpleDraweeView iv_bell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_bell = itemView.findViewById(R.id.iv_bell);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            ln_notification = itemView.findViewById(R.id.ln_notification);
        }
    }

    private OnNotificationClickListener listener;

    public void setListener(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(AdminNotificationModel model);

    }
}
