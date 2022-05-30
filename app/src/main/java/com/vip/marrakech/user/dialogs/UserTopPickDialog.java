package com.vip.marrakech.user.dialogs;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserTopPickDialog extends DialogFragment implements OnCallBackListener {


    private RecyclerView rv_top_pick;
    private Communication communication;
    String[]  tempList  = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.dialog_user_top_picks2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[]  list  = {getResources().getString(R.string.monday), getResources().getString(R.string.tuesday), getResources().getString(R.string.wednesday), getResources().getString(R.string.thursday),getResources().getString(R.string.friday),getResources().getString(R.string.saturday),getResources().getString(R.string.sunday)};
        communication = new Communication(getActivity(), this);
        MaterialSpinner sp_day = view.findViewById(R.id.sp_day);
        sp_day.setItems(list);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        rv_top_pick = view.findViewById(R.id.rv_top_pick);
        rv_top_pick.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Log.e("DAY:::",dayOfTheWeek);
        sp_day.setSelectedIndex(Arrays.asList(tempList).indexOf(dayOfTheWeek.toUpperCase()));

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        sp_day.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getTopPick(Arrays.asList(list).indexOf(item.toString()));

            }
        });

        getTopPick(Arrays.asList(tempList).indexOf(dayOfTheWeek.toUpperCase()));
        super.onViewCreated(view, savedInstanceState);
    }

    private void getTopPick(int dayOfTheWeek) {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "get/top/picks/" + dayOfTheWeek);
        communication.callGET(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.contains("get/top/picks/")) {
            try {
                List<TopPickModel> list = new ArrayList<>();
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray top_picks = data.getJSONArray("top_picks");
                for (int i = 0; i < top_picks.length(); i++) {
                    JSONObject object = top_picks.getJSONObject(i);
                    JSONArray description = object.getJSONArray("description");
                    List<String> msgs = new ArrayList<>();
                    for (int k = 0; k < description.length(); k++) {
                        msgs.add(description.getString(k));
                    }

                    list.add(new TopPickModel(object.getString("display_name"), android.text.TextUtils.join(", ", msgs),object.getString("recommandation_type_id")));

                }

                rv_top_pick.setAdapter(new TopPickAdapter(list));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    private class TopPickAdapter extends RecyclerView.Adapter<TopPickAdapter.ViewHolder> {
        private List<TopPickModel> list;

        private TopPickAdapter(List<TopPickModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_user_top_pick2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_title.setText(list.get(position).getName());
            holder.tv_desc.setText(list.get(position).getMsg());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title, tv_desc;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_desc = itemView.findViewById(R.id.tv_desc);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        listener.onTopPickCLick(list.get(getAdapterPosition()));
                    }
                });
            }

        }
    }


    public class TopPickModel implements Serializable {
        private String name, msg,recommandation_type_id;

        private TopPickModel(String name, String msg, String recommandation_type_id) {
            this.name = name;
            this.msg = msg;
            this.recommandation_type_id = recommandation_type_id;
        }

        public String getName() {
            return name;
        }

        public String getMsg() {
            return msg;
        }

        public String getRecommandation_type_id() {
            return recommandation_type_id;
        }

        public void setRecommandation_type_id(String recommandation_type_id) {
            this.recommandation_type_id = recommandation_type_id;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout( (int)(size.x * 0.9), (int)(size.y * 0.6) );
        window.setGravity( Gravity.CENTER );
    }

    private OnTopClickListener listener;

    public void setListener(OnTopClickListener listener) {
        this.listener = listener;
    }

    public interface OnTopClickListener{
        void onTopPickCLick(TopPickModel pickModel);
    }
}
