package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.GRecyclerAdapter;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class SearchConciergeDialog extends DialogFragment implements OnCallBackListener {

    private RecyclerView rv_search;
    private LinearLayoutManager layoutManager;
    private List<SearcConciergeModel> list = new ArrayList<>();
    private GRecyclerAdapter<SearcConciergeModel, ViewHolder> adapter;
    private Communication communication;
    private EditText edt_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
      ImageView iv_back = view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });
        edt_search = view.findViewById(R.id.edt_search);
        edt_search.setHint("Search Concierge");
        rv_search = view.findViewById(R.id.rv_search);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_search.setLayoutManager(layoutManager);
        adapter = new GRecyclerAdapter<SearcConciergeModel, ViewHolder>(getActivity(), list) {
            @Override
            public ViewHolder setItemRow(ViewGroup viewGroup, int i) {
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text_spinner, viewGroup, false));
            }

            @Override
            public void myBindViewHolder(final ViewHolder viewHolder, int position, final List<SearcConciergeModel> mList) {
                viewHolder.tv_string.setText(mList.get(position).name);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onConciergeSelect(mList.get(viewHolder.getAdapterPosition()).getId(), mList.get(viewHolder.getAdapterPosition()).getName(), mList.get(viewHolder.getAdapterPosition()).getEmail());
                            onBackPress();
                        }
                    }
                });
            }

            @Override
            public String onFilterWith(SearcConciergeModel item) {
                return item.name;
            }
        };


        rv_search.setAdapter(adapter);
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "get/all/concierge");
        communication.callGET(param);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        try {
            if (jsonObject.getInt("status") == 1) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray all_concierge = data.getJSONArray("all_concierge");
                list.clear();
                for (int i = 0; i < all_concierge.length(); i++) {
                    SearcConciergeModel model = new Gson().fromJson(all_concierge.get(i).toString(), SearcConciergeModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    private class SearcConciergeModel implements Serializable {
        @SerializedName("concierge_id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("email")
        @Expose
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_string;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_string = itemView.findViewById(R.id.tv_string);
        }
    }


    private OnSearchListener listener;

    public void setListener(OnSearchListener listener) {
        this.listener = listener;
    }

    public interface OnSearchListener {
        void onConciergeSelect(String id, String title, String email);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
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
        list.clear();
        adapter.notifyDataSetChanged();
        dismiss();

    }
}
