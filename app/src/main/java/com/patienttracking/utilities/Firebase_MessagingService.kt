package com.patienttracking.utilities

import com.google.firebase.messaging.FirebaseMessagingService
import android.R
import android.annotation.SuppressLint
import android.app.Notification
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.os.Build
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONObject
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import com.patienttracking.Patient_Details


class Firebase_MessagingService : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", s)
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
        Log.i("NEW_TOKEN", "******************************")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        var intent = Intent(this, Patient_Details::class.java)
        var request_code = 0

        val params = remoteMessage.data
        val jsonObject = JSONObject(params as Map<*, *>?)
        Log.e("JSON_OBJECT", jsonObject.toString())
        Log.e("function", (jsonObject["function"]).toString())

        if((jsonObject["function"]).toString() == "updates"){
            val sp = SharedPreference(this)
            sp.setPreference("blood_pressure", (jsonObject["blood_pressure"]).toString())
            sp.setPreference("heart_rate", (jsonObject["heart_rate"]).toString())
            sp.setPreference("calories_burned", (jsonObject["calories_burned"]).toString())
            request_code = 1
        } else if ((jsonObject["function"]).toString() == "location_update"){
            intent.putExtra("lat", (jsonObject["lat"]).toString())
            intent.putExtra("lng", (jsonObject["lng"]).toString())
            request_code = 2
        }

        val NOTIFICATION_CHANNEL_ID = "FCM"+request_code.toString()
        val pattern = longArrayOf(0, 1000, 500, 1000)
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "FCM"+request_code.toString(),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = ""
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = pattern
            notificationChannel.enableVibration(true)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }

        @SuppressLint("WrongConstant") val pi = PendingIntent.getActivity(
            this, request_code, intent, Intent.FLAG_ACTIVITY_NEW_TASK)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.background_dark))
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_popup_reminder)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        mNotificationManager.notify(1000, notificationBuilder.build())
    }
}