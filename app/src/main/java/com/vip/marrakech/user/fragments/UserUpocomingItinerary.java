package com.vip.marrakech.user.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vip.marrakech.R;
import com.vip.marrakech.activities.LoginActivity;
import com.vip.marrakech.helpers.EndlessRecyclerOnScrollListener;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.PaginationListener;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.user.models.UserItinararyModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.exeptions.NetworkUtil;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.IternityDetailActivity;
import com.vip.marrakech.user.adapters.UserItineraryAdapter;
import com.vip.marrakech.user.models.UerItinareryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserUpocomingItinerary extends Fragment implements OnCallBackListener {
    private RecyclerView rv_itinerary;
    private UserItineraryAdapter adapter;
    private List<UserItinararyModel> list = new ArrayList<>();
    private Communication communication;
    private TextView tv_no_data;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_itinerary_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        rv_itinerary = view.findViewById(R.id.rv_itinerary);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_itinerary.setLayoutManager(layoutManager);

         scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {


            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage = page;
                isLoading = true;
                getList();
            }
        };
        rv_itinerary.addOnScrollListener(scrollListener);
        adapter = new UserItineraryAdapter(getActivity(), list);
        adapter.setListener(new UserItineraryAdapter.OnItineraryListener() {
            @Override
            public void OnItineraryClick(UserItinararyModel model) {
                Intent intent = new Intent(getActivity(), IternityDetailActivity.class);
                intent.putExtra("data", model);
                intent.putExtra("iaPast", false);
                startActivity(intent);
            }
        });
        rv_itinerary.setAdapter(adapter);


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        list.clear();
        scrollListener.resetState();
        adapter.notifyDataSetChanged();
        currentPage = 1;
        getList();
        super.onResume();
    }

    private void getList() {
        if( !SessionManager.isLogged() ){
            if (adapter.getItemCount() == 0) {
                tv_no_data.setVisibility(View.VISIBLE);
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
        }else
        if (NetworkUtil.isOnline(getActivity()) ) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "get/user/itinerary");
            param.put("is_past", "0");
            param.put("page", String.valueOf(currentPage));
            communication.callPOST(param);
        } else {
            JSONObject data = null;
            try {
                data = new JSONObject(SessionManager.getUpcommingItinerary());
                list.clear();
                JSONArray today = data.getJSONArray("today");
                JSONArray itineraries = data.getJSONArray("itinerary");
                for (int i = 0; i < today.length(); i++) {
                    UserItinararyModel model = new Gson().fromJson(today.getJSONObject(i).toString(), UserItinararyModel.class);
                    list.add(model);
                }
               /* for (int i = 0; i < itineraries.length(); i++) {
                    UserItinararyModel model = new Gson().fromJson(itineraries.getJSONObject(i).toString(), UserItinararyModel.class);
                    list.add(model);
                }*/
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() == 0) {
                    tv_no_data.setVisibility(View.VISIBLE);
                } else {
                    tv_no_data.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("get/user/itinerary")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray today = data.getJSONArray("today");
                JSONArray itineraries = data.getJSONArray("itinerary");
                for (int i = 0; i < today.length(); i++) {
                    UserItinararyModel model = new Gson().fromJson(today.getJSONObject(i).toString(), UserItinararyModel.class);
                    list.add(model);
                }
                for (int i = 0; i < itineraries.length(); i++) {
                    UserItinararyModel model = new Gson().fromJson(itineraries.getJSONObject(i).toString(), UserItinararyModel.class);
                    list.add(model);
                }
                if (currentPage == 1) {
                    SessionManager.setUpcommingItinerary(data.toString());
                }
                if (today.length() == 0 && itineraries.length() == 0) {
                    isLastPage = true;
                }

                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() == 0) {
                    tv_no_data.setVisibility(View.VISIBLE);
                } else {
                    tv_no_data.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
