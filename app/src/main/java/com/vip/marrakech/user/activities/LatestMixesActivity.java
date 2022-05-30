package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.dialogs.ProgressDialog;
import com.vip.marrakech.helpers.AppController;
import com.vip.marrakech.retrofit.ApiClient;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.adapters.MixesAdapter;
import com.vip.marrakech.user.models.MixesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class LatestMixesActivity extends AppCompatActivity implements OnCallBackListener {

    private Communication communication;
    private List<MixesModel> list = new ArrayList<>();
    private MixesAdapter adapter;
    private int resumePosition;
    private ProgressDialog progressDialog;
    private Handler handler;
    private ImageView iv_play_pause;
    private LinearLayout ln_control;
    private int seekSecond = 10;
    private TextView tv_mixes_name;
    private SeekBar seekbar;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (AppController.getExoPlayer() != null) {
                    seekbar.setMax((int) AppController.getExoPlayer().getDuration());
                    System.out.println((int) AppController.getExoPlayer().getCurrentPosition());
                    seekbar.setProgress((int) AppController.getExoPlayer().getCurrentPosition());
                    tv_current.setText(stringForTime((int) AppController.getExoPlayer().getCurrentPosition()));
                    tv_end.setText(stringForTime((int) AppController.getExoPlayer().getDuration()));

            }
            handler.postDelayed(runnable, 1000);
        }
    };
    private TextView tv_current, tv_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_mixes);
        handler = new Handler();
        communication = new Communication(this, this);
        progressDialog = new ProgressDialog();
        progressDialog.setListener(new ProgressDialog.OnProgressCancelListener() {
            @Override
            public void onDownloadCancel() {
                PRDownloader.cancelAll();
            }
        });
        progressDialog.setCancelable(false);


        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView iv_rewind = findViewById(R.id.iv_rewind);
        ImageView iv_forward = findViewById(R.id.iv_forward);
        iv_play_pause = findViewById(R.id.iv_play_pause);
        ImageView iv_stop = findViewById(R.id.iv_stop);
        tv_mixes_name = findViewById(R.id.tv_mixes_name);
        ln_control = findViewById(R.id.ln_control);
        tv_current = findViewById(R.id.tv_current);
        tv_end = findViewById(R.id.tv_end);
        seekbar = findViewById(R.id.seekbar);
        RecyclerView rv_mixes_list = findViewById(R.id.rv_mixes_list);
        rv_mixes_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MixesAdapter(this, list, new MixesAdapter.OnMixesListener() {
            @Override
            public void onMixesClick(final MixesModel model) {
//                progressDialog.setMessage("Downloading Mixes...");
              /*  stopMedia();
                mediaPlayer.reset();*/

                final File file = new File(LatestMixesActivity.this.getFilesDir(), new File(model.getUrl()).getName());
                if (file.exists()) {
                 /*   try {
                        // Set the data source to the mediaFile location
                        mediaPlayer.setDataSource(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.prepareAsync();*/
                    prepareExoPlayerFromFileUri(model, Uri.fromFile(file));

                } else {
                    progressDialog.show(getSupportFragmentManager(), "progress");
                    PRDownloader.download(String.format("%s/audio/%s", ApiClient.HOST, model.getUrl()), LatestMixesActivity.this.getFilesDir().getAbsolutePath(), new File(model.getUrl()).getName())
                            .build()
                            /*.setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                    progressDialog.show(getSupportFragmentManager(), "progress");
                                }
                            })*/
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    progressDialog.dismiss();
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(final Progress progress) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setProgress((int) ((progress.currentBytes * 100) / progress.totalBytes));
                                        }
                                    });
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    adapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                   /* try {
                                        // Set the data source to the mediaFile location
                                        mediaPlayer.setDataSource(file.getAbsolutePath());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaPlayer.prepareAsync();*/
                                    prepareExoPlayerFromFileUri(model, Uri.fromFile(file));
                                }

                                @Override
                                public void onError(Error error) {
                                    progressDialog.dismiss();
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    Toast.makeText(LatestMixesActivity.this, error.isServerError()?error.getServerErrorMessage():error.getConnectionException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });
                }

            }
        });

        rv_mixes_list.setAdapter(adapter);
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "get/all/audio");
        communication.callGET(param);


        iv_play_pause.setOnClickListener(v -> setPlayPause(!AppController.getExoPlayer().isPlaying()));

        iv_rewind.setOnClickListener(v -> AppController.getExoPlayer().seekTo(AppController.getExoPlayer().getCurrentPosition() + (seekSecond * 1000)));
        iv_forward.setOnClickListener(v -> AppController.getExoPlayer().seekTo(AppController.getExoPlayer().getCurrentPosition() + (seekSecond * 1000)));
        iv_stop.setOnClickListener(v -> {
            ln_control.setVisibility(View.GONE);
           if (AppController.getExoPlayer()!=null){
               AppController.getExoPlayer().stop();
           }
            AppController.resetEXO();
        });


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (AppController.getExoPlayer()!=null){
                        AppController.getExoPlayer().seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {
        if (tag.equals("get/all/audio")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray audios = data.getJSONArray("audios");
            list.clear();
            for (int i = 0; i < audios.length(); i++) {
                MixesModel model = new Gson().fromJson(audios.getJSONObject(i).toString(), MixesModel.class);
                list.add(model);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    protected void onResume() {
        if (AppController.getExoPlayer() != null) {
            ln_control.setVisibility(View.VISIBLE);
        }
        if (AppController.getExoPlayer() != null) {
            if (!AppController.getExoPlayer().isPlaying()) {
                iv_play_pause.setImageResource(R.drawable.ic_play_arrow);
            } else {
                iv_play_pause.setImageResource(R.drawable.ic_pause);
            }
        }
        handler.postDelayed(runnable,1000);
        super.onResume();
    }

    private void prepareExoPlayerFromFileUri(MixesModel model, Uri uri) {
        if (AppController.getExoPlayer()!=null){
            AppController.getExoPlayer().stop();
        }
        AppController.initEXO();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(LatestMixesActivity.this,
                Util.getUserAgent(LatestMixesActivity.this, getPackageName()));
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        AppController.getExoPlayer().prepare(mediaSource);
        setPlayPause(true);
        AppController.setLastMixes(model);

        AppController.getExoPlayer().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    ln_control.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_READY) {
                    ln_control.setVisibility(View.VISIBLE);
                    tv_mixes_name.setText(AppController.getLastMixes() == null ? "" : AppController.getLastMixes().getTitle());
                    handler.postDelayed(runnable,1000);
                }
            }
        });
//        initMediaControls();
    }

    private void setPlayPause(boolean play) {
        if (AppController.getExoPlayer() != null) {
            AppController.getExoPlayer().setPlayWhenReady(play);
            if (!play) {
                iv_play_pause.setImageResource(R.drawable.ic_play_arrow);
            } else {
                iv_play_pause.setImageResource(R.drawable.ic_pause);
            }
        }
    }


    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }
}
