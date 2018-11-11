package com.mobitant.bestfood;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyMS";
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨.");

        String from = remoteMessage.getFrom();
        Map<String, String> resData = remoteMessage.getData();
        String command = resData.get("command");
        String type = resData.get("type");
        String data = resData.get("data");

        Log.v(TAG, "from : " + from + ", command : " + command + ", type : " + type + ", data : " + data);

        sendToActivity(getApplicationContext(), from, command, type, data);
    }

    private void sendToActivity(Context context, String from, String command, String type, String data) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("command", command);
        intent.putExtra("type", type);
        intent.putExtra("data", data);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //중요도 설정, MAX(5), HIGH(4), DEFAULT(3), LOW(2), MIN(1), NONE(0), UNSPECIFIED(-1000), MAX를 사용하려면 @SuppressLint("WrongConstant") 를 맨 앞에 붙여야함
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "SoMeeting", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_user_icon)
                .setTicker("Hearty365")
                .setContentIntent(pendingIntent)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("소미팅")
                .setContentText(data)
                .setContentInfo("Info");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());



        /*
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"뭔채널이여ㅡㅡ");

notificationBuilder.setSmallIcon(R.drawable.ic_user_icon)
                .setContentTitle("소모임")
                .setContentText(data)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        //PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP);
        //wakeLock.acquire(5000);
        notificationManager.notify(0,notificationBuilder.build());
        //context.startActivity(intent);


        */
    }

}
