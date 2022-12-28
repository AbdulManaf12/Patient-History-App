package com.patienttracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patienttracking.utilities.AlarmReceiver
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.RemindersAdapter
import com.patienttracking.utilities.RemindersViewModel
import java.text.SimpleDateFormat
import java.util.*

class Reminders : AppCompatActivity() {
    var alarmTimePicker: TimePicker? = null
    var pendingIntent: PendingIntent? = null
    var alarmManager: AlarmManager? = null
    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reminders)
        frameLayout = findViewById<FrameLayout>(R.id.add_new_frame)
        alarmTimePicker = findViewById<View>(R.id.timePicker) as TimePicker
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val reminders = ArrayList<RemindersViewModel>()

        val db = DB_Helper(this, null)
        val cursor = db.retrieve("SELECT * FROM reminders WHERE status <>'deleted';")
        cursor!!.moveToFirst()
        if(cursor != null && cursor.getCount() > 0) {
            Log.i(
                "Reminders ",
                cursor.getString(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getString(3) + " | " + cursor.getString(4) + " | " + cursor.getString(5)
            )
            reminders.add(
                RemindersViewModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                )
            )
            while (cursor.moveToNext()) {
                reminders.add(
                    RemindersViewModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                    )
                )
                Log.i(
                    "Reminders ",
                    cursor.getString(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getString(3) + " | " + cursor.getString(4) + " | " + cursor.getString(5)
                )
            }
            cursor.close()
        }
        val recyclerview = findViewById<RecyclerView>(R.id.reminders_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = RemindersAdapter(this, reminders)
        recyclerview.adapter = adapter
    }

    fun newReminder(view: View){
        frameLayout.visibility = View.VISIBLE
    }

    fun addReminder(view: View) {
        var time: Long
        var remindertitle: EditText = findViewById(R.id.remindertitle)
        var reminder_title = remindertitle.text.toString()
        var reminderbody: EditText = findViewById(R.id.reminderbody)
        var reminder_body = reminderbody.text.toString()

        if(reminder_title == "" || reminder_title == null){
            Toast.makeText(this, "Reminder title is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val calendar: Calendar = Calendar.getInstance()

        // calendar is called to get current time in hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker!!.currentHour)
        calendar.set(Calendar.MINUTE, alarmTimePicker!!.currentMinute)

        time = calendar.getTimeInMillis() - calendar.getTimeInMillis() % 60000
        var timeFormat = SimpleDateFormat("hh:mm a")
        Log.i("Time", timeFormat.format(Date((time).toLong())))

        val db = DB_Helper(this, null)
        val values = ContentValues()
        values.put("title", reminder_title)
        values.put("body", reminder_body)
        values.put("time", time.toString())
        values.put("next_time", time.toString())
        values.put("status", "true")
        db.add(values, "reminders")
        val cursor_id = db.retrieve("SELECT id FROM reminders ORDER BY id DESC limit 1")
        cursor_id!!.moveToFirst()
        val id = cursor_id.getString(0)

        val simpleDateFormat = SimpleDateFormat("MMddHHmm")
        val dateString = simpleDateFormat.format(Date((time).toLong()))

        // using intent i have class AlarmReceiver class which inherits
        // BroadcastReceiver
        // we call broadcast using pendingIntent
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("title", reminder_title)
        intent.putExtra("body", reminder_body)
        intent.putExtra("id", id)
        intent.putExtra("dateString", dateString)
        pendingIntent = PendingIntent.getBroadcast(this, ("0"+dateString).toInt(), intent, 0)

        // Alarm rings continuously until toggle button is turned off
        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000 * 60 * 60 * 24, pendingIntent)
        //alarmManager!!.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        Toast.makeText(this@Reminders, "Reminder Added...", Toast.LENGTH_SHORT).show()
        frameLayout.visibility = View.GONE

        reload()
    }

    fun cancelReminder(view: View){
        frameLayout.visibility = View.GONE
    }

    fun reload(){
        finish();
        startActivity(getIntent());
    }

    override fun onBackPressed() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}