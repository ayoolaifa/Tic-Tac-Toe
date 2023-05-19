package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

// Broadcast receiver that creates and cause a notification to appear
public class MakeMove extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("tictactoe", "Tic Tac Toe", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "tictactoe")
                .setSmallIcon(R.drawable.notifictionicon)
                .setContentTitle("Tic Tac Toe")
                .setContentText("Hurry Up And Return To Make Your Move!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());



    }
}
