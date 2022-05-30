package com.vip.marrakech.user.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class FAQFullDialog extends DialogFragment
        implements View.OnClickListener, OnCallBackListener {

    private RecyclerView rv_top_pick;
    private Communication communication;
//    LayoutBottomSheetBinding bi;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_user_top_picks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        Toolbar toolBar = view.findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });

        rv_top_pick = view.findViewById(R.id.rv_top_pick);
        rv_top_pick.setLayoutManager(new LinearLayoutManager(getActivity()));



        getFAQ();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);//solves issue with statusbar
                dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                onBackPress();
            }
        };
    }

    private void onBackPress() {
       /* adapter.notifyDataSetChanged();
        scrollListener.resetState();*/
        dismiss();
    }

    @Override
    public void onClick(View v) {

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
                List<FAQFullDialog.TopPickModel> list = new ArrayList<>();
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray FAQs = data.getJSONArray("FAQs");
                for (int i = 0; i < FAQs.length(); i++) {
                    JSONObject object = FAQs.getJSONObject(i);
                    list.add(new FAQFullDialog.TopPickModel(object.getString("question"), object.getString("answer")));

                }


                rv_top_pick.setAdapter(new FAQFullDialog.TopPickAdapter(list));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    public interface ItemClickListener {
        void onDismiss();

    }

    private class TopPickAdapter extends RecyclerView.Adapter<FAQFullDialog.TopPickAdapter.ViewHolder> {
        private List<FAQFullDialog.TopPickModel> list;

        private TopPickAdapter(List<FAQFullDialog.TopPickModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public FAQFullDialog.TopPickAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FAQFullDialog.TopPickAdapter.ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_user_top_pick, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FAQFullDialog.TopPickAdapter.ViewHolder holder, int position) {
            holder.tv_title.setText(String.format(Locale.ENGLISH,"%d. %s", position+1, list.get(position).getName()));
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
}