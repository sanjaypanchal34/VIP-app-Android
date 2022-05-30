package com.vip.marrakech.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadHelper {

    private AsyncTask<Void, Void, File> Task;

    public DownloadHelper(final Context context, final DownloadHelper.OnDownloaderListener listener, final String url) {
        Dexter.withActivity((Activity) context).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){
                    Task = new AsyncTAskClass(context, listener, url);
                    Task.execute();
                }else {
                    Toast.makeText(context, "Permission Needed to download", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
            }
        }).check();
    }

    public void cancel() {
        Task.cancel(true);
    }


    @SuppressLint("StaticFieldLeak")
    static
    class AsyncTAskClass extends android.os.AsyncTask<Void, Void, File> {

        private Context context;
        private String link;
        private OnDownloaderListener listener;
        private File file;

        AsyncTAskClass(Context context, OnDownloaderListener listener, String url) {
            this.listener = listener;
            this.link = url;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreTask();
        }

        @Override
        protected File doInBackground(Void... voids) {
            int count;
            try {
                File folder = new File(context.getFilesDir(), "musics");
                folder.mkdir();
                file = new File(folder.getAbsolutePath(), new File(link).getName());

                if (file.exists()) {

                    URL url = new URL(link);
                    URLConnection conection = url.openConnection();
                    int contentLength = conection.getContentLength();
                    if (contentLength != file.length()) {
                        DataInputStream stream = new DataInputStream(url.openStream());

                        byte[] buffer = new byte[contentLength];
                        Log.e("CONTEnt:::", String.valueOf(buffer.length));
                        stream.readFully(buffer);
                        stream.close();

                        DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
                        fos.write(buffer);
                        fos.flush();
                        fos.close();
                    }
                } else {
                    URL url = new URL(link);
                    URLConnection conection = url.openConnection();
                    int contentLength = conection.getContentLength();
                    DataInputStream stream = new DataInputStream(url.openStream());
                    DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));

                    byte[] buffer = new byte[1024];

                    long total = 0;

                    while ((count = stream.read(buffer)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress((int)((total*100)/contentLength));

                        // writing data to file
                       // stream.readFully(buffer, 0, count);
                        fos.write(buffer, 0, count);

                    }

                    Log.e("CONTEnt:::", String.valueOf(buffer.length));
                 /*   stream.readFully(buffer);*/
                    stream.close();


                    fos.flush();
                    fos.close();
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            Log.e("Downloaded:::", String.valueOf(file.length()));
            return file;
        }

        private void publishProgress(int s) {
            listener.onProgress(s, link);
        }

        @Override
        protected void onPostExecute(File o) {
            super.onPostExecute(o);
            listener.onPostTask(o, link);
        }
    }

    public interface OnDownloaderListener {

        void onPreTask();

        void onPostTask(File file, String url);

        void onProgress(int progress, String url);
    }
}
