package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.adapters.SPAdapter;
import com.vip.marrakech.admin.dataBaseHeler.DatabaseHelper;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.customs.CustomSpinner;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayBottleDetail;

import java.util.List;

public class AdminItineraryDayDataBottleAdapter extends GRecyclerAdapter<GetItineraryDayBottleDetail, AdminItineraryDayDataBottleAdapter.ViewHolder> {

    private OnBottleSelectListener listener;
    private List<String> bottleType;
    private DatabaseHelper databaseHelper;


    public AdminItineraryDayDataBottleAdapter(Context context, List<GetItineraryDayBottleDetail> list, String venue_Id, OnBottleSelectListener listener) {
        super(context, list);
        System.out.println(venue_Id);
        this.listener = listener;
        databaseHelper = new DatabaseHelper(context);
        this.bottleType = databaseHelper.getBottleType(venue_Id);
        Log.e(venue_Id,bottleType.toString());
    }


    @Override
    public AdminItineraryDayDataBottleAdapter.ViewHolder setItemRow(ViewGroup viewGroup, int i) {
        return new AdminItineraryDayDataBottleAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_itinerary_day_data_bottel, viewGroup, false));
    }

    @Override
    public void myBindViewHolder(final AdminItineraryDayDataBottleAdapter.ViewHolder viewHolder, final int i, final List<GetItineraryDayBottleDetail> mList) {
        final GetItineraryDayBottleDetail model = mList.get(i);
        viewHolder.sp_bottle_type.setText(model.getBottleType());
        viewHolder.sp_bottle_name.setText(model.getBottleName());


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomSpinner sp_bottle_type, sp_bottle_name;

        private ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            sp_bottle_type = itemView.findViewById(R.id.sp_bottle_type);
            sp_bottle_name = itemView.findViewById(R.id.sp_bottle_name);
            sp_bottle_type.setAdapter(new SPAdapter<String>(context, bottleType, new SPAdapter.onSpinnerSelectListener<String>() {
                @Override
                public void onSpinnerItemClick(String item, int pos) {
                    sp_bottle_type.dismiss();
                    if (listener != null) {
                        list.get(getAdapterPosition()).setBottleType(item);
                        list.get(getAdapterPosition()).setBottleName("");
                        listener.onBottleSelect(new MasterBottelModel(""), -1);
                      /*  if (pos != 0) {
                            list.get(getAdapterPosition()).setBottleType(item);
                        } else {
                            list.get(getAdapterPosition()).setBottleType("");
                        }*/
                        notifyDataSetChanged();
                    }
                }
            }) {
                @Override
                protected String setSpinnerText(String item) {
                    return item;
                }
            });

            sp_bottle_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp_bottle_type.select();
                }
            });


            sp_bottle_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sp_bottle_type.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Select Bottle Type", Toast.LENGTH_SHORT).show();
                    } else {
                        sp_bottle_name.setAdapter(new SPAdapter<MasterBottelModel>(context, databaseHelper.getAllBottle(sp_bottle_type.getText().toString()), new SPAdapter.onSpinnerSelectListener<MasterBottelModel>() {
                            @Override
                            public void onSpinnerItemClick(MasterBottelModel item, int pos) {
                                sp_bottle_name.dismiss();
                                if (listener != null) {
                                        list.get(getAdapterPosition()).setBottleName(item.getBottle_name());
                                        list.get(getAdapterPosition()).setBottlePrice(item.getBottle_price());
                                        listener.onBottleSelect(item, pos);
                                        sp_bottle_name.setText(item.getBottle_name());
                                }
                            }
                        }) {
                            @Override
                            protected String setSpinnerText(MasterBottelModel item) {
                                return item.getBottle_name();
                            }
                        });

                        sp_bottle_name.select();
                    }

                }
            });


            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        list.remove(getAdapterPosition());
                        listener.onBottleDelete(getAdapterPosition());

                    }
                }
            });



        /*    sp_bottle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    MasterBottelModel[] bottleArray = new MasterBottelModel[databaseHelper.getAllBottle(sp_bottle_type.getSelectedItem().toString()).size()];
                    databaseHelper.getAllBottle(sp_bottle_type.getSelectedItem().toString()).toArray(bottleArray);
                    sp_bottle_name.setAdapter(new SpinAdapter<MasterBottelModel>(context, R.layout.item_spinner, bottleArray) {
                        @Override
                        protected String setValue(MasterBottelModel model) {
                            return model.getBottle_name();
                        }
                    });
                    // list.get(getAdapterPosition()).setBottleType(sp_bottle_type.getSelectedItem().toString());

                    for (int i = 0; i < databaseHelper.getAllBottle(sp_bottle_type.getSelectedItem().toString()).size(); i++) {
                        MasterBottelModel bottelModel = databaseHelper.getAllBottle(sp_bottle_type.getSelectedItem().toString()).get(i);
                        if (bottelModel.getBottle_name().equals(list.get(getAdapterPosition()).getBottleName()))
                            sp_bottle_name.setSelection(i);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sp_bottle_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   *//* Log.e(sp_bottle_name.getSelectedItem().toString(), String.valueOf(position));
                    for (int i1 = 0; i1 < bottleNameList.size(); i1++) {
                        MasterBottelModel bottelModel = bottleNameList.get(i1);
                        Log.e(String.valueOf(i1),bottelModel.getBottle_name());
                    }*//*
                if (sp_bottle_name.getSelectedItemPosition() != 0) {
                    MasterBottelModel bottelModel = databaseHelper.getAllBottle(sp_bottle_type.getSelectedItem().toString()).get(position);
                    list.get(getAdapterPosition()).setBottleName(bottelModel.getBottle_name());
                    list.get(getAdapterPosition()).setBottleType(bottelModel.getBottle_type());
                    list.get(getAdapterPosition()).setBottlePrice(bottelModel.getBottle_price());
                    list.get(getAdapterPosition()).setId(bottelModel.getId());
                    notifyDataSetChanged();

                    listener.onBottleSelect();

                }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }


    public interface OnBottleSelectListener {
        void onBottleSelect(MasterBottelModel item, int pos);
        void onBottleDelete(int adapterPosition);
    }


}