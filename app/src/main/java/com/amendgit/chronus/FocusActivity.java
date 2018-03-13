package com.amendgit.chronus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    TickTockView mCountDownView;
    Button mStartButton;
    Button mPauseButton;
    Button mResumeButton;
    Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        mCountDownView = findViewById(R.id.ticktock_countdown_view);
        mCountDownView.setOnTickListener(new TickTockView.OnTickListener() {
            @Override
            public String getText(long timeRemainingInMillis) {
                int s = (int) ( timeRemainingInMillis /  1000)            % 60;
                int m = (int) ((timeRemainingInMillis / (1000 * 60))      % 60);
                int h = (int) ((timeRemainingInMillis / (1000 * 60 * 60)) % 24);
                return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
            }
        });

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long countDownIntervalInMillis = 60 * 1000;
                if (mCountDownView != null) {
                    mCountDownView.start(countDownIntervalInMillis);
                    mStartButton.setVisibility(View.GONE);
                    mPauseButton.setVisibility(View.VISIBLE);
                    mResumeButton.setVisibility(View.GONE);
                    mStopButton.setVisibility(View.GONE);
                }
            }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownView.pause();
                mStartButton.setVisibility(View.GONE);
                mPauseButton.setVisibility(View.GONE);
                mResumeButton.setVisibility(View.VISIBLE);
                mStopButton.setVisibility(View.VISIBLE);
            }
        });

        mResumeButton = findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownView.resume();
                mStartButton.setVisibility(View.GONE);
                mPauseButton.setVisibility(View.VISIBLE);
                mResumeButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.GONE);
            }
        });

        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownView.stop();
                mStartButton.setVisibility(View.VISIBLE);
                mPauseButton.setVisibility(View.GONE);
                mResumeButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDownView.stop();
    }
}
