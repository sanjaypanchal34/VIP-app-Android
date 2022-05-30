package com.vip.marrakech.retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import com.google.android.gms.common.api.Api;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vip.marrakech.helpers.AppController;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.exeptions.NoConnectivityException;
import com.vip.marrakech.retrofit.interfaces.ApiInterface;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.retrofit.models.PART;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communication {

    private AlertDialog internetDialog;
    private Context context;
    private OnCallBackListener listener;
    private KProgressHUD progressDialog;

    public Communication(Context context, OnCallBackListener listener) {
        this.context = context;
        this.listener = listener;


    }

    private void loader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setSize(60, 60)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .show();
    }


    public void callPOST(@NonNull HashMap<String, String> param) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = service.getPost(url, param, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    public void callPOSTWITHCACH(@NonNull HashMap<String, String> param) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            ApiInterface service = ApiClient.getCachClient().create(ApiInterface.class);
            Call<String> stringCall = service.getPost(url, param, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    public void callPOSTWithRetry(@NonNull HashMap<String, String> param) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = service.getPost(url, param, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

/*

    public void callPOST(@NonNull String action) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null){
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        }else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(ApiClient.BASE_URL, action, action, "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }
*/


    public void callPOST(@NonNull HashMap<String, String> param, @NonNull PART part) {

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createMultiPart(part), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    public void callDelete(@NonNull HashMap<String, String> param) {

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getDelete(url, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }


    public void callPOST(@NonNull HashMap<String, String> param, @NonNull List<PART> partList) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createPartList(partList), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }
    public void callPOST(@NonNull HashMap<String, String> param, @NonNull List<PART> partList, @NonNull List<PART> partList1) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createPartList(partList), Params.createPartList(partList), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }


    public void callPOST(@NonNull HashMap<String, String> param, @NonNull PART part, @NonNull List<PART> partList, @NonNull List<PART> partList1) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createMultiPart(part), Params.createPartList(partList), Params.createPartList(partList1), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    public void callPOST(@NonNull HashMap<String, String> param, @NonNull PART part, @NonNull List<PART> partList) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createMultiPart(part), Params.createPartList(partList), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }


    public void callPOST(@NonNull HashMap<String, String> param, @NonNull PART part, @NonNull PART part1, @NonNull List<PART> partList, @NonNull List<PART> partList1) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getPost(url, getParam(param), Params.createMultiPart(part), Params.createMultiPart(part1), Params.createPartList(partList), Params.createPartList(partList1), param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }


    public void  callGET(@NonNull HashMap<String, String> param) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getGet(url, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    public void  GETIP() {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            String url = String.format("https://api.ipify.org/?format=json");
            Call<String> stringCall = service.getGet(url,"IP", "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });

    }

    public void  GETIP6() {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            String url = "https://api64.ipify.org/?format=json";
            Call<String> stringCall = service.getGet(url,"IP6", "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });

    }

    public void callGETRetry(@NonNull HashMap<String, String> param) {
     /*   loader();
        if (progressDialog != null) {
            progressDialog.show();
        }*/
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (param.get("action") == null) {
            listener.OnCallBackError("TAG NOT FOUND", "Are you missing action in param?");
        } else {
            String url = String.format("%s%s", ApiClient.BASE_URL, param.get("action"));
            Call<String> stringCall = service.getGet(url, param.get("action") == null ? "" : param.get("action"), "bharat");
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    onCallBackFail(call, t);
                }
            });
        }
    }

    private void onCallBackSuccess(Call<String> call, @NonNull Response<String> response) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        //Log.e(call.request().header("tag"), response.body());

        if (response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body());
                if (object.has("status")) {
                    if (object.getString("status") .equalsIgnoreCase("1")) {
                        if (listener != null) {
                            try {
                                listener.OnCallBackSuccess(call.request().header("tag"), new JSONObject(response.body()));
                            } catch (JSONException e) {
                                listener.OnCallBackError(call.request().header("tag"), e.getMessage());
                            }
                        }
                    } else {

                        switch (Objects.requireNonNull(call.request().header("tag"))){
                            case "itinerary/day/detail/update":
                            case "vendor/check/availability":
                            case "user/book/now":
                            case "vendor/book/now":
                            case "vendor/edit/other/booking":{
                                listener.OnCallBackError(call.request().header("tag"), object.toString());
                            }break;

                            default: {
                                Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                listener.OnCallBackError(call.request().header("tag"), object.getString("msg"));
                            }
                        }



                    }
                } else {
                    if (listener != null) {
                        try {
                            listener.OnCallBackSuccess(call.request().header("tag"), new JSONObject(response.body()));
                        } catch (JSONException e) {
                            listener.OnCallBackError(call.request().header("tag"), e.getMessage());
                        }
                    }
                }
            } catch (JSONException e) {
                if (listener != null) {
                    listener.OnCallBackError(call.request().header("tag"), e.getMessage());
                }

                writeToFile(e.fillInStackTrace().toString());
            }
        }
    }

    private void writeToFile(String currentStacktrace) {
        try {

            //Gets the Android external storage directory & Create new folder Crash_Reports
            File dir = new File(Environment.getExternalStorageDirectory(),
                    "Crash_Reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String filename = dateFormat.format(date) + "---EncryptedID::::" + SessionManager.getEncryptedID() + "====" + ".STACKTRACE";

            // Write the file into the folder
            File reportFile = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(reportFile);
            String text = "TOKEN:::::" + SessionManager.getToken() + "\n\n\n" + currentStacktrace;
            fileWriter.append(text);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Log.e("ExceptionHandler", e.getMessage());
        }
    }

    private void onCallBackFail(Call<String> call, Throwable t) {


        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (listener != null) {
            listener.OnCallBackError(call.request().header("tag"), t.getMessage());
        }

        if (t instanceof NoConnectivityException) {
            AppController.initNoInternet(context);
            AppController.showInternetDialog(t.getMessage());
        }
    }

    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private HashMap<String, RequestBody> getParam(HashMap<String, String> param) {
        HashMap<String, RequestBody> tempParam = new HashMap<>();
        for (String key : param.keySet()) {
            tempParam.put(key, toRequestBody(param.get(key)));
        }

        return tempParam;
    }


    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void cancel(Object tag) {
        List<okhttp3.Call> list = ApiClient.getHttpClient().build().dispatcher().runningCalls();
        for (okhttp3.Call call :
                list) {
            if (call.request().header("tag") == tag) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        List<okhttp3.Call> list = ApiClient.getHttpClient().build().dispatcher().runningCalls();
        for (okhttp3.Call call :
                list) {
            call.cancel();
        }
    }
}
