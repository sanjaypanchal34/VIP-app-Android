package com.vip.marrakech.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.user.models.TableOptionModel;

import java.util.List;
import java.util.Locale;

public class TableOptionAdapter extends GRecyclerAdapter<TableOptionModel, TableOptionAdapter.ViewHolder> {
    public TableOptionAdapter(Context context, List<TableOptionModel> list) {
        super(context, list);
    }


    @Override
    public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_table_option, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(ViewHolder viewHolder, int i, List<TableOptionModel> mList) {
       TableOptionModel model = mList.get(i);
       viewHolder.tv_table_name.setText(model.getDisplay_name());
       viewHolder.tv_no.setText(String.format(Locale.ENGLISH,"%d.", i + 1));
       viewHolder.tv_alcohol_name.setText(String.format("(%s)",model.getAlcohol_name()));
       String no = model.getNumber_of_table() == null ? "0" : model.getNumber_of_table();
       if (no.equalsIgnoreCase("0")){
           viewHolder.tv_heading_table.setVisibility(View.GONE);
           viewHolder.tv_number_of_table.setVisibility(View.GONE);
           viewHolder.tv_see_alcohol_list.setVisibility(View.GONE);
       }else {
           viewHolder.tv_heading_table.setVisibility(View.VISIBLE);
           viewHolder.tv_number_of_table.setVisibility(View.VISIBLE);
           viewHolder.tv_see_alcohol_list.setVisibility(View.VISIBLE);
       }
        viewHolder.tv_number_of_table.setText(model.getNumber_of_table() == null ? "0" : model.getNumber_of_table());
        viewHolder.tv_spend.setText(String.format(context.getResources().getString(R.string.total_spend_mad_s), model.getPrice_in_MAD()));
        viewHolder.price_in_MAD.setText(String.format(context.getResources().getString(R.string.approx_s_per_person), model.getPrice_per_person()));


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_table_name,tv_see_alcohol_list,tv_number_of_table,tv_alcohol_name,price_in_MAD,tv_heading_table,tv_no,tv_spend;
        private final Button btn_book_now;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_see_alcohol_list = itemView.findViewById(R.id.tv_see_alcohol_list);
            tv_no = itemView.findViewById(R.id.tv_no);
            tv_spend = itemView.findViewById(R.id.tv_spend);
            tv_heading_table = itemView.findViewById(R.id.tv_heading_table);
            tv_table_name = itemView.findViewById(R.id.tv_table_name);
            tv_number_of_table = itemView.findViewById(R.id.tv_number_of_table);
            tv_alcohol_name = itemView.findViewById(R.id.tv_alcohol_name);
            price_in_MAD = itemView.findViewById(R.id.price_in_MAD);
            btn_book_now = itemView.findViewById(R.id.btn_book_now);

            btn_book_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onBookTable(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    private OnTableBookListener listener;

    public void setListener(OnTableBookListener listener) {
        this.listener = listener;
    }

    public interface OnTableBookListener {
        void onBookTable(TableOptionModel model);

    }
}
