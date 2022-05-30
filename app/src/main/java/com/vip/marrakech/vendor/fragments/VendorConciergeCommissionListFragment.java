package com.vip.marrakech.vendor.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.ConciergeDetailActivity;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.adapters.ConciergeCommissionAdapter;
import com.vip.marrakech.vendor.dialog.FilterDialogCommission;
import com.vip.marrakech.vendor.dialog.ProfilePasswordDialog;
import com.vip.marrakech.vendor.dialog.SetProfilePasswordDialog;
import com.vip.marrakech.vendor.models.ConciergeCommissionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorConciergeCommissionListFragment extends Fragment implements OnCallBackListener, OtherBookingActivity.OnFragmentViewPagerChangeListener, Toolbar.OnMenuItemClickListener, ProfilePasswordDialog.OnPasswordConfirmListener, SetProfilePasswordDialog.OnProfilePasswordSetListener {


    private RecyclerView rv_c_commission;
    private List<ConciergeCommissionModel> list = new ArrayList<>();
    private ConciergeCommissionAdapter adapter;
    private Communication communication;
    private String filter = "", commission_title = "", concierge_id;
    private ProfilePasswordDialog passwordDialog;
    private SetProfilePasswordDialog setPasswordDialog;

    public VendorConciergeCommissionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_concierage_commission_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void getList(HashMap<String, String> param) {


        if (SessionManager.isVendorConfirmPassword() && SessionManager.isProfilePasswordSet() == 1) {
            if (concierge_id != null) {
                param.put("concierge_id", concierge_id);
            }
            param.put("action", "vendor/concierge/comission/list");
            param.put("venue_type", SessionManager.getVenue_Type());
            param.put("filter", filter.isEmpty() && concierge_id == null   ? "0" : "1");
            param.put("all_booking", filter);
            Log.e("COMMISSION_FILTER:::", param.toString());
            communication.callPOST(param);
        } else if (getUserVisibleHint()) {

            if (SessionManager.isProfilePasswordSet() == 1) {
                if (passwordDialog != null && passwordDialog.isVisible()) {
                    passwordDialog.dismiss();
                }
                passwordDialog = new ProfilePasswordDialog();
                passwordDialog.setListener(this);
                passwordDialog.show(getChildFragmentManager(), "confirm Profile password");
            } else {
                if (setPasswordDialog != null && setPasswordDialog.isVisible()) {
                    setPasswordDialog.dismiss();
                }
                setPasswordDialog = new SetProfilePasswordDialog();
                setPasswordDialog.setListener(this);
                setPasswordDialog.show(getChildFragmentManager(), "set Profile password");
            }
        }
    }

    private void initUI(View view) {
        final Toolbar toolBar = view.findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
        rv_c_commission = view.findViewById(R.id.rv_c_commission);
        rv_c_commission.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ConciergeCommissionAdapter(getActivity(), list);
        adapter.setListener(new ConciergeCommissionAdapter.OnBookingListener() {
            @Override
            public void onBookingClick(ConciergeCommissionModel model) {
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getConciergeId());
                GoTo.startWithExtra(getActivity(), ConciergeDetailActivity.class, bundle);
            }
        });
        rv_c_commission.setAdapter(adapter);

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        try {
            if (tag.equals("vendor/concierge/comission/list")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray comission_list = data.getJSONArray("comission_list");
                list.clear();
                for (int i = 0; i < comission_list.length(); i++) {
                    ConciergeCommissionModel model = new Gson().fromJson(comission_list.getJSONObject(i).toString(), ConciergeCommissionModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onRefresh(int page, String name) {
        getList(new HashMap<String, String>());

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_filter) {
            FilterDialogCommission filterDialogCommission = new FilterDialogCommission();
            filterDialogCommission.setData(filter, concierge_id, commission_title, new FilterDialogCommission.ItemClickListener() {
                @Override
                public void onItemClick(String booking, String id, String title) {
                    filter = booking;
                    concierge_id = id;
                    commission_title = title;
                    getList(new HashMap<String, String>());
                }

                @Override
                public void onItemClick(String booking, String id, String title, String startDate, String endDate) {
                    filter = booking;
                    concierge_id = id;
                    commission_title = title;
                    HashMap<String, String> param = new HashMap<>();
                    param.put("from_date", startDate);
                    param.put("to_date", endDate);
                    getList(param);
                }
            });
            filterDialogCommission.show(getChildFragmentManager(), "filter_commission");
        }
        return false;
    }

    @Override
    public void onPasswordConfirm() {
        SessionManager.setVendorConfirmPassword(true);
        getList(new HashMap<String, String>());
    }

    @Override
    public void onProfilePassword() {
        SessionManager.setVendorConfirmPassword(true);
        getList(new HashMap<String, String>());
    }

    @Override
    public void onResume() {
        list.clear();
        adapter.notifyDataSetChanged();
        getList(new HashMap<String, String>());
        Log.e("Resume::","Commision");
        super.onResume();
    }
}
