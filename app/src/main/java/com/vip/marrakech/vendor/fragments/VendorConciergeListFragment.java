package com.vip.marrakech.vendor.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.vip.marrakech.vendor.activities.AddConciergeActivity;
import com.vip.marrakech.vendor.activities.OtherBookingActivity;
import com.vip.marrakech.vendor.adapters.ConciergeAdapter;
import com.vip.marrakech.vendor.dialog.ProfilePasswordDialog;
import com.vip.marrakech.vendor.dialog.SetProfilePasswordDialog;
import com.vip.marrakech.vendor.models.ConciergeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorConciergeListFragment extends Fragment implements OnCallBackListener, OtherBookingActivity.OnFragmentViewPagerChangeListener, Toolbar.OnMenuItemClickListener, ProfilePasswordDialog.OnPasswordConfirmListener, SetProfilePasswordDialog.OnProfilePasswordSetListener {


    private List<ConciergeModel> list = new ArrayList<>();
    private ConciergeAdapter adapter;
    private Communication communication;
    private ProfilePasswordDialog passwordDialog;
    private SetProfilePasswordDialog setPasswordDialog;

    public VendorConciergeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_concierage_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        initUI(view);


        super.onViewCreated(view, savedInstanceState);
    }

    private void getList() {

        if (SessionManager.isVendorConfirmPassword() && SessionManager.isProfilePasswordSet() == 1) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "get/all/concierge");
            if (communication != null)
                communication.callGET(param);

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
        Toolbar toolBar = view.findViewById(R.id.toolBar);
        toolBar.setOnMenuItemClickListener(this);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        RecyclerView rv_c_commission = view.findViewById(R.id.rv_c_commission);
        rv_c_commission.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ConciergeAdapter(getActivity(), list);
        adapter.setListener(new ConciergeAdapter.OnConciergeListener() {
            @Override
            public void onConciergeClick(ConciergeModel model) {

            }

            @Override
            public void onConciergeEdit(ConciergeModel model) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                GoTo.startWithExtra(getActivity(), AddConciergeActivity.class, bundle);
            }

            @Override
            public void onConciergeDelete(final ConciergeModel model) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure want to delete this Concierge?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("action", "concierge/delete/" + model.getEncryptedConciergeId());
                                communication.callDelete(param);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });
        rv_c_commission.setAdapter(adapter);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        try {
            if (tag.equals("get/all/concierge")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray comission_list = data.getJSONArray("all_concierge");
                list.clear();
                for (int i = 0; i < comission_list.length(); i++) {
                    ConciergeModel model = new Gson().fromJson(comission_list.getJSONObject(i).toString(), ConciergeModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            } else if (tag.contains("concierge/delete/")) {
                Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                getList();
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
        getList();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_add) {
            if (SessionManager.isVendorConfirmPassword() && SessionManager.isProfilePasswordSet() == 1) {
                GoTo.start(getActivity(), AddConciergeActivity.class);
            }else if (getUserVisibleHint()) {

                if (SessionManager.isProfilePasswordSet() == 1) {
                    if (passwordDialog != null && passwordDialog.isVisible()) {
                        passwordDialog.dismiss();
                    }
                    passwordDialog = new ProfilePasswordDialog();
                    passwordDialog.setListener(new ProfilePasswordDialog.OnPasswordConfirmListener() {
                        @Override
                        public void onPasswordConfirm() {
                            GoTo.start(getActivity(), AddConciergeActivity.class);
                        }
                    });
                    passwordDialog.show(getChildFragmentManager(), "confirm Profile password");
                } else {
                    if (setPasswordDialog != null && setPasswordDialog.isVisible()) {
                        setPasswordDialog.dismiss();
                    }
                    setPasswordDialog = new SetProfilePasswordDialog();
                    setPasswordDialog.setListener(new SetProfilePasswordDialog.OnProfilePasswordSetListener() {
                        @Override
                        public void onProfilePassword() {
                            GoTo.start(getActivity(), AddConciergeActivity.class);
                        }
                    });
                    setPasswordDialog.show(getChildFragmentManager(), "set Profile password");
                }
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        list.clear();
        adapter.notifyDataSetChanged();
        getList();
        super.onResume();
    }

    @Override
    public void onPasswordConfirm() {
        SessionManager.setVendorConfirmPassword(true);
        getList();

    }

    @Override
    public void onProfilePassword() {
        SessionManager.setVendorConfirmPassword(true);
        getList();

    }
}
