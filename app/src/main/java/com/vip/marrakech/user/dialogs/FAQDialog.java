package com.vip.marrakech.user.dialogs;

import android.graphics.Point;
import android.os.Bundle;
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

import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FAQDialog extends DialogFragment implements OnCallBackListener {


    private RecyclerView rv_top_pick;
    private Communication communication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.dialog_user_top_picks, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        TextView tv_day = view.findViewById(R.id.tv_day);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        rv_top_pick = view.findViewById(R.id.rv_top_pick);
        rv_top_pick.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_day.setText(getResources().getString(R.string.faq));

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getFAQ();
        super.onViewCreated(view, savedInstanceState);
    }

    private void getFAQ() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "allFAQs");
        communication.callGET(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("allFAQs")) {
            try {
                List<TopPickModel> list = new ArrayList<>();
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray FAQs = data.getJSONArray("FAQs");
                for (int i = 0; i < FAQs.length(); i++) {
                    JSONObject object = FAQs.getJSONObject(i);
                    list.add(new TopPickModel(object.getString("question"), object.getString("answer")));

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
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_user_top_pick, parent, false));
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
            }
        }
    }


    private class TopPickModel implements Serializable {
        private String name, msg;

        private TopPickModel(String name, String msg) {
            this.name = name;
            this.msg = msg;
        }

        public String getName() {
            return name;
        }

        public String getMsg() {
            return msg;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout( (int)(size.x * 0.9), (int)(size.y * 0.6));
        window.setGravity( Gravity.CENTER );
    }
}
