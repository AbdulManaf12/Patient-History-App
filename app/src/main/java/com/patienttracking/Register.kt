package com.patienttracking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.SharedPreference

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val register_name = findViewById<EditText>(R.id.register_name)
        val register_email = findViewById<EditText>(R.id.register_email)
        val register_password = findViewById<EditText>(R.id.register_password)
        val register_button = findViewById<Button>(R.id.register_button)

        register_button.setOnClickListener(View.OnClickListener {
            val registername = register_name.text.toString()
            val registeremail = register_email.text.toString().lowercase()
            val registerpassword = register_password.text.toString()
            if(registername == "" || registeremail == "" || registerpassword == "")
                Toast.makeText(this, "All the fields must be filled properly!", Toast.LENGTH_SHORT).show()
            else {
                val db = DB_Helper(this, null)
                val values = ContentValues()
                values.put("user_name", registername)
                values.put("user_email", registeremail)
                values.put("user_password", registerpassword)
                values.put("status", "active")
                db.add(values, "users")

                val values1 = ContentValues()
                values1.put("user_email", registeremail)
                values1.put("patient_name", "")
                values1.put("patient_birthday", "")
                values1.put("patient_address", "")
                values1.put("patient_guardian_name", "")
                values1.put("patient_guardian_phone", "")
                values1.put("patient_guardian_nic", "")
                values1.put("patient_family_doctor_name", "")
                values1.put("patient_family_doctor_phone", "")
                values1.put("patient_most_recent_doctor_visit", "")
                db.add(values1, "patients")

                val sp = SharedPreference(this)
                sp.setPreference("isLoggedIn", "true")
                sp.setPreference("user_name", registername)
                sp.setPreference("user_email", registeremail)
                sp.setPreference("blood_pressure", "0")
                sp.setPreference("heart_rate", "0")
                sp.setPreference("calories_burned", "0")

                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    fun openLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}