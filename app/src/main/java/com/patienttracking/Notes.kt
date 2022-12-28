package com.patienttracking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.NotesAdapter
import com.patienttracking.utilities.NotesViewModel
import java.util.*

class Notes : AppCompatActivity() {

    lateinit var frameLayout: FrameLayout
    lateinit var notetitle: EditText
    lateinit var notebody: EditText
    lateinit var note_add_button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes)
        frameLayout = findViewById<FrameLayout>(R.id.add_new_frame)
        notetitle = findViewById(R.id.notetitle)
        notebody = findViewById(R.id.notebody)
        note_add_button = findViewById(R.id.note_add_button)

        val notes = ArrayList<NotesViewModel>()
        val db = DB_Helper(this, null)
        val cursor = db.retrieve("SELECT * FROM notes WHERE status <>'deleted';")
        cursor!!.moveToFirst()
        if(cursor != null && cursor.getCount() > 0) {
            notes.add(
                NotesViewModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
            while (cursor.moveToNext()) {
                notes.add(
                    NotesViewModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                    )
                )
            }
            cursor.close()
        }
        val recyclerview = findViewById<RecyclerView>(R.id.notes_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = NotesAdapter(this, notes)
        recyclerview.adapter = adapter
    }

    fun newNote(view: View){
        frameLayout.visibility = View.VISIBLE
        note_add_button.setText("Add")
        note_add_button.setOnClickListener(View.OnClickListener {addNote()})
    }

    fun addNote() {
        var note_title = notetitle.text.toString()
        var note_body = notebody.text.toString()

        if(note_title == "" || note_body == ""){
            Toast.makeText(this, "Note title & body is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val db = DB_Helper(this, null)
        val values = ContentValues()
        values.put("title", note_title)
        values.put("body", note_body)
        values.put("time", System.currentTimeMillis())
        values.put("status", "true")
        db.add(values, "notes")

        reload()
    }

    fun openNote(title: String, body: String, id: String){
        notetitle.setText(title)
        notebody.setText(body)
        frameLayout.visibility = View.VISIBLE
        note_add_button.setText("Update")
        note_add_button.setOnClickListener(View.OnClickListener {
            var note_title = notetitle.text.toString()
            var note_body = notebody.text.toString()
            val db = DB_Helper(this, null)
            val values = ContentValues()
            values.put("title", note_title)
            values.put("body", note_body)
            db.update("notes", values, id, "id")
            reload()
        })
    }

    fun cancelNote(view: View){
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