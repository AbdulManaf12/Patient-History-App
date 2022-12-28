package com.patienttracking.utilities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;
import com.patienttracking.R;
import com.patienttracking.Reminders;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {

        // we will use vibrator first
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        Toast.makeText(context, intent.getStringExtra("title"), Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // setting default ringtone
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        // play ringtone
        ringtone.play();

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.reminders_icon) // notification icon
                .setContentTitle(intent.getStringExtra("title")) // title for notification
                .setContentText(intent.getStringExtra("body")) // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent1 = new Intent(context, Reminders.class);
        @SuppressLint("WrongConstant") PendingIntent pi = PendingIntent.getActivity(context, Integer.parseInt("0"+intent.getStringExtra("dateString")), intent1, Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt("0"+intent.getStringExtra("dateString")), intent, 0);
        alarmManager.cancel(pendingIntent);
        DB_Helper db = new DB_Helper(context, null);
        Cursor cursor_time = db.retrieve("SELECT next_time FROM reminders WHERE id="+intent.getStringExtra("id"));
        cursor_time.moveToFirst();
        ContentValues values = new ContentValues();
        values.put("next_time", Long.parseLong(cursor_time.getString(0))+(1000*60*60*24));
        db.update("reminders", values, intent.getStringExtra("id"), "id");
    }
}