package com.amendgit.chronos;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    private TickTockView mCountDownView;
    private Button mStartButton;
    private Button mPauseButton;
    private Button mResumeButton;
    private Button mStopButton;

    final String TAG = "FocusActivity";
    final String NOTIFICATION_CHANNEL_ID = "focus_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_focus);

        mCountDownView = super.findViewById(R.id.ticktock_countdown_view);
        mCountDownView.setOnTickDelegate(new TickTockView.TickTockDelegate() {
            @Override
            public String getTickText(long timeRemainingInMillis) {
                int s = (int) ( timeRemainingInMillis /  1000)            % 60;
                int m = (int) ((timeRemainingInMillis / (1000 * 60))      % 60);
                int h = (int) ((timeRemainingInMillis / (1000 * 60 * 60)) % 24);
                return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
            }

            @Override
            public void onTickFinish() {
                onStopFocus();
                showFocusFinishedNotification();
            }
        });

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onStartFocus(); }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onPauseFocus(); }
        });

        mResumeButton = findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onResumeFocus(); }
        });

        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onStopFocus(); }
        });
    }

    void onStartFocus() {
        long countDownIntervalInMillis = 10 * 1000;
        mCountDownView.start(countDownIntervalInMillis);
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void onPauseFocus() {
        mCountDownView.pause();
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    void onResumeFocus() {
        mCountDownView.resume();
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void onStopFocus() {
        mCountDownView.stop();
        mStartButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void showFocusFinishedNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_focus);

        NotificationManager notificationManager = (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = builder
                .setContentTitle("专注")
                .setContentText("一段美好的专注时光已经完成")
                .setContent(remoteViews)
                .setAutoCancel(true)
                // .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beat))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(1, notification);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beat);
        mediaPlayer.start();
    }
}
