package com.amendgit.chronos;

/**
 * Created by jash on 25/03/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FocusNotification extends Service {
    final static String TAG = "FocusNotification";

    private RemoteViews mRemoteViews;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    public FocusNotification() {
        FocusController.getInstance().addDelegate(new FocusController.FocusDelegate() {
            @Override
            public void onTick(long remainMillis) {
                mRemoteViews.setTextViewText(R.id.time_interval_label, TimeUtil.millisToLabel(remainMillis));
                mNotificationManager.notify(1, mNotificationBuilder.build());
            }

            @Override
            public void onFinish() {
                displayStopUI();
            }

            @Override
            public void onStateChange(FocusState state) {
                if (state == FocusState.RUN) {
                    displayPauseUI();
                } else if (state == FocusState.PAUSE) {
                    displayStopUI();
                } else if (state == FocusState.STOP) {
                    displayRunUI();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, intent.getAction(), Toast.LENGTH_LONG).show();

        if (intent.getAction().equals(Constants.ACTION_START_FOREGROUND)) {
            this.startForeground();
        } else if (intent.getAction().equals(Constants.ACTION_START)) {
            FocusController.getInstance().start(Constants.TIME_INTERVAL_FOCUS);
        } else if (intent.getAction().equals(Constants.ACTION_PAUSE)) {
            FocusController.getInstance().pause();
        } else if (intent.getAction().equals(Constants.ACTION_RESUME)) {
            FocusController.getInstance().resume();
        } else if (intent.getAction().equals(Constants.ACTION_STOP)) {
            FocusController.getInstance().stop();
        } else if (intent.getAction().equals(Constants.ACTION_STOP_FOREGROUND)) {
            super.stopForeground(true);
            super.stopSelf();
        }
        return START_STICKY;
    }

    void displayRunUI() {
        mRemoteViews.setViewVisibility(R.id.playGroup, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.pauseGroup, View.GONE);
        mRemoteViews.setViewVisibility(R.id.resumeAndStopGroup, View.GONE);
        mNotificationManager.notify(1, mNotificationBuilder.build());
    }

    void displayPauseUI() {
        mRemoteViews.setViewVisibility(R.id.playGroup, View.GONE);
        mRemoteViews.setViewVisibility(R.id.pauseGroup, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.resumeAndStopGroup, View.GONE);
        mNotificationManager.notify(1, mNotificationBuilder.build());
    }

    void displayStopUI() {
        mRemoteViews.setViewVisibility(R.id.playGroup, View.GONE);
        mRemoteViews.setViewVisibility(R.id.pauseGroup, View.GONE);
        mRemoteViews.setViewVisibility(R.id.resumeAndStopGroup, View.VISIBLE);
        mNotificationManager.notify(1, mNotificationBuilder.build());
    }

    void startForeground() {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_focus);

        Intent playIntent = new Intent(this, FocusNotification.class);
        playIntent.setAction(Constants.ACTION_START);
        PendingIntent startPendingIntent = PendingIntent.getService(this, 0, playIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.start, startPendingIntent);

        Intent pauseIntent = new Intent(this, FocusNotification.class);
        pauseIntent.setAction(Constants.ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.pause_button, pausePendingIntent);

        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_FOCUS);
        Notification notification = mNotificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(mRemoteViews)
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);
    }

    public static void startForeground(Context context) {
        Intent serviceIntent = new Intent(context, FocusNotification.class);
        serviceIntent.setAction(Constants.ACTION_START_FOREGROUND);
        context.startService(serviceIntent);
    }
}