package com.patienttracking

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.patienttracking.utilities.SharedPreference

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val logout_button = findViewById<ImageButton>(R.id.logout_button)
        logout_button.setOnClickListener(View.OnClickListener {
            val sp = SharedPreference(this)
            sp.setPreference("isLoggedIn", "false")
            sp.removePreference("user_name")
            sp.removePreference("user_email")
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        })
    }

    fun openMedication(view: View) {
        val intent = Intent(this, Patient_Details::class.java)
        startActivity(intent)
        finish()
    }

    fun openReminders(view: View) {
        val intent = Intent(this, Reminders::class.java)
        startActivity(intent)
        finish()
    }

    fun openNotes(view: View) {
        val intent = Intent(this, Notes::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val pushToken = task.result

            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "pushToken: $pushToken")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")
            Log.i("PUSH_TOKEN", "*****************************")

            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("FCM Token", pushToken)
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}