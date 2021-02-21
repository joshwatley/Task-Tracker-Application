package com.example.goaltrackerapplication;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotificationIntentService extends IntentService {

    public static NotificationManager notificationManager;


    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";


    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "motivationchannel";
            CharSequence name = "NotificationChannel";
            String Description = "This is my notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        List<String> motivations = new ArrayList<>();
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        // get list of motivations
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        motivations = dataBaseHelper.getMotivations();
        Random r = new Random();
        motivations.size();
        int i1 = r.nextInt(4 - 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "motivationchannel");
        builder.setContentTitle("Daily Motivation!")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("''" + motivations.get(i1 * 2) + "''" + "  - " + motivations.get((i1*2) + 1))
                .setSmallIcon(R.drawable.ic_goals);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, MainHome.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}