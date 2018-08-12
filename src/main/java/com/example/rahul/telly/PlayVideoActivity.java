package com.example.rahul.telly;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
public class PlayVideoActivity extends AppCompatActivity
        implements SurfaceHolder.Callback, MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener {
    SurfaceView view;
    SurfaceHolder holder;
    MediaPlayer player;
    MediaController mc;
    int contentLength;
    int bytesPerSeek;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        view = (SurfaceView) findViewById(R.id.sv);

        Intent in = getIntent();
        videoPath = in.getStringExtra("videopath");


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mc.show();
                return false;
            }
        });

        holder = view.getHolder();
        holder.addCallback(this);

        player = new MediaPlayer();
        player.setOnPreparedListener(this);


        mc = new MediaController(this);
        mc.setAnchorView(view);
        mc.setMediaPlayer(this);
        mc.setEnabled(true);
        Log.d("VODMSG","-----Path" + videoPath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    player.setDataSource(Global.global + videoPath);
                    //   player.setDataSource(MyClient.SERVER_DOMAIN+MyClient.APPLICATION_NAME + "/myuploads/2nd hand husband.mkv");



                    player.prepare();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mc.show();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        Log.d("MESSAGE", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        Log.d("MESSAGE", "Duration: " + player.getDuration());
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        Log.d("MESSAGE", "seek to pos " + pos);
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        mc.show();
    }
}

