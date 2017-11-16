package com.example.miyazakikazuki.mycountdowntimer;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    TextView mTimerText;
    MyCountDownTimer mTimer;
    FloatingActionButton mFab;
    SoundPool mSoundPool;
    int mSoundResId;
    int sound_stop;

    public class MyCountDownTimer extends CountDownTimer{
        public boolean isRunning = false;
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minute = millisUntilFinished / 1000 / 60;
            long second = millisUntilFinished / 1000 % 60;
            mTimerText.setText(String.format("%1d:%2$02d",minute,second));
        }

        @Override
        public void onFinish() {
            mTimerText.setText("0:00");
            sound_stop = mSoundPool.play(mSoundResId,1.0f,1.0f,0,0,1.0f);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaPlayer = MediaPlayer.create(this,R.raw.bellsound);
        mMediaPlayer.setLooping(true);

        mTimerText = (TextView)findViewById(R.id.timer_text);
        mTimerText.setText("3:00");
        mTimer = new MyCountDownTimer(3*60*1000,100);

        mFab = (FloatingActionButton)findViewById(R.id.play_stop);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mTimer.isRunning){
                    mTimer.isRunning = false;
                    mTimer.cancel();
                    mFab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    mSoundPool.stop(sound_stop);
                }else{
                    mTimer.isRunning  = true;
                    mTimer.start();
                    mFab.setImageResource(R.drawable.ic_stop_black_24dp);
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            mSoundPool = new SoundPool(2, AudioManager.STREAM_ALARM,0);
        }else{
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }

        mSoundResId = mSoundPool.load(this,R.raw.bellsound,1);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSoundPool.release();
    }
}
