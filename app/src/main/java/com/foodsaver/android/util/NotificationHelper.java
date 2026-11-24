package com.foodsaver.android.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.foodsaver.android.R;
import com.foodsaver.android.ui.MainActivity;

public final class NotificationHelper {

    public static final String CHANNEL_CLIENT   = "client_updates";
    public static final String CHANNEL_MERCHANT = "merchant_updates";

    private NotificationHelper() {}

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        NotificationChannel client = new NotificationChannel(
                CHANNEL_CLIENT,
                "Notifications client",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        client.setDescription("Confirmation de réservation et paniers disponibles");
        client.enableLights(true);
        client.setLightColor(Color.GREEN);

        NotificationChannel merchant = new NotificationChannel(
                CHANNEL_MERCHANT,
                "Notifications commerçant",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        merchant.setDescription("Nouvelles réservations reçues");
        merchant.enableLights(true);
        merchant.setLightColor(Color.BLUE);

        manager.createNotificationChannel(client);
        manager.createNotificationChannel(merchant);
    }

    private static PendingIntent buildContentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
                context,
                0,
                intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        : PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static void showClientNotification(Context context, String title, String message, int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_CLIENT)
                .setSmallIcon(R.mipmap.ic_launcher) // uses app icon
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(buildContentIntent(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(id, builder.build());
    }

    public static void showMerchantNotification(Context context, String title, String message, int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_MERCHANT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(buildContentIntent(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(id, builder.build());
    }
}

