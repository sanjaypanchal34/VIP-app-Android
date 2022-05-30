package com.vip.marrakech.admin.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminItineraryDetailActivity;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.admin.activities.AdminNotificationActivity;
import com.vip.marrakech.admin.activities.AdminPromotionLIst;
import com.vip.marrakech.admin.activities.AdminRecommandationActivity;
import com.vip.marrakech.admin.activities.CopyIteniraryActivity;
import com.vip.marrakech.admin.adapters.AdminItineraryAdapter;
import com.vip.marrakech.admin.adapters.NewAllAdminBookingAdapter;
import com.vip.marrakech.admin.adapters.NewAllAdminItineriryAdapter;
import com.vip.marrakech.admin.models.AdminExpandBooking;
import com.vip.marrakech.admin.models.AdminExpandedIntenariry;
import com.vip.marrakech.admin.models.AdminItineraryModel;
import com.vip.marrakech.admin.models.NewAdminBookingModel;
import com.vip.marrakech.admin.models.NewAdminItinareryModel;
import com.vip.marrakech.helpers.EndlessRecyclerOnScrollListener;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.PaginationListener;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminItineraryFragment extends Fragment implements OnCallBackListener, AdminMainActivity.OnFragmentViewPagerChangeListener, Toolbar.OnMenuItemClickListener {

    private RecyclerView rv_itinerary;
    private List<NewAdminItinareryModel> list = new ArrayList<>();
    private NewAllAdminItineriryAdapter adapter;
    private Communication communication;
    private Toolbar toolBar;
    private ImageView iv_notification;
    private TextView tv_notification_count;
    private LinearLayoutManager layoutManager;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_itinerary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        toolBar = view.findViewById(R.id.toolBar);
        toolBar.setOnMenuItemClickListener(this);
        communication = new Communication(getActivity(), this);
        rv_itinerary = view.findViewById(R.id.rv_itinerary);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_itinerary.setLayoutManager(layoutManager);
         scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {


            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage = page;
                isLoading = true;
                getItinerary(currentPage);
            }
        };
        /*rv_itinerary.addOnScrollListener(scrollListener);*/
        adapter = new NewAllAdminItineriryAdapter(getActivity(), list, new AdminItineraryAdapter.OnItineraryListener() {
            @Override
            public void onItineraryClick(AdminItineraryModel model) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", model);
                GoTo.startWithExtra(getActivity(), AdminItineraryDetailActivity.class, bundle);
            }

            @Override
            public void onItineraryCopyClick(AdminItineraryModel model) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure want to copy this Itinerary?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", model);
                                GoTo.startWithExtra(getActivity(), CopyIteniraryActivity.class, bundle);
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }

            @Override
            public void onItineraryDeleteClick(AdminItineraryModel model) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure want to delete this Itinerary?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("action", "itinerary/delete/" + model.getEncryptedId());
                                communication.callDelete(param);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        adapter.setListener(new NewAllAdminItineriryAdapter.OnAllBookingListener() {
            @Override
            public void onHeaderClick(NewAdminItinareryModel allBookingModel) {
                for (NewAdminItinareryModel bookingModel :
                        list) {
                    if (bookingModel.getName().equals(allBookingModel.getName())) {
                        bookingModel.setVisible(true);
                        if(bookingModel.getBookingList().size()>0){
                            bookingModel.getBookingList().get(0).setVisible(true);
                        }
                    } else {
                        for (AdminExpandedIntenariry expandBooking : bookingModel.getBookingList()) {
                            expandBooking.setVisible(bookingModel.getName().equalsIgnoreCase("Today"));
                        }
                        bookingModel.setVisible(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        rv_itinerary.setAdapter(adapter);


        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        ImageView gift = (ImageView) toolBar.getMenu().findItem(R.id.opt_gift).getActionView();
        ImageView recomndation = (ImageView) toolBar.getMenu().findItem(R.id.opt_recommandation).getActionView();
        notification.setOnClickListener(customMenuListener);
        gift.setOnClickListener(customMenuListener);
        recomndation.setOnClickListener(customMenuListener);
        super.onViewCreated(view, savedInstanceState);
    }


    private View.OnClickListener customMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMenuItemClick(toolBar.getMenu().findItem(v.getId()));
        }
    };

    private void getItinerary(int page) {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "get/all/itinerary");
        param.put("page", String.valueOf(page));
        communication.callPOST(param);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("get/all/itinerary")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray bookingsToday = data.getJSONArray("today");
                JSONArray bookingsPast = data.getJSONArray("past");
                JSONArray bookingsFuture = data.getJSONArray("future");
                list.clear();
                List<AdminExpandedIntenariry> todayBooking = new ArrayList<>();
                List<AdminExpandedIntenariry> pastBooking = new ArrayList<>();
                List<AdminExpandedIntenariry> futureBooking = new ArrayList<>();
                for (int i = 0; i < bookingsToday.length(); i++) {
                    AdminExpandedIntenariry model = new Gson().fromJson(bookingsToday.getJSONObject(i).toString(), AdminExpandedIntenariry.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    if (model.getDate().equals(dateFormat.format(new Date()))) {
                        model.setVisible(true);
                    }
                    todayBooking.add(model);

                }

                for (int i = 0; i < bookingsFuture.length(); i++) {
                    AdminExpandedIntenariry model = new Gson().fromJson(bookingsFuture.getJSONObject(i).toString(), AdminExpandedIntenariry.class);
                    if (i == 0 && todayBooking.size() == 0) {
                        model.setVisible(true);
                    }
                    futureBooking.add(model);

                }

                for (int i = 0; i < bookingsPast.length(); i++) {
                    AdminExpandedIntenariry model = new Gson().fromJson(bookingsPast.getJSONObject(i).toString(), AdminExpandedIntenariry.class);
                    if(i==0 && todayBooking.size()==0 && futureBooking.size()==0){
                        model.setVisible(true);
                    }
                    pastBooking.add(model);

                }


                list.add(new NewAdminItinareryModel("Past", pastBooking, todayBooking.size()==0 && futureBooking.size()==0, data.getString("past_pax")));
                list.add(new NewAdminItinareryModel("Today", todayBooking, todayBooking.size() != 0, data.getString("today_pax")));
                list.add(new NewAdminItinareryModel("Future", futureBooking, todayBooking.size() == 0, data.getString("future_pax")));

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            list.clear();
            isLastPage = false;
            currentPage = 1;
            scrollListener.resetState();
            adapter.notifyDataSetChanged();
            getItinerary(1);
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_recommandation) {
            GoTo.start(getActivity(), AdminRecommandationActivity.class);
        } else if (item.getItemId() == R.id.opt_gift) {
            GoTo.start(getActivity(), AdminPromotionLIst.class);
        } else if (item.getItemId() == R.id.opt_notification) {
            GoTo.start(getActivity(), AdminNotificationActivity.class);
        }
        return false;
    }


    @Override
    public void onResume() {
        list.clear();
        currentPage = 1;
        scrollListener.resetState();
        isLastPage = false;
        adapter.notifyDataSetChanged();
        getItinerary(1);
        super.onResume();
    }
}
