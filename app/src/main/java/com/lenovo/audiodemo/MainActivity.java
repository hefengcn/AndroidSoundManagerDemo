package com.lenovo.audiodemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "MainActivity";
    private SeekBar mSeekBar;
    private MediaPlayer player;
    private boolean mFaceFlag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = (Button) findViewById(R.id.bt_play);
        Button pause = (Button) findViewById(R.id.tb_pause);
        Button replay = (Button) findViewById(R.id.bt_replay);
        Button stop = (Button) findViewById(R.id.bt_stop);
        Button face = (Button) findViewById(R.id.face_control);
        Button channelLeft = (Button) findViewById(R.id.channel_left);
        Button channelRight = (Button) findViewById(R.id.channel_right);
        Button channelDefault = (Button) findViewById(R.id.channel_default);
        mSeekBar = (SeekBar) findViewById(R.id.sb_balance);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        stop.setOnClickListener(this);
        face.setOnClickListener(this);
        channelLeft.setOnClickListener(this);
        channelRight.setOnClickListener(this);
        channelDefault.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sensetime.senseme.humanaction.faceaction");
        registerReceiver(handActionReceiver, intentFilter);
        player = MediaPlayer.create(this, R.raw.beijingbeijing);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(handActionReceiver);
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private final BroadcastReceiver handActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("positionvalue", -1);
            Log.d(TAG, "position = " + position);
            Log.d(TAG, "mSeekBar.getMax() = " + mSeekBar.getMax());
            if (!mFaceFlag) return;
            if (position < 20) {
                if (player != null)
                    player.setVolume(0, 1);
                mSeekBar.setProgress(mSeekBar.getMax());
            } else if (position < 400) {
                if (player != null)
                    player.setVolume(position / 400.0f, (400.0f - position) / 400.0f);
                mSeekBar.setProgress((400 - position) / 4);
            } else {
                if (player != null)
                    player.setVolume(1, 0);
                mSeekBar.setProgress(0);
            }

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play:
                if (player == null)
                    player = MediaPlayer.create(this, R.raw.beijingbeijing);
                player.start();
                player.setVolume(1, 0);
                break;
            case R.id.tb_pause:
                if (player != null)
                    player.pause();
                break;
            case R.id.bt_replay:
                if (player != null)
                    player.start();
                break;
            case R.id.bt_stop:
                if (player != null)
                    player.stop();
                player = null;
                break;
            case R.id.channel_left:
                mFaceFlag = false;
                if (player != null)
                    player.setVolume(0, 1);
                mSeekBar.setProgress(0);

                break;
            case R.id.channel_right:
                mFaceFlag = false;
                if (player != null)
                    player.setVolume(1, 0);
                mSeekBar.setProgress(mSeekBar.getMax());
                break;
            case R.id.face_control:
                mFaceFlag = true;
                break;
            case R.id.channel_default:
                mFaceFlag = false;
                if (player != null)
                    player.setVolume(0.5f, 0.5f);
                mSeekBar.setProgress(mSeekBar.getMax() / 2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int max = seekBar.getMax();
        Log.d(TAG, "max = " + max);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
