package com.patienttracking

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.SharedPreference

class Patient_Details : AppCompatActivity() {

    var action_button_status: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_details)

        var intent = intent
        if (intent.hasExtra("lat") && intent.hasExtra("lng")) {
            val gmmIntentUri = Uri.parse("google.navigation:q="+ (intent.getExtras()?.getString("lat")) +"," +intent.getExtras()?.getString("lng"))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
                this.startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Failed to open Google Maps", Toast.LENGTH_SHORT).show()
            }
        }
        val sp = SharedPreference(this)

        val patient_name = findViewById<EditText>(R.id.patient_name)
        val patient_birthday = findViewById<EditText>(R.id.patient_birthday)
        val patient_address = findViewById<EditText>(R.id.patient_address)
        val patient_guardian_name = findViewById<EditText>(R.id.patient_guardian_name)
        val patient_guardian_phone = findViewById<EditText>(R.id.patient_guardian_phone)
        val patient_guardian_nic = findViewById<EditText>(R.id.patient_guardian_nic)
        val patient_family_doctor_name = findViewById<EditText>(R.id.patient_family_doctor_name)
        val patient_family_doctor_phone = findViewById<EditText>(R.id.patient_family_doctor_phone)
        val patient_most_recent_doctor_visit = findViewById<EditText>(R.id.patient_most_recent_doctor_visit)
        val details_action_button = findViewById<ImageButton>(R.id.details_action_button)

        val patient_details_blood_pressure = findViewById<TextView>(R.id.patient_details_blood_pressure)
        val patient_details_heart_rate = findViewById<TextView>(R.id.patient_details_heart_rate)
        val patient_details_caloties_burned = findViewById<TextView>(R.id.patient_details_calories_burned)

        patient_details_blood_pressure.text = sp.getPreference("blood_pressure")
        patient_details_heart_rate.text = sp.getPreference("heart_rate")
        patient_details_caloties_burned.text = sp.getPreference("calories_burned")

        val db = DB_Helper(this, null)
        val cursor = db.retrieve("SELECT * FROM patients WHERE user_email='"+sp.getPreference("user_email")+"';")
        Log.i("Count", cursor?.getCount().toString())
        if(cursor != null && cursor.getCount() > 0) {
            cursor!!.moveToFirst()
            patient_name.setText(cursor.getString(2))
            patient_birthday.setText(cursor.getString(3))
            patient_address.setText(cursor.getString(4))
            patient_guardian_name.setText(cursor.getString(5))
            patient_guardian_phone.setText(cursor.getString(6))
            patient_guardian_nic.setText(cursor.getString(7))
            patient_family_doctor_name.setText(cursor.getString(8))
            patient_family_doctor_phone.setText(cursor.getString(9))
            patient_most_recent_doctor_visit.setText(cursor.getString(10))
        }

        details_action_button.setOnClickListener(View.OnClickListener {
            if(action_button_status == "0") {
                details_action_button.setImageResource(R.drawable.ic_baseline_done_outline_24)
                action_button_status = "1"
                patient_name.isEnabled = true
                patient_birthday.isEnabled = true
                patient_address.isEnabled = true
                patient_guardian_name.isEnabled = true
                patient_guardian_phone.isEnabled = true
                patient_guardian_nic.isEnabled = true
                patient_family_doctor_name.isEnabled = true
                patient_family_doctor_phone.isEnabled = true
                patient_most_recent_doctor_visit.isEnabled = true
            } else {
                patient_name.isEnabled = false
                patient_birthday.isEnabled = false
                patient_address.isEnabled = false
                patient_guardian_name.isEnabled = false
                patient_guardian_phone.isEnabled = false
                patient_guardian_nic.isEnabled = false
                patient_family_doctor_name.isEnabled = false
                patient_family_doctor_phone.isEnabled = false
                patient_most_recent_doctor_visit.isEnabled = false

                val db = DB_Helper(this, null)
                val values = ContentValues()
                values.put("patient_name", patient_name.text.toString())
                values.put("patient_birthday", patient_birthday.text.toString())
                values.put("patient_address", patient_address.text.toString())
                values.put("patient_guardian_name", patient_guardian_name.text.toString())
                values.put("patient_guardian_phone", patient_guardian_phone.text.toString())
                values.put("patient_guardian_nic", patient_guardian_nic.text.toString())
                values.put("patient_family_doctor_name", patient_family_doctor_name.text.toString())
                values.put("patient_family_doctor_phone", patient_family_doctor_phone.text.toString())
                values.put("patient_most_recent_doctor_visit", patient_most_recent_doctor_visit.text.toString())
                db.update("patients", values, sp.getPreference("user_email"), "user_email")
                finish();
                startActivity(getIntent());

                details_action_button.setImageResource(R.drawable.ic_baseline_edit_24)
                action_button_status = "0"
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}