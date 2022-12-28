package com.patienttracking.utilities

import android.app.AlarmManager
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.patienttracking.R
import com.patienttracking.Notes
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter (private val context: Context, private val noteList: List<NotesViewModel>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var alarmManager: AlarmManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_single, parent, false)

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val timeFormat = SimpleDateFormat("hh:mm a")
        holder.note_single_title.text = note.title
        holder.note_single_body.text = note.body
        holder.note_single_date.text = dateFormat.format(Date((note.time).toLong()))
        holder.note_single_time.text = timeFormat.format(Date((note.time).toLong()))

        holder.note_single_bin.setOnClickListener(View.OnClickListener {
            val db = DB_Helper(context, null)
            val values = ContentValues()
            values.put("status", "deleted")
            db.update("notes", values, note.id, "id")
            Toast.makeText(context, "Note deleted...", Toast.LENGTH_SHORT).show()
            (context as Notes).reload()
        })

        holder.note_single_item.setOnClickListener(View.OnClickListener {
            (context as Notes).openNote(note.title, note.body, note.id)
        })
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val note_single_title: TextView = itemView.findViewById(R.id.note_single_title)
        val note_single_body: TextView = itemView.findViewById(R.id.note_single_body)
        val note_single_date: TextView = itemView.findViewById(R.id.note_single_date)
        val note_single_time: TextView = itemView.findViewById(R.id.note_single_time)
        val note_single_bin: ImageView = itemView.findViewById(R.id.note_single_bin)
        val note_single_item: ConstraintLayout = itemView.findViewById(R.id.note_single_item)
    }
}