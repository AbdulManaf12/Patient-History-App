package com.patienttracking;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

class App extends Application {

    private static Context context;
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
        createNotificationChannnel();
    }

    public static Context getAppContext() {
        return context;
    }

    private void createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
