package com.github.markszabo.wifree;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

public class CrackService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CRACK = "com.github.markszabo.wifree.action.CRACK";

    public CrackService() {
        super("CrackService");
    }

    public static void startCrackService(Context context) {
        Intent intent = new Intent(context, CrackService.class);
        intent.setAction(ACTION_CRACK);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CRACK.equals(action)) {
                runCrack();
            }
        }
    }

    private void runCrack() {
        makeNotification("Crack started", "for SSID", 0, true);

            for(int i=0; i<1000000000; i++){
                int a = i+1;
                int b = a+i;
                int c = b+a%123;
                if(i%10000000 == 0) {
                    makeNotification("Crack in progress", String.valueOf(i/10000000) + "%", i/10000000, true);
                }
            }
        makeNotification("Crack finished", "13 possible passwords found", 100, false);
    }

    private void makeNotification(String contentTitle, String contentText, int progress, boolean onGoing) {
        // prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, CrackListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setOngoing(onGoing)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setProgress(100,progress,false)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
