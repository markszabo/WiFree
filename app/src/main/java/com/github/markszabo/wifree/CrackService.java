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
        WifiNetwork[] networks = CrackList.getListFromDb(getApplicationContext());
        WifiNetwork net;
        for(int i=0; i<networks.length; i++) {
            net = networks[i];
            makeNotification(i, "Crack started", "for UPC" + net.SSID, 0, true);
            Crack c = new Crack(net.SSID, net.BSSID);
            String PSK;
            int sn;
            for(sn=net.serialNumber; sn <= 260000000; sn++) { //serialnumber goes from 200.000.000 to 260.000.000 according to cisco_psk.py
                PSK = c.getPSK(sn);
                if(PSK != null) {
                    net.possiblePasswords.add(PSK);
                }
                if((sn-200000000)%6000 == 0) {
                    makeNotification(i, "Crack in progress for UPC" + net.SSID, String.format("%d.%02d%%",(sn-200000000)/600000,(sn-200000000)/6000%100) , (sn-200000000)/600000, true);
                }
                if((sn-200000000)%60000 == 0) {
                    CrackList.updateListInDb(getApplicationContext(), net.BSSID, sn, net.getPossiblePasswordsAsString());
                }
            }
            CrackList.updateListInDb(getApplicationContext(), net.BSSID, sn, net.getPossiblePasswordsAsString()); //always save in the end
            makeNotification(i, "Crack finished", net.possiblePasswords.size() + " possible passwords found", 100, false);
        }
    }

    private void makeNotification(int notificationId, String contentTitle, String contentText, int progress, boolean onGoing) {
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

        notificationManager.notify(notificationId, n);
    }
}
