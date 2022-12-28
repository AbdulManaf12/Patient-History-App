package com.patienttracking.utilities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.patienttracking.R
import java.text.SimpleDateFormat
import java.util.*
import com.patienttracking.Reminders

class RemindersAdapter (private val context: Context, private val reminderList: List<RemindersViewModel>) : RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {

    var alarmManager: AlarmManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminders_single, parent, false)

        alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = reminderList[position]
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val timeFormat = SimpleDateFormat("hh:mm a")
        holder.reminder_single_title.text = reminder.title
        holder.reminder_single_body.text = reminder.body
        holder.reminder_single_date.text = dateFormat.format(Date((reminder.next_time).toLong()))
        holder.reminder_single_time.text = timeFormat.format(Date((reminder.next_time).toLong()))
        holder.reminder_single_status.isChecked = reminder.status == "true"
        holder.reminder_single_status.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val simpleDateFormat = SimpleDateFormat("MMddHHmm")
            val dateString = simpleDateFormat.format(Date((reminder.time).toLong()))
            val intent = Intent(context, AlarmReceiver::class.java)
            var pendingIntent = PendingIntent.getBroadcast(context, ("0"+dateString).toInt(), intent, 0)
            if (isChecked) {
                val db = DB_Helper(context, null)
                val values = ContentValues()
                values.put("status", "true")
                db.update("reminders", values, reminder.id, "id")
                alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, (reminder.time).toLong(), 1000 * 60 * 60 * 24, pendingIntent)
                Toast.makeText(context, "Reminder switched on...", Toast.LENGTH_SHORT).show()
            } else {
                alarmManager!!.cancel(pendingIntent)
                val db = DB_Helper(context, null)
                val values = ContentValues()
                values.put("status", "false")
                db.update("reminders", values, reminder.id, "id")
                Toast.makeText(context, "Reminder switched off...", Toast.LENGTH_SHORT).show()
            }
        })

        holder.reminder_single_bin.setOnClickListener(View.OnClickListener {
            val db = DB_Helper(context, null)
            val values = ContentValues()
            values.put("status", "deleted")
            db.update("reminders", values, reminder.id, "id")
            Toast.makeText(context, "Reminder deleted...", Toast.LENGTH_SHORT).show()
            (context as Reminders).reload()
        })

    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val reminder_single_title: TextView = itemView.findViewById(R.id.reminder_single_title)
        val reminder_single_body: TextView = itemView.findViewById(R.id.reminder_single_body)
        val reminder_single_date: TextView = itemView.findViewById(R.id.reminder_single_date)
        val reminder_single_time: TextView = itemView.findViewById(R.id.reminder_single_time)
        val reminder_single_status: Switch = itemView.findViewById(R.id.reminder_single_status)
        val reminder_single_bin: ImageView = itemView.findViewById(R.id.reminder_single_bin)
    }
}