package com.patienttracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.patienttracking.utilities.DB_Helper
import com.patienttracking.utilities.SharedPreference

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val login_email = findViewById<EditText>(R.id.login_email)
        val login_password = findViewById<EditText>(R.id.login_password)
        val login_button = findViewById<Button>(R.id.login_button)

        login_button.setOnClickListener(View.OnClickListener {
            val loginemail = login_email.text.toString()
            val loginpassword = login_password.text.toString()
            if(loginemail == "" || loginpassword == "")
                Toast.makeText(this, "All the fields must be filled properly!", Toast.LENGTH_SHORT).show()
            else {
                val sp = SharedPreference(this)
                val db = DB_Helper(this, null)
                val cursor = db.retrieve("SELECT * FROM users WHERE user_email='"+loginemail+"' AND user_password='"+loginpassword+"';")
                if(cursor != null && cursor.getCount() > 0) {
                    cursor!!.moveToFirst()
                    sp.setPreference("isLoggedIn", "true")
                    sp.setPreference("user_name", cursor.getString(1))
                    sp.setPreference("user_email", loginemail)
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun openRegister(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }
}