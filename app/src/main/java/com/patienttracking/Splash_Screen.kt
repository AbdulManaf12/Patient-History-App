package com.patienttracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.SharedPreference


class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val sp = SharedPreference(this)

        Handler(Looper.getMainLooper()).postDelayed({
            var isNew = sp.getPreference("isNew")
            if(isNew == "false"){
                var isLoggedIn = sp.getPreference("isLoggedIn")
                if(isLoggedIn == "true"){
                    Toast.makeText(this, "Welcome back "+sp.getPreference("user_name"), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val db = DB_Helper(this, null)
                db.createTable("CREATE TABLE reminders (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, body TEXT NOT NULL, time TEXT NOT NULL, next_time TEXT NOT NULL, status TEXT NOT NULL)")
                db.createTable("CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, body TEXT NOT NULL, time TEXT NOT NULL, status TEXT NOT NULL)")
                db.createTable("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT NOT NULL, user_email TEXT NOT NULL UNIQUE, user_password TEXT NOT NULL, status TEXT NOT NULL)")
                db.createTable("CREATE TABLE patients (id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT NOT NULL UNIQUE, patient_name TEXT, patient_birthday TEXT, patient_address TEXT, patient_guardian_name TEXT, patient_guardian_phone TEXT, patient_guardian_nic TEXT, patient_family_doctor_name TEXT, patient_family_doctor_phone TEXT, patient_most_recent_doctor_visit TEXT)")
                sp.setPreference("isNew", "false")
                val intent = Intent(this, Welcome::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}