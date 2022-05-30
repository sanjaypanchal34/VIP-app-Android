package com.vip.marrakech.vendor.dialog;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.vip.marrakech.R;
import com.vip.marrakech.enums.VenueTypes;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.EditOtherBookingActivity;
import com.vip.marrakech.vendor.activities.OtherBookingDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class BookingDetailDialog extends DialogFragment
        implements View.OnClickListener, OnCallBackListener {

    private TextView tv_client, tv_pax, tv_group_type,
            tv_table_name, tv_address, tv_status, tv_final_spend, tv_prebooked_spend, tv_submit;
    private ItemClickListener listener;
    private TextInputEditText ed_final_spend;
    private Communication communication;
    private String bookId = "";
    private Toolbar toolBar;
    private TextView tv_source,tv_day_no;
    private EditText edt_table_no;
    private TextView rd_non_arrived, rd_arrived;
    private boolean isArrived = false;
    private String status;
    private Button btn_cancel;
    private String itinerary_id_encryptedId, itinerary_day_id_encryptedId;
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
        return inflater.inflate(R.layout.booking_detail_dialog, container, false);
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
        btn_cancel.setOnClickListener(this);
        getBookingDetails();

        rd_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("C")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Cancel booking status cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (status.equals("NA")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Status once selected cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (!status.equals("A")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure want to change the status because later you won't be able to change status.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isArrived = true;
                                    status = "A";
                                    //edt_spend.setVisibility(View.VISIBLE);
                                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        rd_non_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("C")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Cancel booking status cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (status.equals("A")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Status once selected cannot be changed.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else if (!status.equals("NA")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure want to change the status because later you won't be able to change status.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isArrived = false;
                                    status = "NA";
                                    //edt_spend.setVisibility(View.VISIBLE);
                                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

    }

    private void initUI(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        tv_client = view.findViewById(R.id.tv_client);
        tv_pax = view.findViewById(R.id.tv_pax);
        tv_group_type = view.findViewById(R.id.tv_group_type);
        tv_table_name = view.findViewById(R.id.tv_table_name);
        tv_address = view.findViewById(R.id.tv_address);
        tv_source = view.findViewById(R.id.tv_source);
        tv_day_no = view.findViewById(R.id.tv_day_no);
        tv_status = view.findViewById(R.id.tv_status);
        tv_final_spend = view.findViewById(R.id.tv_final_spend);
        tv_prebooked_spend = view.findViewById(R.id.tv_prebooked_spend);
        ed_final_spend = view.findViewById(R.id.ed_final_spend);
        edt_table_no = view.findViewById(R.id.edt_table_no);
        tv_submit = view.findViewById(R.id.tv_submit);
        rd_non_arrived = view.findViewById(R.id.rd_non_arrived);
        rd_arrived = view.findViewById(R.id.rd_arrived);
        btn_cancel = view.findViewById(R.id.btn_cancel);
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
        if (view.getId() == R.id.tv_submit) {

            HashMap<String, String> param = new HashMap<>();
            param.put("action", "add/vendor/booking/final/spend");
            param.put("itinerary_day_id", bookId);
            param.put("final_spend", ed_final_spend.getText().toString());
            param.put("table_no", edt_table_no.getText().toString());
            param.put("status", status);
            communication.callPOST(param);

        } else if (view.getId() == R.id.btn_cancel) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure want to cancel the booking?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("action", "vendor/cancel/booking");
                            param.put("itinerary_id", itinerary_id_encryptedId);
                            param.put("itinerary_day_id", itinerary_day_id_encryptedId);
                            communication.callPOST(param);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();


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
                itinerary_id_encryptedId = data.getString("itinerary_id_encryptedId");
                itinerary_day_id_encryptedId = data.getString("itinerary_day_id_encryptedId");
                tv_final_spend.setText(String.format("%s", data.getString("final_spend")));
                tv_prebooked_spend.setText(String.format("%s", data.getString("pre_book_spend")));
                tv_client.append(data.getString("name"));
                //ed_final_spend.setText(String.format("%s", data.getString("final_spend")));
                edt_table_no.setText(data.getString("table_no"));
                tv_pax.append(data.getString("pax"));
                tv_group_type.append(data.getString("group"));
                if (SessionManager.getVenue_Type().equalsIgnoreCase(VenueTypes.NIGHTCLUB.toString()) || SessionManager.getVenue_Type().equalsIgnoreCase(VenueTypes.DAY_PARTIES.toString())) {
                    tv_table_name.append(data.getString("table_name"));
                } else {
                    tv_table_name.append("N/A");
                }
                tv_source.append(data.getString("booking_from"));
                tv_day_no.append(data.optString("day_no"));
                if (data.getString("booking_from").isEmpty()) {
                    tv_source.setVisibility(View.GONE);
                }
                tv_address.setText(String.format("%s\n%s - %s - %s%s", data.getString("start_date"), data.getString("start_time"), data.getString("title"), data.getString("venue_type"), data.getString("pre_book_spend").isEmpty() ? "" : String.format(" - %s", data.getString("pre_book_spend"))));
                status = data.getString("status");
//                itinerary_id = data.getString("itinerary_id_encryptedId");
                btn_cancel.setVisibility(View.INVISIBLE);
                switch (status) {
                    case "CNF":
                        btn_cancel.setVisibility(View.VISIBLE);
                        tv_status.setText(getResources().getString(R.string.confirmed));
                        break;
                    case "NC":
                        btn_cancel.setVisibility(View.VISIBLE);
                        tv_status.setText(getResources().getString(R.string.not_confirmed));
                        break;
                    case "C":
                        btn_cancel.setVisibility(View.INVISIBLE);
                        tv_status.setText(getResources().getString(R.string.cancel));
                        break;
                    case "A":
                        btn_cancel.setVisibility(View.INVISIBLE);
                        tv_status.setText(getResources().getString(R.string.arrived));
                        break;
                    case "NA":
                        btn_cancel.setVisibility(View.VISIBLE);
                        tv_status.setText(getResources().getString(R.string.notarrived));
                        break;
                }


                rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                if (status.equalsIgnoreCase("A")) {
                    isArrived = true;
                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                } else if (status.equalsIgnoreCase("C")) {
                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                } else if (data.getString("status").equalsIgnoreCase("NA")) {
                    isArrived = false;
                    rd_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_off_button, 0, 0, 0);
                    rd_non_arrived.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_radio_on_button, 0, 0, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("add/vendor/booking/final/spend")) {
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            onBackPress();
        } else if (tag.equals("vendor/cancel/booking")) {
            Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            onBackPress();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    public interface ItemClickListener {
        void onDismiss();

    }
}