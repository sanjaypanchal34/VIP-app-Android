package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.vip.marrakech.R;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class BookingDetailDialog1 extends DialogFragment
        implements View.OnClickListener, OnCallBackListener {

    private TextView tv_client, tv_source, tv_pax, tv_address, tv_final_spend, tv_prebooked_spend, tv_submit;
    private ItemClickListener listener;
    private TextInputEditText ed_final_spend;
    private Communication communication;
    private String bookId = "";
    private Toolbar toolBar;
    private RadioButton rd_accept, rd_reject;
    private boolean isAccept = false;
    private boolean isReject = false;
    private String itinerary_id;
//    LayoutBottomSheetBinding bi;

    public void setData(String bookId, ItemClickListener listener) {
        this.listener = listener;
        this.bookId = bookId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.booking_detail_dialog1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communication = new Communication(getActivity(), this);
        initUI(view);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
        tv_submit.setOnClickListener(this);
        getBookingDetails();
    }

    private void initUI(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        rd_accept = view.findViewById(R.id.rd_accept);
        rd_reject = view.findViewById(R.id.rd_reject);
        tv_client = view.findViewById(R.id.tv_client);
        tv_source = view.findViewById(R.id.tv_source);
        tv_pax = view.findViewById(R.id.tv_pax);
        tv_address = view.findViewById(R.id.tv_address);
        tv_final_spend = view.findViewById(R.id.tv_final_spend);
        tv_prebooked_spend = view.findViewById(R.id.tv_prebooked_spend);
        ed_final_spend = view.findViewById(R.id.ed_final_spend);
        tv_submit = view.findViewById(R.id.tv_submit);


        rd_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itinerary_id == null) {
                    return;
                }
                if (!isAccept && !isReject)
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure want to accept this booking?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("action", "vendor/confirm/booking");
                                    param.put("itinerary_id", itinerary_id);
                                    param.put("itinerary_day_id", bookId);
                                    communication.callPOST(param);

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isAccept = false;
                                    rd_accept.setChecked(false);
                                }
                            })
                            .show();
            }
        });

        rd_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itinerary_id == null) {
                    return;
                }
                if (!isAccept && !isReject) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure want to cancel this booking?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    BookingRejectReasonDialog reasonDialog = new BookingRejectReasonDialog();
                                    reasonDialog.setCancelable(false);
                                    reasonDialog.setListener(new BookingRejectReasonDialog.OnReasonListener() {
                                        @Override
                                        public void onRejectReason(String reason) {
                                            HashMap<String, String> param = new HashMap<>();
                                            param.put("action", "vendor/reject/reason");
                                            param.put("itinerary_id", itinerary_id);
                                            param.put("itinerary_day_id",bookId);
                                            param.put("reason_for_reject", reason);
                                            communication.callPOST(param);
                                        }
                                    });
                                    reasonDialog.show(getChildFragmentManager(),"reason");

                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isReject = false;
                            rd_reject.setChecked(false);
                        }
                    })
                            .show();
                }
            }
        });
    }

    private void getBookingDetails() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("get/vendor/booking/detail/%s", bookId));
        communication.callGET(param);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit: {
                if (ed_final_spend.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Final Spend", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "add/vendor/booking/final/spend");
                    param.put("itinerary_day_id", bookId);
                    param.put("final_spend", ed_final_spend.getText().toString());
                    communication.callPOST(param);
                }
            }
            break;
        }
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
        listener.onDismiss();
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("get/vendor/booking/detail/%s", bookId))) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                tv_final_spend.setText(String.format("%s", data.getString("final_spend")));
                tv_prebooked_spend.setText(String.format("%s", data.getString("pre_book_spend")));
                tv_client.append(data.getString("name"));
                tv_pax.append(data.getString("pax"));
                tv_source.append(data.getString("booking_from"));
                if (data.getString("booking_from").isEmpty()){
                    tv_source.setVisibility(View.GONE);
                }
                tv_address.setText(String.format("%s\n%s - %s - %s - %s", data.getString("start_date"), data.getString("start_time"), data.getString("title"), data.getString("venue_type"), data.getString("total")));
                String status = data.getString("status");
                itinerary_id = data.getString("itinerary_id_encryptedId");
                switch (status) {
                    case "A":
                    case "CNF":
                        isAccept = true;
                        rd_reject.setChecked(false);
                        rd_reject.setEnabled(false);
                        rd_accept.setChecked(true);
                        rd_accept.setEnabled(true);
                        break;
                    case "NA":
                    case "NC":
                        rd_reject.setEnabled(true);
                        rd_accept.setEnabled(true);
                        break;
                    case "C":
                        isReject = true;
                        rd_reject.setChecked(true);
                        rd_reject.setEnabled(true);
                        rd_accept.setChecked(false);
                        rd_accept.setEnabled(false);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("add/vendor/booking/final/spend")) {
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            listener.onDismiss();
            dismiss();
        }else if (tag.equals("vendor/confirm/booking")){
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            isAccept = true;
            rd_accept.setChecked(true);
            rd_reject.setChecked(false);
            rd_accept.setEnabled(false);
            rd_reject.setEnabled(false);
        }else if (tag.equals("vendor/reject/reason")){
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            isReject = true;
            rd_accept.setChecked(false);
            rd_reject.setChecked(true);
            rd_accept.setEnabled(false);
            rd_reject.setEnabled(false);
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    public interface ItemClickListener {
        void onDismiss();

    }
}