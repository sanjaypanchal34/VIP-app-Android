package com.vip.marrakech.vendor.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.activities.VendorMainActivity;
import com.vip.marrakech.vendor.activities.VendorNotificationActivity;
import com.vip.marrakech.vendor.dialog.TableDetailDialog;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SacnFragment extends Fragment implements VendorMainActivity.OnFragmentViewPagerChangeListener, OnCallBackListener {


    private Communication communication;
    private CodeScanner mCodeScanner;
    private ImageView iv_notification;
    private TextView tv_notification_count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sacn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        communication = new Communication(getActivity(), this);
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                initUI(view);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    Toast.makeText(getActivity(), "Camera Permission is required for scanner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
        Toolbar toolBar = view.findViewById(R.id.toolBar);

        RelativeLayout notification = (RelativeLayout) toolBar.getMenu().findItem(R.id.opt_notification).getActionView();
        iv_notification = notification.findViewById(R.id.iv_notification);
        tv_notification_count = notification.findViewById(R.id.tv_notification_count);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.start(getActivity(), VendorNotificationActivity.class);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    private void callApi(String[] array, boolean b) {

        HashMap<String, String> param = new HashMap<>();

        param.put("action", "read/QR/code");
        if (b) {
            param.put("unique_id", array[1]);
            param.put("itinerary_id", array[2]);
            param.put("user_id", array[3]);
        } else {
            param.put("other_booking_id", array[1]);

        }
        param.put("vendor_id", SessionManager.getEncryptedID());
        param.put("type", array[0]);
        communication.callPOST(param);
    }

    private void initUI(View view) {
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Log.e("SCANEEEE:::",result.getText());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] array = result.getText().split("_");
                        if (array.length > 0) {
                            if (array[0].equals("itinerary")) {
                                callApi(array, true);
                            } else if (array[0].equals("other")) {
                                callApi(array, false);
                            }
                        }
                       /* if (array.length == 4){
                            callApi(array);
                        }*/
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRefresh(int page, String name, String notification_count, boolean b) {
        if (mCodeScanner != null && !mCodeScanner.isPreviewActive() && getUserVisibleHint()) {
            mCodeScanner.startPreview();

        }
        if (tv_notification_count != null && !notification_count.equals("0")) {
            tv_notification_count.setVisibility(View.VISIBLE);
            tv_notification_count.setText(notification_count);
        } else if (tv_notification_count != null) {
            tv_notification_count.setVisibility(View.GONE);
        }

    }

    private void getPermission() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getActivity(), "Camera Permission Required to scan QR code", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("read/QR/code")) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(jsonObject.optString("msg"))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TableDetailDialog detailDialog = new TableDetailDialog();
                            detailDialog.setData(jsonObject);
                            detailDialog.setCancelable(false);
                            detailDialog.setListener(new TableDetailDialog.OnTableEditListener() {
                                @Override
                                public void onTableEdit() {
                                    listener.onScanCompleted();
                                }
                            });
                            detailDialog.show(getChildFragmentManager(), "table detail");
                        }
                    })
                    .show();


        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {
        new AlertDialog.Builder(getActivity())
                .setMessage(error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private OnScanCompleteListener listener;

    public void setListener(OnScanCompleteListener listener) {
        this.listener = listener;
    }

    public interface OnScanCompleteListener {
        void onScanCompleted();
    }


}
