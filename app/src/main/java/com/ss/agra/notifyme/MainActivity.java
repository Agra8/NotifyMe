package com.ss.agra.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mNotifyButton;
    private Button mUpdateButton;
    private Button mCancelButton;

    private static final String NOTIFICATION_GUIDE_URL =
            "http://developer.android.com/design/patterns/notification.html";
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.ss.agra.notifyme.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION =
            "ccom.ss.agra.notifyme.ACTION_CANCEL_NOTIFICATION";

    private NotificationManager mNotifyManager;
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        setContentView(R.layout.activity_main);

        mNotifyButton = (Button) findViewById(R.id.notify);
        mUpdateButton = (Button) findViewById(R.id.update);
        mCancelButton = (Button) findViewById(R.id.cancel);

        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(false);

        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });

    }
    /**
     * Unregister the receiver when the app is destroyed
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void sendNotification() {
        Intent notificationIntent = new Intent(this,
                MainActivity.class);
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(this,
                        NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Sets up the pending intent to cancel the notification,
        // delivered when the user dismisses the notification
        Intent cancelIntent = new
                Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent =
                PendingIntent.getBroadcast
                        (this, NOTIFICATION_ID, cancelIntent,
                                PendingIntent.FLAG_ONE_SHOT);
        //Sets up the pending intent associated with the Learn More notification action,
        //uses an implicit intent to go to the web.
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent =
                PendingIntent.getActivity
                        (this, NOTIFICATION_ID, learnMoreIntent,
                                PendingIntent.FLAG_ONE_SHOT);
        //Sets up the pending intent to update the notification.Corresponds to a press of the
        //Update Me! button
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent =
                 PendingIntent.getBroadcast
                        (this, NOTIFICATION_ID, updateIntent,
                                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .setSmallIcon(R.drawable.ic_android)
                        .setContentIntent(notificationPendingIntent)
                        //setPriority
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        //setDefault
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .addAction(R.drawable.ic_learn_more,
                                getString(R.string.learn_more),
                                learnMorePendingIntent)
                        .addAction(R.drawable.ic_update,
                                getString(R.string.update), updatePendingIntent)
                        .setDeleteIntent(cancelPendingIntent);

        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(true);
        mCancelButton.setEnabled(true);

    }

    //buat method update notification
    public void updateNotification() {
        //Load the drawable resource into the a bitmap image
        Bitmap androidImage =
                BitmapFactory.decodeResource(getResources(), R.drawable.mascot_);
//Sets up the pending intent that is delivered when the notification is clicked
        Intent notificationIntent = new Intent(this,
                MainActivity.class);
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity
                        (this, NOTIFICATION_ID, notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

// Sets up the pending intent to cancel the notification,
// delivered when the user dismisses the notification
        Intent cancelIntent = new
                Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent =
                PendingIntent.getBroadcast
                        (this, NOTIFICATION_ID, cancelIntent,
                                PendingIntent.FLAG_ONE_SHOT);
//Sets up the pending intent associated with the Learn More notification action,
//uses an implicit intent to go to the web.
                Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent =
                PendingIntent.getActivity
                        (this, NOTIFICATION_ID, learnMoreIntent,
                                PendingIntent.FLAG_ONE_SHOT);
//Build the updated notification
        NotificationCompat.Builder notifyBuilder = new
                NotificationCompat.Builder(this)

                .setContentTitle(getString(R.string.notification_title))

                .setContentText(getString(R.string.notification_text2))
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDeleteIntent(cancelPendingIntent)
                .addAction(R.drawable.ic_learn_more,
                        getString(R.string.learn_more),
                        learnMorePendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle(getString(R.string.notification_updated)));
//Disable the update button, leaving only the option to cancel
        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(true);
//Deliver the notification
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);
    }

    //buat method cancelNotif
    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);

        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(false);

    }
    private class NotificationReceiver extends BroadcastReceiver {
        /**
         * Gets the action from the incoming broadcast intent and
         responds accordingly
         * @param context Context of the app when the broadcast is
        received.
         * @param intent The broadcast intent containing the
        action.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;

                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;

            }
        }
    }
}
